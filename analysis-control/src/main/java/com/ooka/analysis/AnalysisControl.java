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
public class AnalysisControl {

    static Logger logger = LoggerFactory.getLogger(AnalysisControl.class);

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(AnalysisControl.class, args);
        logger.info("AnalysisControl is running");
    }
}
