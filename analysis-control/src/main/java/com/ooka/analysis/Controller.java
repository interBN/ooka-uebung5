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

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.ooka.*"})
@EntityScan("com.ooka.*")
public class Controller {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(com.ooka.analysis.Controller.class, args);
        System.out.println("Controller is running");
        ProductRepository productRepository = applicationContext.getBean(ProductRepository.class);
        Product product = new Product();
        product.setStartingSystem("testStartingSystem");
        productRepository.save(product);
        System.out.println("Starting AlgorithmA");
        RestTemplate restTemplate = new RestTemplate();

        int resultA = getResult("http://localhost:8071/algorithmA", product, restTemplate);
        System.out.println("resultA = " + resultA);
        int resultB = getResult("http://localhost:8073/algorithmB", product, restTemplate);
        System.out.println("resultB = " + resultB);
        System.out.println("A+B = " + (resultA + resultB));
    }

    private static int getResult(String baseUrl, Product product, RestTemplate restTemplate) {
        restTemplate.put(baseUrl, product);
        State state = null;
        do {
            try {
                Thread.sleep(1000);
                ResponseEntity<State> stateEntity = restTemplate.getForEntity(baseUrl + "/state", State.class);
                state = stateEntity.getBody();
                System.out.println(baseUrl + " => State: " + state);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (state == State.RUNNING);
        ResponseEntity<Integer> response = restTemplate.getForEntity(baseUrl + "/result", Integer.class);
        if (response.hasBody()) {
            return response.getBody();
        }
        return 0;
    }
}
