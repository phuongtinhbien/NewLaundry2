package com.example.vuphu.newlaundry.Main;

import java.io.Serializable;

public class OBBill implements Serializable {
    private String idService;
    private String servicename;
    private String nameClothes;
    private String unitName;
    private String unitId;
    private double unitprice;
    private double weight;
    private double weightReceived;
    private long amount;
    private long amountReceived;

    public OBBill() {
        this.unitprice = 0;
        this.weight = 0;
        this.weightReceived = 0;
        this.amount = 0;
        this.amountReceived = 0;
    }

    public OBBill(String idService, String servicename, String nameClothes, String unitName, String unitId, double unitprice, double weight, double weightReceived, long amount, long amountReceived) {
        this.idService = idService;
        this.servicename = servicename;
        this.nameClothes = nameClothes;
        this.unitName = unitName;
        this.unitId = unitId;
        this.unitprice = unitprice;
        this.weight = weight;
        this.weightReceived = weightReceived;
        this.amount = amount;
        this.amountReceived = amountReceived;
    }

    public String getIdService() {
        return idService;
    }

    public void setIdService(String idService) {
        this.idService = idService;
    }

    public String getServicename() {
        return servicename;
    }

    public void setServicename(String servicename) {
        this.servicename = servicename;
    }

    public String getNameClothes() {
        return nameClothes;
    }

    public void setNameClothes(String nameClothes) {
        this.nameClothes = nameClothes;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public double getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(double unitprice) {
        this.unitprice = unitprice;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getWeightReceived() {
        return weightReceived;
    }

    public void setWeightReceived(double weightReceived) {
        this.weightReceived = weightReceived;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(long amountReceived) {
        this.amountReceived = amountReceived;
    }

    @Override
    public String toString() {
        return "OBBill{" +
                "idService='" + idService + '\'' +
                ", servicename='" + servicename + '\'' +
                ", nameClothes='" + nameClothes + '\'' +
                ", unitName='" + unitName + '\'' +
                ", unitId='" + unitId + '\'' +
                ", unitprice=" + unitprice +
                ", weight=" + weight +
                ", weightReceived=" + weightReceived +
                ", amount=" + amount +
                ", amountReceived=" + amountReceived +
                '}';
    }
}
