package com.ooka.analysis;

import com.ooka.analysis.feign.AnalysisFeignClient;
import com.ooka.analysis.feign.LiquidGasFeignClient;
import com.ooka.analysis.feign.ManagementSystemsFeignClient;
import com.ooka.analysis.feign.PowerSystemsFeignClient;
import com.ooka.analysis.product.Product;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Date;
import java.util.function.Function;

@RestController
@RequestMapping("/analysiscontrol")
@EnableFeignClients
public class AnalysisController {

    Logger logger = LoggerFactory.getLogger(AnalysisController.class);
    private State state = State.IDLE;
    private Integer result = null;
    private CircuitBreakerRegistry circuitBreakerRegistry = null;
    private RetryRegistry retryRegistry = null;

    @Autowired
    private AnalysisFeignClient analysisClient;
    @Autowired
    private LiquidGasFeignClient liquidGasClient;
    @Autowired
    private ManagementSystemsFeignClient managementSystemsClient;
    @Autowired
    private PowerSystemsFeignClient powerSystemsClient;

    @Value("${spring.application.name}")
    private String appName;

    @GetMapping(value = "/state", produces = "application/json")
    public ResponseEntity<State> getState() {
        return new ResponseEntity<>(state, HttpStatus.OK);
    }

    @PutMapping("/run")
    public void runController(@RequestBody Product product) {
        new Thread(() -> {
            run(product);
        }).start();
    }

    private void run(Product product) {
        if (state != State.RUNNING) {
            long start = new Date().getTime();

            if (circuitBreakerRegistry == null) {
                circuitBreakerRegistry = initCircuitBreakerRegistry();
            }
            if (retryRegistry == null) {
                retryRegistry = initRetryRegistry();
            }
            state = State.RUNNING;
            if (product == null) {
                product = getDummyProduct();
            }
            analysisClient.createProduct(product);

            logger.info("Starting algorithms");

            CircuitBreaker circuitBreakerA = circuitBreakerRegistry.circuitBreaker("powerSystems");
            Retry retryA = retryRegistry.retry("powerSystems");
            Function<Product, Integer> decoratedA = Retry.decorateFunction(retryA,
                    CircuitBreaker.decorateFunction(circuitBreakerA, this::runPowerSystems));

            int resultA;
            try {
                resultA = decoratedA.apply(product);
            } catch (Exception ignored) {
                resultA = 0;
                logger.warn("Power Systems failed");
            }

            CircuitBreaker circuitBreakerB = circuitBreakerRegistry.circuitBreaker("liquidGasSystems");
            Retry retryB = retryRegistry.retry("liquidGasSystems");
            Function<Product, Integer> decoratedB = Retry.decorateFunction(retryB,
                    CircuitBreaker.decorateFunction(circuitBreakerB, this::runLiquidGasSystems));

            int resultB;
            try {
                resultB = decoratedB.apply(product);
            } catch (Exception ignored) {
                resultB = 0;
                logger.warn("Liquid & Gas Systems failed");
            }

            CircuitBreaker circuitBreakerC = circuitBreakerRegistry.circuitBreaker("managementSystems");
            Retry retryC = retryRegistry.retry("managementSystems");
            Function<Product, Integer> decoratedC = Retry.decorateFunction(retryC,
                    CircuitBreaker.decorateFunction(circuitBreakerC, this::runManagementSystems));

            int resultC;
            try {
                resultC = decoratedC.apply(product);
            } catch (Exception ignored) {
                resultC = 0;
                logger.warn("Management Systems failed");
            }
            result = resultA + resultB + resultC;

            logger.info("---------------------------------------------------------");
            logger.info("Power Systems: " + resultA);
            logger.info("Liquid/Gas Systems: " + resultB);
            logger.info("Management Systems: " + resultC);
            logger.info("Total result: " + result);

            long end = new Date().getTime();
            logger.info("Duration = " + ((end - start) / 1000) + " sec");

            product.setResult(result);
            analysisClient.createProduct(product);

            state = State.SUCCEEDED;
        } else {
            logger.info(AnalysisControl.class.getName() + " is already running. Skip request.");
        }
    }

    private int runPowerSystems(Product product) {
        State algorithmState = null;
        powerSystemsClient.runAlgorithm(product);
        logger.info("Starting Power Systems");

        while (algorithmState != State.SUCCEEDED) {
            try {
                Thread.sleep(500);
                logger.info("---------------------------------------------------------");
                algorithmState = powerSystemsClient.getState().getBody();
                if (algorithmState == State.FAILED) {
                    powerSystemsClient.runAlgorithm(product);
                }
                logger.info("Power Systems => State: " + algorithmState);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.info("---------------------------------------------------------");
        return powerSystemsClient.getResult().getBody();
    }

    private int runLiquidGasSystems(Product product) {
        State algorithmState = null;
        liquidGasClient.runAlgorithm(product);
        logger.info("Starting Liquid and Gas Systems");

        while (algorithmState != State.SUCCEEDED) {
            try {
                Thread.sleep(500);
                logger.info("---------------------------------------------------------");
                algorithmState = liquidGasClient.getState().getBody();
                if (algorithmState == State.FAILED) {
                    liquidGasClient.runAlgorithm(product);
                }
                logger.info("Liquid and Gas Systems => State: " + algorithmState);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.info("---------------------------------------------------------");
        return liquidGasClient.getResult().getBody();
    }

    private int runManagementSystems(Product product) {
        State algorithmState = null;
        managementSystemsClient.runAlgorithm(product);
        System.out.println("Starting Management Systems");

        while (algorithmState != State.SUCCEEDED) {
            try {
                Thread.sleep(500);
                System.out.println("---------------------------------------------------------");
                algorithmState = managementSystemsClient.getState().getBody();
                if (algorithmState == State.FAILED) {
                    managementSystemsClient.runAlgorithm(product);
                }
                System.out.println("Management Systems => State: " + algorithmState);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("---------------------------------------------------------");
        return managementSystemsClient.getResult().getBody();
    }

    private CircuitBreakerRegistry initCircuitBreakerRegistry() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .permittedNumberOfCallsInHalfOpenState(3)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .slidingWindowSize(5)
                .failureRateThreshold(40)
                .build();
        return CircuitBreakerRegistry.of(config);
    }

    private RetryRegistry initRetryRegistry() {
        RetryConfig config = RetryConfig.custom()
                .waitDuration(Duration.ofSeconds(10))
                .maxAttempts(5)
                .build();
        return RetryRegistry.of(config);
    }

    @Deprecated
    private Product getDummyProduct() {
        Product product = new Product();
        product.setStartingSystem("testStartingSystem");
        product.setAuxiliaryPTO("A");
        product.setOilSystem("B");
        product.setFuelSystem("C");
        product.setCoolingSystem("D");
        product.setExhaustSystem("E");
        product.setMountingSystem("F");
        product.setEngineManagementSystem("G");
        product.setMonitoringSystem("H");
        product.setPowerTransmission("I");
        product.setGearbox("J");
        product.setMountingSystem("K");
        return product;
    }

    @GetMapping(value = "result", produces = "application/json")
    public ResponseEntity<Integer> getResult() {
        HttpStatus status;
        if (state == State.SUCCEEDED) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.NO_CONTENT;
        }
        return new ResponseEntity<>(result, status);
    }
}
