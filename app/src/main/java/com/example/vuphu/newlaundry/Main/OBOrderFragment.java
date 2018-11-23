package com.example.vuphu.newlaundry.Main;

import java.io.Serializable;

public class OBOrderFragment implements Serializable {
    private String id;
    private String date;
    private String status;
    private String branchAddress;
    private String reciever;
    private String branchName;

    public OBOrderFragment() {

    }

    public OBOrderFragment(String id, String date, String status, String branchAddress, String reciever, String branchName) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.branchAddress = branchAddress;
        this.reciever = reciever;
        this.branchName = branchName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }
}
