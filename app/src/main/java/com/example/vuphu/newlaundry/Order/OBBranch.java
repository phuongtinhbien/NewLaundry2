package com.example.vuphu.newlaundry.Order;

import java.io.Serializable;

public class OBBranch implements Serializable {
    private String id;
    private String branchName;
    private String branchAddress;
    private String latitude;
    private String longitude;
    private float distance;

    public OBBranch() {

    }

    public OBBranch(String id, String branchName, String latitude, String longitude, String branchAddress, float distance) {
        this.id = id;
        this.branchName = branchName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.branchAddress = branchAddress;
        this.distance = distance;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
