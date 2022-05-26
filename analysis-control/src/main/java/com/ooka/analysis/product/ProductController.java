package com.ooka.analysis.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @PostMapping("")
    public void createProduct(@RequestBody Product product) {
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

    @GetMapping("/getAll")
    public List<Product> readAllProducts() {
        Iterable<Product> product = productRepository.findAll();
        if (StreamSupport.stream(product.spliterator(), false).count() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Products found");
        }
        return (List<Product>) product;
    }

    @PutMapping("/{productId}")
    public void updateProduct(@PathVariable Long productId, @RequestBody Product productUpdate) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with " + productId + " not found");
        }
        Product productInstance = product.get();
        productInstance.setCoolingSystem(productUpdate.getCoolingSystem());
        productInstance.setAuxiliaryPTO(productUpdate.getAuxiliaryPTO());
        productInstance.setEngineManagementSystem(productUpdate.getEngineManagementSystem());
        productInstance.setExhaustSystem(productUpdate.getExhaustSystem());
        productInstance.setGearbox(productUpdate.getGearbox());
        productInstance.setFuelSystem(productUpdate.getFuelSystem());
        productInstance.setMonitoringSystem(productUpdate.getMonitoringSystem());
        productInstance.setMountingSystem(productUpdate.getMountingSystem());
        productInstance.setOilSystem(productUpdate.getOilSystem());
        productInstance.setPowerTransmission(productUpdate.getPowerTransmission());
        productInstance.setStartingSystem(productUpdate.getStartingSystem());
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
