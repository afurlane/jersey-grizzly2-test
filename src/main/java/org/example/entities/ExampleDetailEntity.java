package org.example.entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "ExampleDetailEntity")
@Table(name = "exampledetailentity")
public class ExampleDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private ExampleEntity exampleEntity;

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

    public ExampleEntity getExampleEntity() {
        return exampleEntity;
    }
    public void setExampleEntity(ExampleEntity exampleEntity) {
        this.exampleEntity = exampleEntity;
    }
}
