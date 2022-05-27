package com.ooka.cli.log;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CliRepository extends CrudRepository<CliEntity, Long> {
}
