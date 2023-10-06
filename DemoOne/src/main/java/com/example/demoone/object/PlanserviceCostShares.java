package com.example.demoone.object;

import java.io.Serializable;

public class PlanserviceCostShares implements Serializable {
    private int deductible;
    private String org;
    private int copay;
    private String objectId;
    private String objectType;

    public int getDeductible() {
        return deductible;
    }

    public void setDeductible(int deductible) {
        this.deductible = deductible;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public int getCopay() {
        return copay;
    }

    public void setCopay(int copay) {
        this.copay = copay;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }
}
