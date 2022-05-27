package com.ooka.analysis;

import com.ooka.analysis.product.Product;
import com.ooka.analysis.product.ProductRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.ooka.*"})
@EntityScan("com.ooka.*")
public class AnalysisController {

    public static void main(String[] args) throws Exception {

        long start = new Date().getTime();

        ConfigurableApplicationContext applicationContext = SpringApplication.run(AnalysisController.class, args);
        System.out.println("Controller is running");
        ProductRepository productRepository = applicationContext.getBean(ProductRepository.class);
        Product product = new Product();
        product.setStartingSystem("testStartingSystem");
        product.setAuxiliaryPTO("A");
        product.setOilSystem("B");
        product.setFuelSystem("C");
        product.setCoolingSystem("D");
        product.setExhaustSystem("E");
        product.setMountingSystem("F");
        product.setEngineManagementSystem("G");
        product.setMonitoringSystem("H");
        product.setPowerTransmission("I");
        product.setGearbox("J");
        product.setMountingSystem("K");
        productRepository.save(product);

        System.out.println("Starting AlgorithmA");
        RestTemplate restTemplate = new RestTemplate();

        int resultA = getResult("http://localhost:8071/algorithmA", product, restTemplate);
        System.out.println("resultA = " + resultA);

        int resultB = getResult("http://localhost:8073/algorithmB", product, restTemplate);
        System.out.println("resultB = " + resultB);

        int result = resultA + resultB;
        System.out.println("A+B = " + result);

        long end = new Date().getTime();
        System.out.println("Duration = " + ((end - start) / 1000) + " sec");

        product.setResult(result);
        productRepository.save(product);
    }

    private static int getResult(String baseUrl, Product product, RestTemplate restTemplate) throws Exception {
        return getResult(baseUrl, product, restTemplate, 0);
    }

    private static int getResult(String baseUrl, Product product, RestTemplate restTemplate, int errorCounter) throws Exception {
        if (errorCounter > 5) {
            throw new Exception("Retry > 5");
        }
        restTemplate.put(baseUrl, product);
        State state = null;
        do {
            try {
                Thread.sleep(500);
                ResponseEntity<State> stateEntity = restTemplate.getForEntity(baseUrl + "/state", State.class);
                state = stateEntity.getBody();
                System.out.println(baseUrl + " => State: " + state);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (state == State.RUNNING);
        if (state == State.FAILED) {
            System.out.println(baseUrl + " => Retry request");
            return getResult(baseUrl, product, restTemplate, ++errorCounter);
        }
        ResponseEntity<Integer> response = restTemplate.getForEntity(baseUrl + "/result", Integer.class);
        if (response.hasBody()) {
            Integer body = response.getBody();
            return body == null ? -1 : body;
        }
        return -1;
    }
}
