package com.ooka.test;

import com.ooka.increment.numbers.NumberEntity;
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
public class TestApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(TestApplication.class, args);
//
//        City berlin = new City();
//        berlin.setName("Berlin");
//        berlin.setCapital(true);
//
//        City cologne = new City();
//        cologne.setName("Cologne");
//        cologne.setCapital(false);
//
//        CityRepository cityRepository = applicationContext.getBean(CityRepository.class);
//        cityRepository.save(berlin);
//        cityRepository.save(cologne);
        while (applicationContext.isActive()) {
            try {
                Thread.sleep(1000);
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<NumberEntity> numberEntity = restTemplate.getForEntity("http://localhost:8090/numbers/1", NumberEntity.class);
                if (numberEntity.hasBody()) {
                    NumberEntity body = numberEntity.getBody();
                    System.out.println(body.getValue());
                }
            } catch (Exception e) {
                break;
            }

        }
    }
}
