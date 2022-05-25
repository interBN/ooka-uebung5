package com.ooka.increment.numbers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/numbers")
public class NumberController {

    @Autowired
    NumberRepository numberRepository;

    @PostMapping("")
    public void createNumber(@RequestBody NumberEntity number) {
        numberRepository.save(number);
    }

    @GetMapping("/{numberId}")
    public NumberEntity readNumber(@PathVariable Long numberId) {
        Optional<NumberEntity> number = numberRepository.findById(numberId);
        if (number.isPresent()) {
            return number.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Number with " + numberId + " not found");
    }

    @GetMapping("/getAll")
    public List<NumberEntity> readAllNumbers() {
        Iterable<NumberEntity> number = numberRepository.findAll();
        if (StreamSupport.stream(number.spliterator(), false).count() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Numbers found");
        }
        return (List<NumberEntity>) number;
    }

    @PutMapping("/{numberId}")
    public void updateNumber(@PathVariable Long numberId, @RequestBody NumberEntity numberUpdate) {
        Optional<NumberEntity> number = numberRepository.findById(numberId);
        if (number.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Number with " + numberId + " not found");
        }
        NumberEntity numberInstance = number.get();
        numberInstance.setValue(numberUpdate.getValue());
        numberRepository.save(numberInstance);
    }

    @DeleteMapping("/{numberId}")
    public void deleteProduct(@PathVariable Long numberId) {
        Optional<NumberEntity> number = numberRepository.findById(numberId);
        if (number.isPresent()) {
            numberRepository.delete(number.get());
            return;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Number with " + numberId + " not found");
    }
}
