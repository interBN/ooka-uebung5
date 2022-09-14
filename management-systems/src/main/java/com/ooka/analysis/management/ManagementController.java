package com.ooka.analysis.management;

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
@RequestMapping("/management")
public class ManagementController {
    Logger logger = LoggerFactory.getLogger(ManagementController.class);

    @Autowired
    ManagementRepository managementRepository;

    private State state = State.IDLE;
    @Value("${spring.application.name}")
    private String appName;

    private int lastResult = 0;

    @PutMapping("")
    public void runAlgorithm(@RequestBody Product product) {
        new Thread(() -> run(product)).start();
    }

    private void run(Product product) {
        logger.info("Starting Management Systems Analysis");
        if (state != State.RUNNING) {
            state = State.RUNNING;
            int analysisTime = 5000;
//                    ThreadLocalRandom.current().nextInt(100, 10000);
            ManagementEntity alg = new ManagementEntity();
            try {
                Thread.sleep(analysisTime);
                if (Math.random() < 0.2) {
                    throw new Exception();
                }
                lastResult = product.hashCode() ;
                state = State.SUCCEEDED;
                alg.setLog("Analysis of " + product.getStartingSystem() + "Succeeded. Analysis time: " + analysisTime + "ms");
            } catch (Exception e) {
                state = State.FAILED;
                alg.setLog("Analysis failed.");
            }
            managementRepository.save(alg);
        }
        logger.info("Finished Management Systems Analysis");
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

    @GetMapping("/{managementId}")
    public String readLog(@PathVariable Long managementId) {
        Optional<ManagementEntity> management = managementRepository.findById(managementId);
        if (management.isPresent()) {
            return management.get().getLog();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Log with " + managementId + " not found");
    }

    @GetMapping(value = "/state", produces = "application/json")
    public ResponseEntity<State> getState() {
        return new ResponseEntity<>(state, HttpStatus.OK);
    }

    @DeleteMapping("/{managementId}")
    public void deleteLog(@PathVariable Long managementId) {
        Optional<ManagementEntity> management = managementRepository.findById(managementId);
        if (management.isPresent()) {
            managementRepository.delete(management.get());
            return;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Log with " + managementId + " not found");
    }
}
