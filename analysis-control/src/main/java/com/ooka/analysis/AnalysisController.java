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
@RequestMapping("/analysiscontrol")
public class AnalysisController {
    final String urlA = "http://localhost:8070/algorithmA";
    final String urlB = "http://localhost:8071/algorithmB";
    final String urlThis = "http://localhost:8072/products";
    private State state = State.IDLE;
    private Integer result = null;

    @GetMapping(value = "/state", produces = "application/json")
    public ResponseEntity<State> getState() {
        return new ResponseEntity<>(state, HttpStatus.OK);
    }


    @PutMapping("/run")
    public void runController() {
        new Thread(this::run).start();
    }

    private void run() {
        if (state != State.RUNNING) {
            long start = new Date().getTime();

            state = State.RUNNING;

            RestTemplate restTemplate = new RestTemplate();
            Product product = getDummyProduct();
            restTemplate.postForObject(urlThis, product, Product.class);

            restTemplate.put(urlA, product);
            System.out.println("Starting AlgorithmA");
            restTemplate.put(urlB, product);
            System.out.println("Starting AlgorithmB");

            int resultA = -1, resultB = -1;
            State stateA = null, stateB = null;

            do {
                try {
                    Thread.sleep(500);
                    System.out.println("---------------------------------------------------------");
                    stateA = checkState(stateA, urlA, product, restTemplate);
                    System.out.println(urlA + " => State: " + stateA);
                    stateB = checkState(stateB, urlB, product, restTemplate);
                    System.out.println(urlB + " => State: " + stateB);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (stateA != State.SUCCEEDED || stateB != State.SUCCEEDED);

            resultA = fetchResult(restTemplate, urlA, resultA);
            resultB = fetchResult(restTemplate, urlB, resultB);
            result = resultA + resultB;

            System.out.println("---------------------------------------------------------");
            System.out.println("A+B = " + result);

            long end = new Date().getTime();
            System.out.println("Duration = " + ((end - start) / 1000) + " sec");

            product.setResult(result);
            restTemplate.postForObject(urlThis, product, Product.class);

            state = State.SUCCEEDED;
        } else {
            System.out.println(AnalysisControl.class.getName() + " is already running. Skip request.");
        }
    }

    private State checkState(State state, String baseUrl, Product product, RestTemplate restTemplate) {
        if (state == State.FAILED) {
            System.out.println("Restart " + baseUrl);
            restTemplate.put(baseUrl, product);
            ResponseEntity<State> stateBEntity = restTemplate.getForEntity(baseUrl + "/state", State.class);
            return stateBEntity.getBody();
        } else if (state != State.SUCCEEDED) {
            ResponseEntity<State> stateBEntity = restTemplate.getForEntity(baseUrl + "/state", State.class);
            return stateBEntity.getBody();
        }
        return state;
    }

    private int fetchResult(RestTemplate restTemplate, String baseUrlA, int resultA) {
        ResponseEntity<Integer> responseA = restTemplate.getForEntity(baseUrlA + "/result", Integer.class);
        if (responseA.hasBody()) {
            Integer body = responseA.getBody();
            resultA = body == null ? -1 : body;
        }
        return resultA;
    }

    private Product getDummyProduct() {
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
        return product;
    }

    @GetMapping(value = "result", produces = "application/json")
    public ResponseEntity<Integer> getResult() {
        HttpStatus status;
        if (state == State.SUCCEEDED) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.NO_CONTENT;
        }
        return new ResponseEntity<>(result, status);
    }
}
