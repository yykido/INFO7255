package com.example.demoone.object;

import java.io.Serializable;

public class LinkedPlanService implements Serializable {
    private LinkedService linkedService;
    private PlanserviceCostShares planserviceCostShares;

    private String _org;
    private String objectId;
    private String objectType;

    public LinkedService getLinkedService() {
        return linkedService;
    }

    public void setLinkedService(LinkedService linkedService) {
        this.linkedService = linkedService;
    }

    public PlanserviceCostShares getPlanserviceCostShares() {
        return planserviceCostShares;
    }

    public void setPlanserviceCostShares(PlanserviceCostShares planserviceCostShares) {
        this.planserviceCostShares = planserviceCostShares;
    }

    public String get_org() {
        return _org;
    }

    public void set_org(String _org) {
        this._org = _org;
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
