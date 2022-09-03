package com.ooka.analysis.feign;

import com.ooka.analysis.State;
import com.ooka.analysis.product.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("management-systems")
public interface ManagementSystemsFeignClient {

    @PutMapping("management")
    void runAlgorithm(@RequestBody Product product);

    @GetMapping(value = "management/state", produces = "application/json")
    ResponseEntity<State> getState();

    @GetMapping(value = "management/result", produces = "application/json")
    ResponseEntity<Integer> getResult();
}
