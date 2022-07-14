package com.ooka.analysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.ooka.*"})
@EntityScan("com.ooka.*")
public class LiquidGas {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(LiquidGas.class, args);
        System.out.println("Liquid/Gas Systems Analysis is running.");
    }
}
