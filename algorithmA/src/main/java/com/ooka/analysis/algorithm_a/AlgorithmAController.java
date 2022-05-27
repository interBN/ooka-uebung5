package com.ooka.analysis.algorithm_a;

import com.ooka.analysis.State;
import com.ooka.analysis.product.Product;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/algorithmA")
public class AlgorithmAController {

    @Autowired
    AlgorithmARepository algorithmARepository;

    private State state = State.IDLE;

    @PutMapping("")
    public void runAlgorithm(@RequestBody Product product) {
        new Thread(() -> run(product)).start();
    }

    private void run(Product product) {
        if (state != State.RUNNING) {
            state = State.RUNNING;
            int analysisTime = 5000;
//                    ThreadLocalRandom.current().nextInt(100, 10000);
            AlgorithmAEntity alg = new AlgorithmAEntity();
            try {
                Thread.sleep(analysisTime);
                if (Math.random() < 0.2) {
                    throw new Exception();
                }
                state = State.SUCCEEDED;
                alg.setLog("Analysis of "+ product.getStartingSystem() + "Succeeded. Analysis time: " + analysisTime + "ms");
            } catch (Exception e) {
                state = State.FAILED;
                alg.setLog("Analysis failed.");
            }
            algorithmARepository.save(alg);
        }
    }

    @GetMapping(value = "result", produces = "application/json")
    public ResponseEntity<Object> getResult() {
        JSONObject entity = new JSONObject();
        HttpStatus status;
        entity.put("state", state);
        if (state == State.SUCCEEDED) {
            entity.put("result", 15635);
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.NO_CONTENT;
        }
        return new ResponseEntity<>(entity.toString(), status);
    }

    @GetMapping("/{algorithmAId}")
    public String readLog(@PathVariable Long algorithmAId) {
        Optional<AlgorithmAEntity> algorithmA = algorithmARepository.findById(algorithmAId);
        if (algorithmA.isPresent()) {
            return algorithmA.get().getLog();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Log with " + algorithmAId + " not found");
    }

    @GetMapping(value = "/state", produces = "application/json")
    public ResponseEntity<Object> getState() {
        JSONObject entity = new JSONObject();
        entity.put("state", state);
        return new ResponseEntity<>(entity.toString(), HttpStatus.OK);
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
