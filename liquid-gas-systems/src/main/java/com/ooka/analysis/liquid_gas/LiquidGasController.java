package com.ooka.analysis.liquid_gas;

import com.ooka.analysis.State;
import com.ooka.analysis.product.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/liquidGas")
public class LiquidGasController {

    Logger logger = LoggerFactory.getLogger(LiquidGasController.class);

    @Autowired
    LiquidGasRepository liquidGasRepository;

    private State state = State.IDLE;
    @Value("${spring.application.name}")
    private String appName;

    private int lastResult = 0;

    @PutMapping("")
    public void runAlgorithm(@RequestBody Product product) {
        new Thread(() -> run(product)).start();
    }

    private void run(Product product) {
        logger.info("Starting Liquid and Gas Systems Analysis");
        if (state != State.RUNNING) {
            state = State.RUNNING;
            int analysisTime = 7500;
//                    ThreadLocalRandom.current().nextInt(100, 10000);
            LiquidGasEntity alg = new LiquidGasEntity();
            try {
                Thread.sleep(analysisTime);
                if (Math.random() < 0.1) {
                    throw new Exception();
                }
                lastResult = product.hashCode();
                state = State.SUCCEEDED;
                alg.setLog("Analysis of " + product.getAuxiliaryPTO() + "Succeeded. Analysis time: " + analysisTime + "ms");
            } catch (Exception e) {
                state = State.FAILED;
                alg.setLog("Analysis failed.");
            }
            liquidGasRepository.save(alg);
        }
        logger.info("Finished Liquid and Gas Systems Analysis");
    }

    @GetMapping(value = "result", produces = "application/json")
    public ResponseEntity<Integer> getResult() {
        HttpStatus status;
        if (state == State.SUCCEEDED) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.NO_CONTENT;
        }
        return new ResponseEntity<>(lastResult, status);
    }

    @GetMapping("/{liquidGasId}")
    public String readLog(@PathVariable Long liquidGasId) {
        Optional<LiquidGasEntity> liquidGas = liquidGasRepository.findById(liquidGasId);
        if (liquidGas.isPresent()) {
            return liquidGas.get().getLog();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Log with " + liquidGasId + " not found");
    }

    @GetMapping(value = "/state", produces = "application/json")
    public ResponseEntity<State> getState() {
        return new ResponseEntity<>(state, HttpStatus.OK);
    }

    @DeleteMapping("/{liquidaGasId}")
    public void deleteLog(@PathVariable Long liquidaGasId) {
        Optional<LiquidGasEntity> liquidGas = liquidGasRepository.findById(liquidaGasId);
        if (liquidGas.isPresent()) {
            liquidGasRepository.delete(liquidGas.get());
            return;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Log with " + liquidaGasId + " not found");
    }
}
