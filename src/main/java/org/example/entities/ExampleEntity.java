package org.example.entities;

import org.eclipse.persistence.internal.helper.ClassConstants;

import javax.persistence.*;
import java.time.Month;
import java.util.UUID;

@Entity
public class ExampleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private ExampleDetailEntity exampleDetailEntity;

    public ExampleEntity() {}

    public ExampleEntity(String name, ExampleDetailEntity exampleDetailEntity) {
        this.name = name;
        this.exampleDetailEntity = exampleDetailEntity;
    }

    public ExampleEntity(String name) {
        this.name = name;
    }

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

    public ExampleDetailEntity getExampleDetailEntity() {
        return exampleDetailEntity;
    }

    public void setExampleDetailEntity(ExampleDetailEntity exampleDetailEntity) {
        this.exampleDetailEntity = exampleDetailEntity;
    }

    @Override
    public String toString() {
        return "Example [id=" + id + ", name=" + name + ", exampleDetailEntity="
                + exampleDetailEntity.getName() + "]";
    }
}
