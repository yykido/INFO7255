package com.example.demoone.object;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.*;

@Getter
@Setter
public class Plan{
    @Valid
    @NotNull
    private PlanCostShares planCostShares;
    @Valid
    @NotNull
    private List<LinkedPlanService> linkedPlanServices;
    private String _org;
    @NotEmpty
    private String objectId;
    @NotNull
    private String objectType;
    @NotNull
    private String planType;
    @NotNull
    private String creationDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plan plan = (Plan) o;
        return Objects.equals(planCostShares, plan.planCostShares) &&
                Objects.equals(linkedPlanServices, plan.linkedPlanServices) &&
                Objects.equals(_org, plan._org) &&
                Objects.equals(objectId, plan.objectId) &&
                Objects.equals(objectType, plan.objectType) &&
                Objects.equals(planType, plan.planType) &&
                Objects.equals(creationDate, plan.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(planCostShares, linkedPlanServices, _org, objectId, objectType, planType, creationDate);
    }

    //    private Plan plan;
//
//    public Plan getPlan() {
//        return plan;
//    }
//
//    public void setPlan(Plan plan) {
//        this.plan = plan;
//    }

//    public PlanCostShares getPlanCostShares() {
//        return planCostShares;
//    }
//
//    public void setPlanCostShares(PlanCostShares planCostShares) {
//        this.planCostShares = planCostShares;
//    }
//
//    public List<LinkedPlanService> getLinkedPlanServices() {
//        return linkedPlanServices;
//    }
//
//    public void setLinkedPlanServices(List<LinkedPlanService> linkedPlanServices) {
//        this.linkedPlanServices = linkedPlanServices;
//    }
//
//    public String get_org() {
//        return _org;
//    }
//
//    public void set_org(String _org) {
//        this._org = _org;
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
//
//    public String getPlanType() {
//        return planType;
//    }
//
//    public void setPlanType(String planType) {
//        this.planType = planType;
//    }
//
//    public String getCreationDate() {
//        return creationDate;
//    }
//
//    public void setCreationDate(String creationDate) {
//        this.creationDate = creationDate;
//    }

    //------------------------------------------------------------------------------------
    //    private String _org;
//    private String objectId;
//    private String objectType;
//    private String planType;
//    private String creationDate;
//    private Map<String, Object> planCostShares;
//    private List<LinkedPlanService> linkedPlanServices;
//
//    // Getters and setters
//
//
//    public String get_org() {
//        return _org;
//    }
//
//    public void set_org(String _org) {
//        this._org = _org;
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
//
//    public String getPlanType() {
//        return planType;
//    }
//
//    public void setPlanType(String planType) {
//        this.planType = planType;
//    }
//
//    public String getCreationDate() {
//        return creationDate;
//    }
//
//    public void setCreationDate(String creationDate) {
//        this.creationDate = creationDate;
//    }
//
//    public Map<String, Object> getPlanCostShares() {
//        return planCostShares;
//    }
//
//    public void setPlanCostShares(Map<String, Object> planCostShares) {
//        this.planCostShares = planCostShares;
//    }
//
//    public List<LinkedPlanService> getLinkedPlanServices() {
//        return linkedPlanServices;
//    }
//
//    public void setLinkedPlanServices(List<LinkedPlanService> linkedPlanServices) {
//        this.linkedPlanServices = linkedPlanServices;
//    }
//
//    public static class LinkedPlanService {
//        private Map<String, Object> linkedService;
//        private Map<String, Object> planserviceCostShares;
//
//        // Getters and setters
//
//        public Map<String, Object> getLinkedService() {
//            return linkedService;
//        }
//
//        public void setLinkedService(Map<String, Object> linkedService) {
//            this.linkedService = linkedService;
//        }
//
//        public Map<String, Object> getPlanserviceCostShares() {
//            return planserviceCostShares;
//        }
//
//        public void setPlanserviceCostShares(Map<String, Object> planserviceCostShares) {
//            this.planserviceCostShares = planserviceCostShares;
//        }
//    }



    //-----------------------------------------------------------------------------------------------
//    private Map<String, Object> planCostShares;
//
//    private List<LinkedPlanService> linkedPlanServices;
//    private String _org;
//    private String objectId;
//    private String objectType;
//    private String planType;
//    private String creationDate;
//
//    public Map<String, Object> getPlanCostShares() {
//        return planCostShares;
//    }
//
//    public void setPlanCostShares(Map<String, Object> planCostShares) {
//        this.planCostShares = planCostShares;
//    }
//
//    public String get_org() {
//        return _org;
//    }
//
//    public void set_org(String _org) {
//        this._org = _org;
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
//
//    public String getPlanType() {
//        return planType;
//    }
//
//    public void setPlanType(String planType) {
//        this.planType = planType;
//    }
//
//    public String getCreationDate() {
//        return creationDate;
//    }
//
//    public void setCreationDate(String creationDate) {
//        this.creationDate = creationDate;
//    }
//
//    public List<LinkedPlanService> getLinkedPlanServices() {
//        return linkedPlanServices;
//    }
//
//    public void setLinkedPlanServices(List<LinkedPlanService> linkedPlanServices) {
//        this.linkedPlanServices = linkedPlanServices;
//    }
//
//    public static class LinkedPlanService {
//
//        private Map<String, Object> linkedService;
//        private Map<String, Object> planserviceCostShares;
//        public Map<String, Object> getLinkedService() {
//            return linkedService;
//        }
//
//        public void setLinkedService(Map<String, Object> linkedService) {
//            this.linkedService = linkedService;
//        }
//
//        public Map<String, Object> getPlanserviceCostShares() {
//            return planserviceCostShares;
//        }
//
//        public void setPlanserviceCostShares(Map<String, Object> planserviceCostShares) {
//            this.planserviceCostShares = planserviceCostShares;
//        }
//
//    }

}
