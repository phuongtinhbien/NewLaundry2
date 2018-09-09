package com.example.vuphu.newlaundry.Order;

import com.example.vuphu.newlaundry.Product.OBProduct;

import java.io.Serializable;

import me.aflak.filter_annotation.Filterable;

@Filterable
public class OBOrderDetail extends OBProduct implements Serializable {

    private String production;
    private String material;
    private String unit;
    private long   count;
    private boolean   selectedIndx; //default -1...not selected

    public String getProduction() {
        return production;
    }

    public void setProduction(String production) {
        this.production = production;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }


    public boolean isSelectedIndx() {
        return selectedIndx;
    }

    public void setSelectedIndx(boolean selectedIndx) {
        this.selectedIndx = selectedIndx;
    }
}
