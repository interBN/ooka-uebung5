package com.ooka.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.ooka.*"})
@EntityScan("com.ooka.*")
public class TestApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(TestApplication.class, args);
//
//        City berlin = new City();
//        berlin.setName("Berlin");
//        berlin.setCapital(true);
//
//        City cologne = new City();
//        cologne.setName("Cologne");
//        cologne.setCapital(false);
//
//        CityRepository cityRepository = applicationContext.getBean(CityRepository.class);
//        cityRepository.save(berlin);
//        cityRepository.save(cologne);
        System.out.println("Hi! I am running.");
    }
}
