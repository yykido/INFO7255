package com.example.demoone.object;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class LinkedPlanService{
    @Valid
    @NotNull
    private LinkedService linkedService;
    @Valid
    @NotNull
    private PlanserviceCostShares planserviceCostShares;

    @NotNull
    private String _org;

    @NotNull
    private String objectId;
    @NotNull
    @NotEmpty(message = "objectType is required.")
    private String objectType;
}
