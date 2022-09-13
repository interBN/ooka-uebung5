package com.ooka.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.ooka.*"})
@EntityScan("com.ooka.*")
public class PowerSystems {
    static Logger logger = LoggerFactory.getLogger(PowerSystems.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(PowerSystems.class, args);
        logger.info("Power System Analysis is running.");
    }
}
