package com.ooka.test.cities;

import com.ooka.test.cities.City;
import com.ooka.test.cities.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CityController {
    @Autowired
    CityRepository cityRepository;

    @GetMapping("/berlin")
    public City berlin() {
        return cityRepository.findByName("Berlin");
    }

    @GetMapping("/cologne")
    public City cologne() {
        return cityRepository.findByName("Cologne");
    }
}
