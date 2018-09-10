package com.example.vuphu.newlaundry.Payment;

import java.io.Serializable;

import me.aflak.filter_annotation.Filterable;

@Filterable
public class OBPayment implements Serializable {

    private long id;
    private String name;
    private String desc;

    public String getSeletedInd() {
        return seletedInd;
    }

    public void setSeletedInd(String seletedInd) {
        this.seletedInd = seletedInd;
    }

    private String seletedInd;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
