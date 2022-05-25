package com.ooka.increment.numbers;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NumberRepository extends CrudRepository<NumberEntity, Long> {
}
