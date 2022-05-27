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
        while (true) {
            printLine();
            String[] options = {"Start", "Status", "Result"};
            int i = printMenu("Please choose a number: ", options);
            if (i == Integer.MIN_VALUE) {
                break;
            }
        }
        CliEntity close = new CliEntity();
        close.setLog("Close CLI");
        cityRepository.save(close);

        applicationContext.close();
    }

    public static int printMenu(String question, String[] options) {
        Scanner sc = new Scanner(System.in);
        System.out.println(question);
        for (int i = 0; i < options.length; i++) {
            System.out.println("[" + i + "] " + options[i]);
        }
        String input = sc.next().toLowerCase(Locale.ROOT);
        if (input.equals("exit")) {
            return Integer.MIN_VALUE;
        }
        int inputInt = Integer.MIN_VALUE;
        try {
            inputInt = Integer.parseInt(input);
        } catch (Exception ignore) {
        }
        if (inputInt < 0 || inputInt >= options.length) {
            System.out.println("Something is wrong with the input: " + input);
            printLine();
            return printMenu(question, options);
        }
        return inputInt;
    }

    public static void printLine() {
        System.out.println("---------------------------------------------------------");
    }
}
