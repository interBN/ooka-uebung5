package com.ooka.increment;

import com.ooka.increment.numbers.Number;
import com.ooka.increment.numbers.NumberRepository;
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

    @Autowired
    private NumberRepository numberRepository;

    @Before
    public void setUp() {
        Number number = new Number();
        number.setValue(200);
        numberRepository.save(number);
    }

    @After
    public void tearDown() {
        numberRepository.deleteAll();
    }

    @Test
    public void testGetProduct() {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        ResponseEntity<Number> productEntity = testRestTemplate.getForEntity("http://localhost:8090/numbers/1", Number.class);
        Assert.assertNotNull(productEntity.getBody());
        Assert.assertEquals(HttpStatus.OK, productEntity.getStatusCode());
        Assert.assertEquals(productEntity.getBody().getValue(), 200);
    }
}