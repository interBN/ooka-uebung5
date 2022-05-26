package com.ooka.analysis.algorithm_a;

import com.ooka.analysis.State;
import com.ooka.analysis.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/algorithmA")
public class AlgorithmAController {

    @Autowired
    AlgorithmARepository algorithmARepository;

    private State state = State.IDLE;

    @PutMapping("")
    public void runAlgorithm(@RequestBody Product product) {
        new Thread(() -> {
            run(product);
        }).start();
    }

    private void run(Product product) {
        if (state == State.IDLE) {
            state = State.RUNNING;
            int analysisTime = 5000;
//                    ThreadLocalRandom.current().nextInt(100, 10000);
            AlgorithmAEntity alg = new AlgorithmAEntity();
            try {
                Thread.sleep(analysisTime);
                state = State.SUCCEEDED;
                alg.setLog(product.getStartingSystem() + " succeded. Analysis time: " + analysisTime + "ms");
            } catch (Exception e) {
                state = State.FAILED;
                alg.setLog("Analysis failed.");
            }
            algorithmARepository.save(alg);
        }
    }

    @GetMapping("/{algorithmAId}")
    public String readLog(@PathVariable Long algorithmAId) {
        Optional<AlgorithmAEntity> algorithmA = algorithmARepository.findById(algorithmAId);
        if (algorithmA.isPresent()) {
            return algorithmA.get().getLog();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Log with " + algorithmAId + " not found");
    }

    @GetMapping("/state")
    public State getState() {
        return state;
    }

    @DeleteMapping("/{algorithmAId}")
    public void deleteLog(@PathVariable Long algorithmAId) {
        Optional<AlgorithmAEntity> algorithmA = algorithmARepository.findById(algorithmAId);
        if (algorithmA.isPresent()) {
            algorithmARepository.delete(algorithmA.get());
            return;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Log with " + algorithmAId + " not found");
    }
}
