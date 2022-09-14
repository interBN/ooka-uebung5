package com.ooka.cli;

import com.ooka.analysis.State;
import com.ooka.analysis.product.Product;
import com.ooka.cli.log.CliEntity;
import com.ooka.cli.log.CliRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Locale;
import java.util.Scanner;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.ooka.*"})
@EntityScan("com.ooka.*")
@Deprecated
public class Cli {

    private static final String baseUrlAnalysisControl = "http://localhost:8072/analysiscontrol";

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Cli.class, args);
        CliEntity c = new CliEntity();
        c.setLog("Started CLI");
        CliRepository cityRepository = applicationContext.getBean(CliRepository.class);
        cityRepository.save(c);

        RestTemplate restTemplate = new RestTemplate();

        System.out.println("CLI is running.");
        while (true) {
            printLine();
            String[] options = {"Start", "State", "Result"};
            int option = printMenu("Please choose a number: ", options);
            if (option == Integer.MIN_VALUE) {
                break;
            } else if (option == 0) {
                start(restTemplate);
            } else if (option == 1) {
                state(restTemplate);
            } else if (option == 2) {
                result(restTemplate);
            }
        }
        CliEntity close = new CliEntity();
        close.setLog("Close CLI");
        cityRepository.save(close);

        applicationContext.close();
    }

    private static void start(RestTemplate restTemplate) {
        restTemplate.put(baseUrlAnalysisControl + "/run", Product.class);
        System.out.println("The Analysis-Control has been started.");
    }

    private static void state(RestTemplate restTemplate) {
        ResponseEntity<State> entity = restTemplate.getForEntity(baseUrlAnalysisControl + "/state", State.class);
        System.out.println("Statuscode: " + entity.getStatusCode());
        System.out.println("Header: " + entity.getHeaders());
        if (entity.hasBody()) {
            State state = entity.getBody();
            System.out.println("State: " + state);
        }
    }

    private static void result(RestTemplate restTemplate) {
        ResponseEntity<Integer> entity = restTemplate.getForEntity(baseUrlAnalysisControl + "/result", Integer.class);
        System.out.println("Statuscode: " + entity.getStatusCode());
        System.out.println("Header: " + entity.getHeaders());
        if (entity.hasBody()) {
            Integer state = entity.getBody();
            System.out.println("Result: " + state);
        }
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
