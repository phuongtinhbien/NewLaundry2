package com.example.vuphu.newlaundry.Product;

import java.io.Serializable;

import me.aflak.filter_annotation.Filterable;

@Filterable
public class OBProduct implements Serializable {

    private String id;
    private String avatar;
    private String title;
    private String category;

    public OBProduct(String id ,String avatar, String title, String category) {
        this.id = id;
        this.avatar = avatar;
        this.title = title;
        this.category = category;
    }

    public OBProduct() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
