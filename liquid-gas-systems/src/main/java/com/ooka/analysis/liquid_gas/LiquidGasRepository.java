package com.ooka.analysis.liquid_gas;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LiquidGasRepository extends CrudRepository<LiquidGasEntity, Long> {
}
