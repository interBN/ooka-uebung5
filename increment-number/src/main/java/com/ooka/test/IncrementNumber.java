package com.ooka.test;

import com.ooka.test.numbers.Number;
import com.ooka.test.numbers.NumberRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.ooka.*"})
@EntityScan("com.ooka.*")
public class IncrementNumber {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(IncrementNumber.class, args);
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
        NumberRepository numberRepository = applicationContext.getBean(NumberRepository.class);
        Number counter = new Number();
        counter.setValue(0);
        numberRepository.save(counter);
        long id = counter.getId();
        while(applicationContext.isActive()){
            try {
                Thread.sleep(1000);
                if(numberRepository.existsById(id)) {
                    counter = numberRepository.findById(id).get();
                    counter.setValue(counter.getValue() + 1);
                    numberRepository.save(counter);
                    System.out.println(counter.getValue());
                }
                else{
                    System.out.println("No number present");
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
