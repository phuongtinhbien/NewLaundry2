package com.example.vuphu.newlaundry.Order;

import com.example.vuphu.newlaundry.Product.OBProduct;

import java.io.Serializable;

import me.aflak.filter_annotation.Filterable;

@Filterable
public class OBOrderDetail implements Serializable {

    private OBProduct product;
    private String label;
    private String material;
    private String unit;
    private String labelID;
    private String materialID;
    private String unitID;
    private String color;
    private String colorID;
    private long   count;
    private String idService;
    private String note;
    private String serviceName;

    public OBOrderDetail() {
    }

    public OBOrderDetail(OBProduct product, String label, String material, String unit, String labelID, String materialID, String unitID, String color, String colorID, long count, String idService, String note, String serviceName) {
        this.product = product;
        this.label = label;
        this.material = material;
        this.unit = unit;
        this.labelID = labelID;
        this.materialID = materialID;
        this.unitID = unitID;
        this.color = color;
        this.colorID = colorID;
        this.count = count;
        this.idService = idService;
        this.note = note;
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getIdService() {
        return idService;
    }

    public void setIdService(String idService) {
        this.idService = idService;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColorID() {
        return colorID;
    }

    public void setColorID(String colorID) {
        this.colorID = colorID;
    }

    public String getLabelID() {
        return labelID;
    }

    public void setLabelID(String labelID) {
        this.labelID = labelID;
    }

    public String getMaterialID() {
        return materialID;
    }

    public void setMaterialID(String materialID) {
        this.materialID = materialID;
    }

    public String getUnitID() {
        return unitID;
    }

    public void setUnitID(String unitID) {
        this.unitID = unitID;
    }

    public OBProduct getProduct() {
        return product;
    }

    public void setProduct(OBProduct product) {
        this.product = product;
    }

    public String getLabel() {
        return label;
    }

    public String getMaterial() {
        return material;
    }

    public String getUnit() {
        return unit;
    }

    public long getCount() {
        return count;
    }


    public void setLabel(String label) {
        this.label = label;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setCount(long count) {
        this.count = count;
    }

}
