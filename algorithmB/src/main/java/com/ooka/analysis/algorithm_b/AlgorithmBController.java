package com.ooka.analysis.algorithm_b;

import com.ooka.analysis.State;
import com.ooka.analysis.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/algorithmB")
public class AlgorithmBController {

    @Autowired
    AlgorithmBRepository algorithmBRepository;

    private State state = State.IDLE;

    @PutMapping("")
    public void runAlgorithm(@RequestBody Product product) {
        new Thread(() -> run(product)).start();
    }

    private void run(Product product) {
        if (state != State.RUNNING) {
            state = State.RUNNING;
            int analysisTime = 7500;
//                    ThreadLocalRandom.current().nextInt(100, 10000);
            AlgorithmBEntity alg = new AlgorithmBEntity();
            try {
                Thread.sleep(analysisTime);
                if (Math.random() < 0.1) {
                    throw new Exception();
                }
                state = State.SUCCEEDED;
                alg.setLog("Analysis of " + product.getAuxiliaryPTO() + "Succeeded. Analysis time: " + analysisTime + "ms");
            } catch (Exception e) {
                state = State.FAILED;
                alg.setLog("Analysis failed.");
            }
            algorithmBRepository.save(alg);
        }
    }

    @GetMapping(value = "result", produces = "application/json")
    public ResponseEntity<Integer> getResult() {
        HttpStatus status;
        if (state == State.SUCCEEDED) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.NO_CONTENT;
        }
        return new ResponseEntity<>(658441, status);
    }

    @GetMapping("/{algorithmBId}")
    public String readLog(@PathVariable Long algorithmBId) {
        Optional<AlgorithmBEntity> algorithmB = algorithmBRepository.findById(algorithmBId);
        if (algorithmB.isPresent()) {
            return algorithmB.get().getLog();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Log with " + algorithmBId + " not found");
    }

    @GetMapping(value = "/state", produces = "application/json")
    public ResponseEntity<State> getState() {
        return new ResponseEntity<>(state, HttpStatus.OK);
    }

    @DeleteMapping("/{algorithmBId}")
    public void deleteLog(@PathVariable Long algorithmBId) {
        Optional<AlgorithmBEntity> algorithmB = algorithmBRepository.findById(algorithmBId);
        if (algorithmB.isPresent()) {
            algorithmBRepository.delete(algorithmB.get());
            return;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Log with " + algorithmBId + " not found");
    }
}
