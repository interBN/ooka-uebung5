package com.ooka.analysis.power;

import com.ooka.analysis.State;
import com.ooka.analysis.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/powerSystems")
public class PowerSystemsController {

    @Autowired
    PowerSystemsRepository powerSystemsRepository;

    private State state = State.IDLE;

    @PutMapping("")
    public void runAlgorithm(@RequestBody Product product) {
        new Thread(() -> run(product)).start();
    }

    private void run(Product product) {
        System.out.println("Starting Power Systems Analysis");
        if (state != State.RUNNING) {
            state = State.RUNNING;
            int analysisTime = 5000;
//                    ThreadLocalRandom.current().nextInt(100, 10000);
            PowerSystemsEntity alg = new PowerSystemsEntity();
            try {
                Thread.sleep(analysisTime);
                if (Math.random() < 0.2) {
                    throw new Exception();
                }
                state = State.SUCCEEDED;
                alg.setLog("Analysis of " + product.getStartingSystem() + "Succeeded. Analysis time: " + analysisTime + "ms");
            } catch (Exception e) {
                state = State.FAILED;
                alg.setLog("Analysis failed.");
            }
            powerSystemsRepository.save(alg);
        }
        System.out.println("Finished Power Systems Analysis");
    }

    @GetMapping(value = "result", produces = "application/json")
    public ResponseEntity<Integer> getResult() {
        HttpStatus status;
        if (state == State.SUCCEEDED) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.NO_CONTENT;
        }
        return new ResponseEntity<>(15635, status);
    }

    @GetMapping("/{powerSystemsId}")
    public String readLog(@PathVariable Long powerSystemsId) {
        Optional<PowerSystemsEntity> powerSystems = powerSystemsRepository.findById(powerSystemsId);
        if (powerSystems.isPresent()) {
            return powerSystems.get().getLog();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Log with " + powerSystemsId + " not found");
    }

    @GetMapping(value = "/state", produces = "application/json")
    public ResponseEntity<State> getState() {
        return new ResponseEntity<>(state, HttpStatus.OK);
    }

    @DeleteMapping("/{powerSystemsId}")
    public void deleteLog(@PathVariable Long powerSystemsId) {
        Optional<PowerSystemsEntity> powerSystems = powerSystemsRepository.findById(powerSystemsId);
        if (powerSystems.isPresent()) {
            powerSystemsRepository.delete(powerSystems.get());
            return;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Log with " + powerSystemsId + " not found");
    }

    @Value("${spring.application.name}")
    private String appName;
}
