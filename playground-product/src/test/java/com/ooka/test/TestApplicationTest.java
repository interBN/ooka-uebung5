package com.ooka.test;

import com.ooka.test.products.Product;
import com.ooka.test.products.ProductRepository;
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

    private static final String productName = "Test Produkt";
    @Autowired
    private ProductRepository productRepository;

    @Before
    public void setUp() {
        Product product = new Product();
        product.setName("Test Produkt");
        product.setCostInEuro(200);
        productRepository.save(product);
    }

    @After
    public void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    public void testGetProduct() {
        TestRestTemplate testRestTemplate = new TestRestTemplate();
        ResponseEntity<Product> productEntity = testRestTemplate.getForEntity("http://localhost:8080/products/1", Product.class);
        Assert.assertNotNull(productEntity.getBody());
        Assert.assertEquals(HttpStatus.OK, productEntity.getStatusCode());
        Assert.assertEquals(productEntity.getBody().getName(), productName);
    }
}