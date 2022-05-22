package com.ooka.test.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SaySomethingController {
    @Autowired
    @Qualifier("sayHelloService")
    SaySomethingService saySomethingService;

    @GetMapping("/")
    public String home() {
        return saySomethingService.saySomething();
    }
}
