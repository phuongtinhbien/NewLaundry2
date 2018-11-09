package com.example.vuphu.newlaundry.Promotion;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class OBPromotion implements Serializable {
    private String id;
    private String title;
    private String img;
    private String code;
    private String timeStart;
    private String timeEnd;
    private String sale;

    public OBPromotion() {

    }

    public OBPromotion(String id, String title, String img, String code, String timeStart, String timeEnd, String sale) {
        this.id = id;
        this.title = title;
        this.img = img;
        this.code = code;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.sale = sale;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

}
