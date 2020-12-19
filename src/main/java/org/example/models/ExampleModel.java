package org.example.models;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class ExampleModel {
    private Long id;
    private String name;

    private List<ExampleDetailModel> exampleDetailModels;

    public ExampleModel() {}

    public ExampleModel(String name) {
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

    public List<ExampleDetailModel> getExampleDetailModels() {
        return exampleDetailModels;
    }

    public void setExampleDetailModels(List<ExampleDetailModel> exampleDetailModels) {
        this.exampleDetailModels = exampleDetailModels;
    }

}
