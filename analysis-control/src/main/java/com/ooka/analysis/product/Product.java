package com.ooka.analysis.product;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

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
    private int result;
    private int hash;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
        hashCode();
    }

    public String getStartingSystem() {
        return startingSystem;
    }

    public void setStartingSystem(String startingSystem) {
        this.startingSystem = startingSystem;
        hashCode();
    }

    public String getAuxiliaryPTO() {
        return auxiliaryPTO;
    }

    public void setAuxiliaryPTO(String auxiliaryPTO) {
        this.auxiliaryPTO = auxiliaryPTO;
        hashCode();
    }

    public String getOilSystem() {
        return oilSystem;
    }

    public void setOilSystem(String oilSystem) {
        this.oilSystem = oilSystem;
        hashCode();
    }

    public String getFuelSystem() {
        return fuelSystem;
    }

    public void setFuelSystem(String fuelSystem) {
        this.fuelSystem = fuelSystem;
        hashCode();
    }

    public String getCoolingSystem() {
        return coolingSystem;
    }

    public void setCoolingSystem(String coolingSystem) {
        this.coolingSystem = coolingSystem;
        hashCode();
    }

    public String getExhaustSystem() {
        return exhaustSystem;
    }

    public void setExhaustSystem(String exhaustSystem) {
        this.exhaustSystem = exhaustSystem;
        hashCode();
    }

    public String getMountingSystem() {
        return mountingSystem;
    }

    public void setMountingSystem(String mountingSystem) {
        this.mountingSystem = mountingSystem;
        hashCode();
    }

    public String getEngineManagementSystem() {
        return engineManagementSystem;
    }

    public void setEngineManagementSystem(String engineManagementSystem) {
        this.engineManagementSystem = engineManagementSystem;
        hashCode();
    }

    public String getMonitoringSystem() {
        return monitoringSystem;
    }

    public void setMonitoringSystem(String monitoringSystem) {
        this.monitoringSystem = monitoringSystem;
        hashCode();
    }

    public String getPowerTransmission() {
        return powerTransmission;
    }

    public void setPowerTransmission(String powerTransmission) {
        this.powerTransmission = powerTransmission;
        hashCode();
    }

    public String getGearbox() {
        return gearbox;
    }

    public void setGearbox(String gearbox) {
        this.gearbox = gearbox;
        hashCode();
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
        hashCode();
    }

    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", startingSystem='" + startingSystem + '\'' + ", auxiliaryPTO='" + auxiliaryPTO + '\'' + ", oilSystem='" + oilSystem + '\'' + ", fuelSystem='" + fuelSystem + '\'' + ", coolingSystem='" + coolingSystem + '\'' + ", exhaustSystem='" + exhaustSystem + '\'' + ", mountingSystem='" + mountingSystem + '\'' + ", engineManagementSystem='" + engineManagementSystem + '\'' + ", monitoringSystem='" + monitoringSystem + '\'' + ", powerTransmission='" + powerTransmission + '\'' + ", gearbox='" + gearbox + '\'' + ", result=" + result + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id && result == product.result && Objects.equals(startingSystem, product.startingSystem) && Objects.equals(auxiliaryPTO, product.auxiliaryPTO) && Objects.equals(oilSystem, product.oilSystem) && Objects.equals(fuelSystem, product.fuelSystem) && Objects.equals(coolingSystem, product.coolingSystem) && Objects.equals(exhaustSystem, product.exhaustSystem) && Objects.equals(mountingSystem, product.mountingSystem) && Objects.equals(engineManagementSystem, product.engineManagementSystem) && Objects.equals(monitoringSystem, product.monitoringSystem) && Objects.equals(powerTransmission, product.powerTransmission) && Objects.equals(gearbox, product.gearbox);
    }

    @Override
    public int hashCode() {
        hash = Objects.hash(startingSystem, auxiliaryPTO, oilSystem, fuelSystem, coolingSystem, exhaustSystem, mountingSystem, engineManagementSystem, monitoringSystem, powerTransmission, gearbox);
        return hash;
    }
}
