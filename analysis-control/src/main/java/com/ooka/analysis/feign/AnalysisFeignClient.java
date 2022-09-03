package com.ooka.analysis.feign;

import com.ooka.analysis.product.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("analysis-control")
public interface AnalysisFeignClient {

    @RequestMapping(method = RequestMethod.POST, value = "/products")
    void createProduct(@RequestBody Product product);
}