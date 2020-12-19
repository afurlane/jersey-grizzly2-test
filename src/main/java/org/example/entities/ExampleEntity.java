package org.example.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "ExampleEntity")
@Table(name = "exampleentity")
public class ExampleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    // READ:
    // https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/
    // so mappedBy is the "navigation property" in linked entity
    @OneToMany(mappedBy = "exampleEntity", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<ExampleDetailEntity> exampleDetailEntities = new ArrayList<>();

    public ExampleEntity() {}

    public ExampleEntity(String name, List<ExampleDetailEntity> exampleDetailEntities) {
        this.name = name;
        this.exampleDetailEntities = exampleDetailEntities;
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

    public List<ExampleDetailEntity> getExampleDetailEntity() {
        return exampleDetailEntities;
    }

    public void setExampleDetailEntity(List<ExampleDetailEntity> exampleDetailEntities) {
        this.exampleDetailEntities = exampleDetailEntities;
    }
}
