package com.ooka.increment.numbers;

import javax.persistence.*;

@Entity
@Table(name = "number")
public class Number {

    @Id
    @GeneratedValue
    private long id;

    private int value;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getValue(){
        return value;
    }

    public void setValue(int value){
        this.value = value;
    }
}
