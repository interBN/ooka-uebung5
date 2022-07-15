package com.ooka.analysis;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.ooka.analysis.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
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
    final String nameA = "power-systems";
    final String nameB = "liquid-gas-systems";
    final String nameC = "management-systems";
    final String nameThis = "analysis-control";
    private State state = State.IDLE;
    private Integer result = null;


    @Autowired
    @Lazy
    private EurekaClient eurekaClient;
    @Value("${spring.application.name}")
    private String appName;

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

            InstanceInfo serviceThis = eurekaClient.getApplication(nameThis).getInstances().get(0);
            InstanceInfo serviceA = eurekaClient.getApplication(nameA).getInstances().get(0);
            InstanceInfo serviceB = eurekaClient.getApplication(nameB).getInstances().get(0);
            InstanceInfo serviceC = eurekaClient.getApplication(nameC).getInstances().get(0);

            String urlThis = serviceThis.getHomePageUrl() + "/products";
            String urlA = serviceA.getHomePageUrl() + "/powerSystems";
            String urlB = serviceB.getHomePageUrl() + "/liquidGas";
            String urlC = serviceC.getHomePageUrl() + "/management";

            RestTemplate restTemplate = new RestTemplate();
            Product product = getDummyProduct();
            restTemplate.postForObject(urlThis, product, Product.class);

            restTemplate.put(urlA, product);
            System.out.println("Starting Power Systems");
            restTemplate.put(urlB, product);
            System.out.println("Starting Liquid and Gas Systems");
            restTemplate.put(urlC, product);
            System.out.println("Starting Management Systems");

            int resultA = -1, resultB = -1, resultC = -1;
            State stateA = null, stateB = null, stateC = null;

            do {
                try {
                    Thread.sleep(500);
                    System.out.println("---------------------------------------------------------");
                    stateA = checkState(stateA, urlA, product, restTemplate);
                    System.out.println(urlA + " => State: " + stateA);
                    stateB = checkState(stateB, urlB, product, restTemplate);
                    System.out.println(urlB + " => State: " + stateB);
                    stateC = checkState(stateC, urlC, product, restTemplate);
                    System.out.println(urlC + " => State: " + stateC);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (stateA != State.SUCCEEDED || stateB != State.SUCCEEDED || stateC != State.SUCCEEDED);

            resultA = fetchResult(restTemplate, urlA, resultA);
            resultB = fetchResult(restTemplate, urlB, resultB);
            resultC = fetchResult(restTemplate, urlC, resultC);
            result = resultA + resultB + resultC;

            System.out.println("---------------------------------------------------------");
            System.out.println("Power Systems: " + resultA);
            System.out.println("Liquid/Gas Systems: " + resultB);
            System.out.println("Management Systems: " + resultC);
            System.out.println("Total result: " + result);

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
