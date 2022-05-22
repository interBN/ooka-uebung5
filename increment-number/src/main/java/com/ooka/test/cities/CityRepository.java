package com.ooka.test.cities;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends CrudRepository<City, Long> {

    // SELECT 1 FROM cities WHERE name = x
    City findByName(String name);
}
