package org.example.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ExampleDetailEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @OneToMany(mappedBy="ExampleDetailEntity",cascade= CascadeType.PERSIST)
    private List<ExampleEntity> employees = new ArrayList<>();

    public ExampleDetailEntity() {
        super();
    }
    public ExampleDetailEntity(String name) {
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
    public List<ExampleEntity> getExampleEntity() {
        return employees;
    }
    public void setExampleEntity(List<ExampleEntity> employees) {
        this.employees = employees;
    }
}
