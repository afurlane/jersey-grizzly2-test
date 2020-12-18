package org.example.entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ExampleDetailEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @OneToMany(mappedBy="ExampleDetailEntity",cascade= CascadeType.PERSIST)
    private List<ExampleEntity> exampleEntities = new ArrayList<>();

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
    // TODO: Use model mapper to map entities to REST API models so you don't need xmltransient
    @XmlTransient
    public List<ExampleEntity> getExampleEntity() {
        return exampleEntities;
    }
    @XmlTransient
    public void setExampleEntity(List<ExampleEntity> exampleEntities) {
        this.exampleEntities = exampleEntities;
    }
}
