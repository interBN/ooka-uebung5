package com.ooka.test.products;

import javax.persistence.*;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String name;
    private int costInEuro;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCostInEuro() {
        return costInEuro;
    }

    public void setCostInEuro(int costInEuro) {
        this.costInEuro = costInEuro;
    }
}
