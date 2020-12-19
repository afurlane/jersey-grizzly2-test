package org.example.models;

public class ExampleDetailModel {

    private Long id;
    private String name;

    public ExampleDetailModel() { }
    public ExampleDetailModel(String name) {
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
}
