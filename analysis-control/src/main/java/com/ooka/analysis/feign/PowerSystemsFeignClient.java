package com.ooka.analysis.feign;

import com.ooka.analysis.State;
import com.ooka.analysis.product.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("power-systems")
public interface PowerSystemsFeignClient {

    @PutMapping("powerSystems")
    void runAlgorithm(@RequestBody Product product);

    @GetMapping(value = "powerSystems/state", produces = "application/json")
    ResponseEntity<State> getState();

    @GetMapping(value = "powerSystems/result", produces = "application/json")
    ResponseEntity<Integer> getResult();
}
