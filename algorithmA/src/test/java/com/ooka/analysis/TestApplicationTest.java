package com.ooka.analysis;

import com.ooka.analysis.algorithm_a.AlgorithmA;
import com.ooka.analysis.algorithm_a.AlgorithmARepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestApplicationTest {

//    @Autowired
//    private AlgorithmARepository numberRepository;
//
//    @Before
//    public void setUp() {
//        AlgorithmA number = new AlgorithmA();
//        number.setValue(200);
//        numberRepository.save(number);
//    }
//
//    @After
//    public void tearDown() {
//        numberRepository.deleteAll();
//    }
//
//    @Test
//    public void testGetProduct() {
//        TestRestTemplate testRestTemplate = new TestRestTemplate();
//        ResponseEntity<AlgorithmA> productEntity = testRestTemplate.getForEntity("http://localhost:8090/numbers/1", AlgorithmA.class);
//        Assert.assertNotNull(productEntity.getBody());
//        Assert.assertEquals(HttpStatus.OK, productEntity.getStatusCode());
//        Assert.assertEquals(productEntity.getBody().getValue(), 200);
//    }
}