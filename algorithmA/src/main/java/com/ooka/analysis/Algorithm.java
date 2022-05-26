package com.ooka.analysis;

import com.ooka.analysis.algorithm_a.AlgorithmA;
import com.ooka.analysis.algorithm_a.AlgorithmARepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.ooka.*"})
@EntityScan("com.ooka.*")
public class Algorithm {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Algorithm.class, args);
        System.out.println("AlgorithmA is running.");
    }
}
