package com.ooka.analysis.product;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue
    private long id;

    private String startingSystem;
    private String auxiliaryPTO;
    private String oilSystem;
    private String fuelSystem;
    private String coolingSystem;
    private String exhaustSystem;
    private String mountingSystem;
    private String engineManagementSystem;
    private String monitoringSystem;
    private String powerTransmission;
    private String gearbox;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartingSystem() {
        return startingSystem;
    }

    public void setStartingSystem(String startingSystem) {
        this.startingSystem = startingSystem;
    }

    public String getAuxiliaryPTO() {
        return auxiliaryPTO;
    }

    public void setAuxiliaryPTO(String auxiliaryPTO) {
        this.auxiliaryPTO = auxiliaryPTO;
    }

    public String getOilSystem() {
        return oilSystem;
    }

    public void setOilSystem(String oilSystem) {
        this.oilSystem = oilSystem;
    }

    public String getFuelSystem() {
        return fuelSystem;
    }

    public void setFuelSystem(String fuelSystem) {
        this.fuelSystem = fuelSystem;
    }

    public String getCoolingSystem() {
        return coolingSystem;
    }

    public void setCoolingSystem(String coolingSystem) {
        this.coolingSystem = coolingSystem;
    }

    public String getExhaustSystem() {
        return exhaustSystem;
    }

    public void setExhaustSystem(String exhaustSystem) {
        this.exhaustSystem = exhaustSystem;
    }

    public String getMountingSystem() {
        return mountingSystem;
    }

    public void setMountingSystem(String mountingSystem) {
        this.mountingSystem = mountingSystem;
    }

    public String getEngineManagementSystem() {
        return engineManagementSystem;
    }

    public void setEngineManagementSystem(String engineManagementSystem) {
        this.engineManagementSystem = engineManagementSystem;
    }

    public String getMonitoringSystem() {
        return monitoringSystem;
    }

    public void setMonitoringSystem(String monitoringSystem) {
        this.monitoringSystem = monitoringSystem;
    }

    public String getPowerTransmission() {
        return powerTransmission;
    }

    public void setPowerTransmission(String powerTransmission) {
        this.powerTransmission = powerTransmission;
    }

    public String getGearbox() {
        return gearbox;
    }

    public void setGearbox(String gearbox) {
        this.gearbox = gearbox;
    }
}
