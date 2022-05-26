package com.ooka.cli;

import com.ooka.cli.log.CliEntity;
import com.ooka.cli.log.CliRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Locale;
import java.util.Scanner;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.ooka.*"})
@EntityScan("com.ooka.*")
public class CLI {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(CLI.class, args);
        CliEntity c = new CliEntity();
        c.setLog("Started CLI");
        CliRepository cityRepository = applicationContext.getBean(CliRepository.class);
        cityRepository.save(c);
        System.out.println("CLI is running.");

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Please enter number: ");
            String next = sc.next().toLowerCase(Locale.ROOT);
            if (next.equals("exit")) break;
            System.out.println("Your number: " + next);
        }
        CliEntity close = new CliEntity();
        close.setLog("Close CLI");
        cityRepository.save(close);

        applicationContext.close();
    }
}
