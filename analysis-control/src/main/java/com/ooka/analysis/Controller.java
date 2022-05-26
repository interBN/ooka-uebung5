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
//        restTemplate.postForObject("http://localhost:8071/algorithmA", product, Product.class);
        restTemplate.put("http://localhost:8071/algorithmA", product);
        State stateA = null;
        do {
            try {
                Thread.sleep(1000);
                System.out.print("State: ");
                ResponseEntity<State> stateEntity = restTemplate.getForEntity("http://localhost:8071/algorithmA/state", State.class);
                stateA = stateEntity.getBody();
                System.out.println("AlgorithmA is " + stateA);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (stateA == State.RUNNING);
    }
}
