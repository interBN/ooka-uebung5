package com.ooka.analysis.algorithm_a;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CliRepository extends CrudRepository<CliEntity, Long> {
}
