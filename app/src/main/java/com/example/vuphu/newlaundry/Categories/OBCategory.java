package com.example.vuphu.newlaundry.Categories;

import java.io.Serializable;

public class OBCategory implements Serializable{

    private String id;
    private String name;

    public OBCategory(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public OBCategory() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
