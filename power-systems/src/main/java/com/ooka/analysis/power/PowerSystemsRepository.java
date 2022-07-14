package com.ooka.analysis.power;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PowerSystemsRepository extends CrudRepository<PowerSystemsEntity, Long> {
}
