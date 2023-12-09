package com.example.demoone.object;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@Getter
@Setter
public class LinkedService{

    @NotNull
    private String _org;
    @NotNull
    private String objectId;
    @NotNull
    private String objectType;
    @NotNull
    private String name;

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
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
}
