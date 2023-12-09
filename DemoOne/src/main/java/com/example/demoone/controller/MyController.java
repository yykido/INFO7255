package com.example.demoone.controller;

import com.example.demoone.object.*;
import com.example.demoone.queue.MessageSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/plan")
public class MyController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MessageSender messageSender;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private HttpHeaders headers = new HttpHeaders();
    Plan planInstance = null;
//    private Set<String> redisKeys = new HashSet<>();

    @GetMapping
//    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Object> homePageIndex(@RequestHeader(name = "If-None-Match", required = false) String ifNoneMatch, @RequestParam(name = "objectId", required = false) String indexParam) {
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
                    return ResponseEntity.ok(object);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
                }
            } else {
                return ResponseEntity.ok("Object is null");
            }
        } else {
            Set<String> keys = redisTemplate.keys("*");
            StringBuffer sb = new StringBuffer();
            for(String key: keys) {
                sb.append(key);
                sb.append(", ");
            }
            return ResponseEntity.ok(redisTemplate.boundValueOps("plan:12xvxc345ssdsds-508").get());
        }
    }

    @PostMapping
    public ResponseEntity<Plan> createPlan(@Valid @RequestBody Plan plan) {
        // generate ETag
        try{
            String planJson = convertObjectToJson(plan);
            String eTag = "\""+calculateETag(planJson)+"\"";
            headers.setETag(eTag);
            redisTemplate.opsForValue().set("eTag",eTag);
        }
        catch (NoSuchAlgorithmException ae) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(plan);
        }

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
            String curKey3 = curPlanService.getObjectType()+":"+curPlanService.getObjectId();
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

        try {
            String s = objectMapper.writeValueAsString(plan);
            s = "0"+s;
            messageSender.send("first_queue", s);
            System.out.println("Message sent to the queue successfully");
            return new ResponseEntity<>(plan, headers, HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println("Error sending message");
            return new ResponseEntity<>(plan, headers, HttpStatus.INTERNAL_SERVER_ERROR);
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
//            redisKeys.remove(key);
            return key+" has been deleted";
        }
    }

    @DeleteMapping("/delete")
    public String deleteAll(@RequestHeader HttpHeaders requestHeaders) {

        Set<String> keys = redisTemplate.keys("*");
        if(keys.isEmpty()) return "There is no keys on Redis";
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }


        try {
            String s = "20";
            messageSender.send("first_queue", s);
            System.out.println("Message sent to the queue successfully");
            return "All keys have been deleted";
        } catch (Exception e) {
            System.out.println("Error sending message");
            return "Something wrong in deleteAll methods";
        }

    }

    public void updateFields(Object planInstance, Map<String, Object> fieldsToUpdate) {
        Class<?> planClass = planInstance.getClass();

        fieldsToUpdate.forEach((fieldName, fieldValue) -> {
            try {
                Field field = planClass.getDeclaredField(fieldName); // 获取字段
                field.setAccessible(true); // 使私有字段可访问
                field.set(planInstance, fieldValue); // 设置新的字段值
            } catch (NoSuchFieldException e) {
                // 如果没有这个字段，则可能想要记录这个问题或者抛出一个异常
                System.out.println("Field not found: " + fieldName);
            } catch (IllegalAccessException e) {
                // 如果这个字段不能被访问，处理这个安全异常
                System.out.println("Access to field denied: " + fieldName);
            }
        });
    }

    @PatchMapping("/{objectId}")
    public ResponseEntity<Plan> updatePlan(@RequestHeader(name = "If-None-Match", required = false) String ifNoneMatch, @PathVariable("objectId") String objectId, @Valid @RequestBody Plan plan) {
//        updateFields(planInstance, fieldsToUpdate);
//        return new ResponseEntity<>(planInstance, headers, HttpStatus.CREATED);
        if (ifNoneMatch != null && ifNoneMatch.equals(headers.getETag())) {
            // if matched, respond with 304
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }

        // header to be modified.
        planInstance = (Plan)redisTemplate.boundValueOps("plan:12xvxc345ssdsds-508").get(); // plan:12xvxc345ssdsds-508

        try {
            String planJson = convertObjectToJson(plan);
            String eTag = "\""+calculateETag(planJson)+"\"";
            if(redisTemplate.boundValueOps("eTag").get().equals(eTag)) {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
            }
            headers.setETag(eTag);
            String redisKey = plan.getObjectType()+":" + plan.getObjectId();
            // Store the healthcarePlan object in Redis
            redisTemplate.opsForValue().set(redisKey, plan);
            redisTemplate.opsForValue().set("eTag",eTag);
            String s = objectMapper.writeValueAsString(plan);
            s = "1"+s;
            messageSender.send("first_queue", s);


            return ResponseEntity.ok().headers(headers).body(plan);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(plan);
        }
    }

//    @PatchMapping("/{objectId}")
//    public ResponseEntity<Plan> updatePlan(@RequestHeader(name = "If-None-Match", required = false) String ifNoneMatch, @PathVariable("objectId") String objectId, @RequestBody Map<String, Object> fieldsToUpdate) {
////        updateFields(planInstance, fieldsToUpdate);
////        return new ResponseEntity<>(planInstance, headers, HttpStatus.CREATED);
//        if (ifNoneMatch != null && ifNoneMatch.equals(headers.getETag())) {
//            // if matched, respond with 304
//            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
//        }
//
//        // header to be modified.
//        planInstance = (Plan)redisTemplate.boundValueOps("plan:12xvxc345ssdsds-508").get(); // plan:12xvxc345ssdsds-508
//
//        boolean found = false;
//        List<LinkedPlanService> linkedPlanServicelist = planInstance.getLinkedPlanServices();
//        if(planInstance.getObjectId().equals(objectId)) {
//            found = true;
//            updateFields(planInstance,fieldsToUpdate);
//        }
//        else if(planInstance.getPlanCostShares().getObjectId().equals(objectId)) {
//            found = true;
//            updateFields(planInstance.getPlanCostShares(),fieldsToUpdate);
//        }
//        else {
//            for (LinkedPlanService linkedPlanService : linkedPlanServicelist) {
//                if (linkedPlanService.getLinkedService().getObjectId().equals(objectId)) {
//                    found = true;
//                    updateFields(linkedPlanService.getLinkedService(), fieldsToUpdate);
//                } else if (linkedPlanService.getPlanserviceCostShares().getObjectId().equals(objectId)) {
//                    found = true;
//                    updateFields(linkedPlanService.getPlanserviceCostShares(), fieldsToUpdate);
//
//                } else if (linkedPlanService.getObjectId().equals(objectId)) {
//                    found = true;
//                    updateFields(linkedPlanService.getObjectId(), fieldsToUpdate);
//                }
//            }
//        }
//        if(!found) return ResponseEntity.notFound().build();
////         Update the eTag as the content has changed
//        try {
//            String planJson = convertObjectToJson(planInstance);
//            String eTag = "\""+calculateETag(planJson)+"\"";
//            if(redisTemplate.boundValueOps("eTag").get().equals(eTag)) {
//                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
//            }
//            headers.setETag(eTag);
//            redisTemplate.opsForValue().set("plan:12xvxc345ssdsds-508",planInstance);
//            return ResponseEntity.ok().headers(headers).body(planInstance);
//
//        } catch (NoSuchAlgorithmException ae) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(headers).body(planInstance);
//        }
//    }

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
