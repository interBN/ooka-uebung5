package com.ooka.analysis;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.ooka.analysis.feign.AnalysisFeignClient;
import com.ooka.analysis.feign.LiquidGasFeignClient;
import com.ooka.analysis.feign.ManagementSystemsFeignClient;
import com.ooka.analysis.feign.PowerSystemsFeignClient;
import com.ooka.analysis.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@RestController
@RequestMapping("/analysiscontrol")
@EnableFeignClients
public class AnalysisController {
    private State state = State.IDLE;
    private Integer result = null;

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
    public void runController() {
        new Thread(this::run).start();
    }

    private void run() {
        if (state != State.RUNNING) {
            long start = new Date().getTime();

            state = State.RUNNING;

            Product product = getDummyProduct();
            analysisClient.createProduct(product);

            powerSystemsClient.runAlgorithm(product);
            System.out.println("Starting Power Systems");
            liquidGasClient.runAlgorithm(product);
            System.out.println("Starting Liquid and Gas Systems");
            managementSystemsClient.runAlgorithm(product);
            System.out.println("Starting Management Systems");

            int resultA, resultB, resultC;
            State stateA = null, stateB = null, stateC = null;

            do {
                try {
                    Thread.sleep(500);
                    System.out.println("---------------------------------------------------------");
                    stateA = powerSystemsClient.getState().getBody();
                    if (stateA == State.FAILED) {
                        powerSystemsClient.runAlgorithm(product);
                    }
                    System.out.println("Power Systems => State: " + stateA);
                    stateB = liquidGasClient.getState().getBody();
                    if (stateB == State.FAILED) {
                        liquidGasClient.runAlgorithm(product);
                    }
                    System.out.println("Liquid and Gas Systems => State: " + stateB);

                    stateC = managementSystemsClient.getState().getBody();
                    if (stateC == State.FAILED) {
                        managementSystemsClient.runAlgorithm(product);
                    }
                    System.out.println("Management Systems => State: " + stateC);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (stateA != State.SUCCEEDED || stateB != State.SUCCEEDED || stateC != State.SUCCEEDED);

            resultA = powerSystemsClient.getResult().getBody() != null ? powerSystemsClient.getResult().getBody() : -1;
            resultB = liquidGasClient.getResult().getBody() != null ? liquidGasClient.getResult().getBody() : -1;
            resultC = managementSystemsClient.getResult().getBody() != null ? managementSystemsClient.getResult().getBody() : -1;
            result = resultA + resultB + resultC;

            System.out.println("---------------------------------------------------------");
            System.out.println("Power Systems: " + resultA);
            System.out.println("Liquid/Gas Systems: " + resultB);
            System.out.println("Management Systems: " + resultC);
            System.out.println("Total result: " + result);

            long end = new Date().getTime();
            System.out.println("Duration = " + ((end - start) / 1000) + " sec");

            product.setResult(result);
            analysisClient.createProduct(product);


            state = State.SUCCEEDED;
        } else {
            System.out.println(AnalysisControl.class.getName() + " is already running. Skip request.");
        }
    }

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
