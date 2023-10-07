package com.example.demoone.controller;

import com.example.demoone.object.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;
import java.util.*;

@RestController
@RequestMapping("/plan")
public class MyController {

    @Autowired
    private RedisTemplate redisTemplate;
    private HttpHeaders headers = new HttpHeaders();
    private Set<String> redisKeys = new HashSet<>();

    @GetMapping
    public ResponseEntity<String> homePageIndex(@RequestHeader(name = "If-None-Match", required = false) String ifNoneMatch, @RequestParam(name = "objectId", required = false) String indexParam) {
        if (ifNoneMatch != null && ifNoneMatch.equals(headers.getETag())) {
            // if matched, respond with 304
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }

        if (indexParam != null) {
            Object object = redisTemplate.boundValueOps(indexParam).get();
            if (object != null) {
//                return ResponseEntity.ok().body(new JSONPObject(object).toString());
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    String jsonString = objectMapper.writeValueAsString(object);
                    return ResponseEntity.ok(jsonString);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
                }
            } else {
                return ResponseEntity.ok("Object is null");
            }
        } else {

            StringBuffer sb = new StringBuffer();
            for(String key: redisKeys) {
                System.out.println(key);
                sb.append(key);
                sb.append(", ");
            }
            return ResponseEntity.ok(sb.toString());
        }
    }

    @PostMapping
    public ResponseEntity<String> createPlan(@RequestBody Plan plan) {

        try {
//             Generate a unique key for the plan in Redis
            String planJson = convertObjectToJson(plan);
            String eTag = "W/"+calculateETag(planJson)+"\"";
            headers.setETag(eTag);

            String redisKey = plan.getObjectType()+":" + plan.getObjectId();
            // Store the healthcarePlan object in Redis
            redisTemplate.opsForValue().set(redisKey, plan);
            redisKeys.add(redisKey);

            // Generate a key for the planCostShares in Redis
            PlanCostShares planCostShares = plan.getPlanCostShares();
            String key2 = planCostShares.getObjectType()+":"+planCostShares.getObjectId();
            redisTemplate.opsForValue().set(key2,planCostShares);
            redisKeys.add(key2);

            // Generate keys for the linkedPlanServices in Redis
            List<LinkedPlanService> linkedPlanServices = plan.getLinkedPlanServices();
            for(LinkedPlanService curPlanService: linkedPlanServices) {
                //Generate a key for the planService in Redis
                String curKey3 = curPlanService.getObjectType()+":"+curPlanService.getObjectId();
                redisTemplate.opsForValue().set(curKey3,curPlanService);
                redisKeys.add(curKey3);

                //Generate a key for the linkedService in Redis
                LinkedService linkedService = curPlanService.getLinkedService();
                String curKey = linkedService.getObjectType()+":"+linkedService.getObjectId();
                redisTemplate.opsForValue().set(curKey,linkedService);
                redisKeys.add(curKey);

                //Generate a key for the planserviceCostShares in Redis
                PlanserviceCostShares planserviceCostShares = curPlanService.getPlanserviceCostShares();
                String curKey2 = planserviceCostShares.getObjectType()+":"+planserviceCostShares.getObjectId();
                redisTemplate.opsForValue().set(curKey2,planserviceCostShares);
                redisKeys.add(curKey2);
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                    .headers(headers)
                    .body("Plan stored in Redis");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error storing plan in Redis");
        }
    }

    @DeleteMapping("/delete/{id}")
    public String deletePlan(@RequestHeader HttpHeaders requestHeaders, @PathVariable String id) {
        String key = id;
        if(!redisTemplate.hasKey(key)) {
            return "not found";
        }
        else {
            redisTemplate.delete(key);
            redisKeys.remove(key);
            return key+" has been deleted";
        }

    }

    @DeleteMapping("/delete")
    public String deleteAll(@RequestHeader HttpHeaders requestHeaders) {
        if(redisKeys.isEmpty()) return "There is no keys on Redis";
        for(String key: redisKeys) {
            redisTemplate.delete(key);
        }
        redisKeys.clear();

        return "All keys have been deleted";
    }

    public Set<String> getAllKeys() {
        return new HashSet<>();
    }

    private String convertObjectToJson(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonString = objectMapper.writeValueAsString(object);
            return jsonString;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    private String calculateETag(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(data.getBytes());

        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }

        return hexString.toString();
    }


}
