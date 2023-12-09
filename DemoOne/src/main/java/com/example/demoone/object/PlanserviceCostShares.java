package com.example.demoone.object;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
@Getter
@Setter
public class PlanserviceCostShares{
    @NotNull
    private int deductible;
    @NotNull
    private String _org;
    @NotNull
    private int copay;
    @NotNull
    private String objectId;
    @NotNull
    private String objectType;

//    public int getDeductible() {
//        return deductible;
//    }
//
//    public void setDeductible(int deductible) {
//        this.deductible = deductible;
//    }
//
//    public String getOrg() {
//        return org;
//    }
//
//    public void setOrg(String org) {
//        this.org = org;
//    }
//
//    public int getCopay() {
//        return copay;
//    }
//
//    public void setCopay(int copay) {
//        this.copay = copay;
//    }
//
//    public String getObjectId() {
//        return objectId;
//    }
//
//    public void setObjectId(String objectId) {
//        this.objectId = objectId;
//    }
//
//    public String getObjectType() {
//        return objectType;
//    }
//
//    public void setObjectType(String objectType) {
//        this.objectType = objectType;
//    }
}
