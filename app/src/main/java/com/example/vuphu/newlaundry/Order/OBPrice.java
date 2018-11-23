package com.example.vuphu.newlaundry.Order;

import java.io.Serializable;

public class OBPrice implements Serializable {
    private String id;
    private double unitPrice;

    public OBPrice() {

    }
    public OBPrice(String id, double unitPrice) {
        this.id = id;
        this.unitPrice = unitPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
