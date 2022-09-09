package com.ooka.analysis.feign;

import com.ooka.analysis.product.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("analysis-control")
public interface AnalysisFeignClient {

    @RequestMapping(method = RequestMethod.POST, value = "/products/")
    void createProduct(@RequestBody Product product);
}