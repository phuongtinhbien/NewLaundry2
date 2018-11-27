package com.example.vuphu.newlaundry.Main;

import java.io.Serializable;

public class OBService_Weight implements Serializable{
    private String idService;
    private String serviceName;
    private double weight;
    private double price;

    public  OBService_Weight() {

    }

    public OBService_Weight(String serviceName, double weight, String idService, double price) {
        this.serviceName = serviceName;
        this.weight = weight;
        this.idService = idService;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getIdService() {
        return idService;
    }

    public void setIdService(String idService) {
        this.idService = idService;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

}
