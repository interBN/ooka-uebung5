package com.ooka.analysis;

import com.ooka.analysis.product.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@RestController
@RequestMapping("")
public class AnalysisController {
    private final State state = State.IDLE;

    @GetMapping(value = "/state", produces = "application/json")
    public ResponseEntity<State> getState() {
        return new ResponseEntity<>(state, HttpStatus.OK);
    }


    @PutMapping("/run")
    public void runController() {
        new Thread(this::run).start();
    }

    private void run() {

        long start = new Date().getTime();
        RestTemplate restTemplate = new RestTemplate();

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
        restTemplate.postForObject("http://localhost:8072/products", product, Product.class);

        System.out.println("Starting AlgorithmA");
        int resultA = -1;
        try {
            resultA = getResult("http://localhost:8070/algorithmA", product, restTemplate);
            System.out.println("resultA = " + resultA);
        } catch (Exception e) {
            System.out.println("Error while performing AlgorithmA");
        }

        System.out.println("Starting AlgorithmB");
        int resultB = -1;
        try {
            resultB = getResult("http://localhost:8071/algorithmB", product, restTemplate);
            System.out.println("resultB = " + resultB);
        } catch (Exception e) {
            System.out.println("Error while performing AlgorithmB");
        }

        int result = resultA + resultB;
        System.out.println("A+B = " + result);

        long end = new Date().getTime();
        System.out.println("Duration = " + ((end - start) / 1000) + " sec");

        product.setResult(result);
        restTemplate.postForObject("http://localhost:8072/products", product, Product.class);
    }

    private int getResult(String baseUrl, Product product, RestTemplate restTemplate) throws Exception {
        return getResult(baseUrl, product, restTemplate, 0);
    }

    private int getResult(String baseUrl, Product product, RestTemplate restTemplate, int errorCounter) throws Exception {
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
