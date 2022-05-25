package com.ooka.test;

import com.ooka.increment.numbers.NumberEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

        int counter = 0;

        while (applicationContext.isActive()) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            RestTemplate restTemplate = new RestTemplate();

            if (counter == 0) {
                /*
                  POST random number
                 */
                int i = new Random().nextInt();
                NumberEntity number = new NumberEntity();
                number.setValue(i);
                restTemplate.postForObject("http://localhost:8090/numbers", number, NumberEntity.class);
                System.out.println("POST => value: " + number.getValue());
            } else if (counter == 1) {
                long id = getLastId(restTemplate);
                ResponseEntity<NumberEntity> numberEntity = restTemplate.getForEntity("http://localhost:8090/numbers/" + id, NumberEntity.class);
                if (numberEntity.hasBody()) {
                    /*
                    GET last entry and change value
                     */
                    NumberEntity entity = numberEntity.getBody();
                    assert entity != null;
                    entity.setValue(-1);
                    /*
                     PUT
                    */
                    Map<String, String> params = new HashMap<>();
                    params.put("id", String.valueOf(id));
                    restTemplate.put("http://localhost:8090/numbers/" + id, entity, params);
                    System.out.println("PUT => id: " + id + ", value: " + entity.getValue());
                }
            } else if (counter == 2) {
                long id = getLastId(restTemplate);
               /*
                 DELETE
                */
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                restTemplate.delete("http://localhost:8090/numbers/" + id, params);
                System.out.println("DELETE => id: " + id);
            }

            /*
              GET and print id 0
             */
            ResponseEntity<NumberEntity> numberEntity = restTemplate.getForEntity("http://localhost:8090/numbers/1", NumberEntity.class);
            if (numberEntity.hasBody()) {
                NumberEntity body = numberEntity.getBody();
                System.out.println(body.getValue());
            }

            counter++;
            counter %= 3;
        }
    }

    private static long getLastId(RestTemplate restTemplate) {
        ResponseEntity<NumberEntity[]> allEntries = restTemplate.getForEntity("http://localhost:8090/numbers/getAll", NumberEntity[].class);
        long id = -1L;
        if (allEntries.hasBody()) {
            NumberEntity[] body = allEntries.getBody();
            assert body != null;
            Long max = 0L;
            for (NumberEntity e : body) {
                if (e.getId() > max) {
                    max = e.getId();
                }
            }
            id = max;
        }
        return id;
    }
}
