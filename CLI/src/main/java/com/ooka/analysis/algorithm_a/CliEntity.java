package com.ooka.analysis.algorithm_a;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "cli")
public class CliEntity {

    @Id
    @GeneratedValue
    private long id;

    @CreatedDate
    private Date created = new Date();

    @LastModifiedDate
    private Date updated = new Date();

    private String log;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    @PreUpdate
    public void setLastUpdate() {
        this.updated = new Date();
    }
}
