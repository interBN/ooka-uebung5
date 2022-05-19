package com.ooka.uebung5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Uebung5Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Uebung5Application.class, args);
    }

    public interface SaySomethingService {
        String saySomething();
    }

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

    /**
     * A
     */

    @Component
    @Qualifier("sayHelloService")
    public class SayHelloService implements SaySomethingService {
        @Override
        public String saySomething() {
            return "Hello!";
        }
    }

    /**
     * B
     */

    @Configuration
    public class SaySomethingConfiguration {
        @Bean
        @Primary
        public SaySomethingConfigurableService saySomethingConfigurableService() {
            SaySomethingConfigurableService saySomethingConfigurableService1 = new SaySomethingConfigurableService();
            saySomethingConfigurableService1.setWhatToSay("Bye");
            return saySomethingConfigurableService1;
        }
    }

    public class SaySomethingConfigurableService implements SaySomethingService {

        private String whatToSay = "";

        @Override
        public String saySomething() {
            return whatToSay;
        }

        public String getWhatToSay() {
            return whatToSay;
        }

        public void setWhatToSay(String whatToSay) {
            this.whatToSay = whatToSay;
        }
    }
}
