package com.ooka.test.products;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    @PostMapping("")
    public void creteProduct(@RequestBody Product product) {
        productRepository.save(product);
    }

    @GetMapping("/{productId}")
    public Product readProduct(@PathVariable Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            return product.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with " + productId + " not found");
    }

    @PutMapping("/{productId}")
    public void updateProduct(@PathVariable Long productId, @RequestBody Product productUpdate) {
        Optional<Product> product = productRepository.findById(productId);
        if (!product.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with " + productId + " not found");
        }
        Product productInstance = product.get();
        productInstance.setName(productUpdate.getName());
        productInstance.setCostInEuro(productUpdate.getCostInEuro());
        productRepository.save(productInstance);
    }

    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            productRepository.delete(product.get());
            return;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with " + productId + " not found");
    }
}
