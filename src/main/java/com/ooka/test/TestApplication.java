package com.ooka.test;

import com.ooka.test.entities.City;
import com.ooka.test.entities.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.ooka.*"})
@EntityScan("com.ooka.*")
public class TestApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(TestApplication.class, args);
        City berlin = new City();
        berlin.setName("Berlin");
        berlin.setCapital(true);

        City cologne = new City();
        cologne.setName("Cologne");
        cologne.setCapital(false);

        CityRepository cityRepository = applicationContext.getBean(CityRepository.class);
        cityRepository.save(berlin);
        cityRepository.save(cologne);
        System.out.println("Hi!");
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
