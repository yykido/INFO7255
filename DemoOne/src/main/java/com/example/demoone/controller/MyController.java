package com.example.demoone.controller;

import com.example.demoone.object.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plan")
public class MyController {

    @Autowired
    private RedisTemplate redisTemplate;


//    @GetMapping("")
//    public String homePage() {
//        return "Greetings from Spring Boot!";
//    }

    @GetMapping("")
    public String homePageIndex(@RequestParam(name = "index", required = false) String indexParam) {
        if (indexParam != null) {
            Object object = redisTemplate.boundValueOps(indexParam).get();
            return "Greetings from Spring Boot! Index: " + object;
        } else {
            return "Greetings from Spring Boot!";
        }
    }

//    @PostMapping("")
//    public String homePage(@RequestBody String studentName) {
//        return "Hello, my name is "+studentName;
//    }

    @PostMapping
    public ResponseEntity<String> createPlan(@RequestBody Plan plan) {
        try {
            // Generate a unique key for the plan in Redis

            String redisKey = plan.getObjectType()+":" + plan.getObjectId();
            // Store the healthcarePlan object in Redis
            redisTemplate.opsForValue().set(redisKey, plan);

            // Generate a key for the planCostShares in Redis
            PlanCostShares planCostShares = plan.getPlanCostShares();
            String key2 = planCostShares.getObjectType()+":"+planCostShares.getObjectId();
            redisTemplate.opsForValue().set(key2,planCostShares);

            // Generate keys for the linkedPlanServices in Redis
            List<LinkedPlanService> linkedPlanServices = plan.getLinkedPlanServices();
            for(LinkedPlanService curPlanService: linkedPlanServices) {
                //Generate a key for the planService in Redis
                String curKey3 = curPlanService.getGetObjectType()+":"+curPlanService.getObjectId();
                redisTemplate.opsForValue().set(curKey3,curPlanService);

                //Generate a key for the linkedService in Redis
                LinkedService linkedService = curPlanService.getLinkedService();
                String curKey = linkedService.getObjectType()+":"+linkedService.getObjectId();
                redisTemplate.opsForValue().set(curKey,linkedService);

                //Generate a key for the planserviceCostShares in Redis
                PlanserviceCostShares planserviceCostShares = curPlanService.getPlanserviceCostShares();
                String curKey2 = planserviceCostShares.getObjectType()+":"+planserviceCostShares.getObjectId();
                redisTemplate.opsForValue().set(curKey2,planserviceCostShares);
            }



            return ResponseEntity.status(HttpStatus.CREATED).body("Plan stored in Redis");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error storing plan in Redis");
        }
    }


//    @DeleteMapping
//    public ResponseEntity deletePlan(@RequestHeader HttpHeaders requestHeaders, @PathVariable String objectID) {
//        String key =
//    }



//    @PostMapping
//    public ResponseEntity<String> createHealthcarePlan(@RequestBody Plan plan) {
//        try {
//            // Store the main healthcarePlan object in Redis
//            String mainRedisKey = "healthcare-plan:" + plan.getObjectId();
//            redisTemplate.opsForValue().set(mainRedisKey, plan);
//
//            // Store each linkedPlanService that contains "objectId" in Redis
//            for (Plan.LinkedPlanService linkedPlanService : plan.getLinkedPlanServices()) {
//                if (linkedPlanService.getLinkedService().containsKey("objectId")) {
//                    String linkedServiceObjectId = linkedPlanService.getLinkedService().get("objectId").toString();
//                    String linkedServiceRedisKey = "linked-service:" + linkedServiceObjectId;
//                    redisTemplate.opsForValue().set(linkedServiceRedisKey, linkedPlanService);
//                }
//            }
//            return ResponseEntity.status(HttpStatus.CREATED).body("Healthcare plan and linked services stored in Redis");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error storing data in Redis");
//        }
//    }



    @DeleteMapping("")
    public String hompePage() {
        return "delete request";
    }
}
