package com.example.demoone.queue;


import com.fasterxml.jackson.databind.JsonNode;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

@Component
public class MessageReceiver {

    @Autowired
    private RestHighLevelClient esClient;

    @RabbitListener(queues = "first_queue")
    public void receive(String jsonMessage) {

        String operation = jsonMessage.substring(0,1);
        String message = jsonMessage.substring(1);

        ObjectMapper mapper = new ObjectMapper();


//        JsonNode linkedPlanServicesNode = rootNode.path("linkedPlanServices");

        System.out.println("Received message: " + message);
        // create index

        switch (operation) {
            case "0":
                // put index
                System.out.println("case 0:");
//                CreateIndexRequest createIndexRequest = new CreateIndexRequest("plan_index");
//                createIndexRequest.mapping(
//                        "{\n" +
//                                "  \"properties\": {\n" +
//                                "    \"relation\": {\n" +
//                                "      \"type\": \"join\",\n" +
//                                "      \"relations\": {\n" +
//                                "        \"plan\": [\"planCostShares\", \"linkedPlanService\"],\n" +
//                                "        \"linkedPlanService\": [\"linkedService\", \"planserviceCostShares\"]\n" +
//                                "      }\n" +
//                                "    }\n" +
//                                "  }\n" +
//                                "}",
//                        XContentType.JSON
//                );
                try{
//                    JsonNode rootNode = mapper.readTree(message);
//                    JsonNode planCostSharesNode = rootNode.path("planCostShares");
//                    JsonNode linkedPlanServicesNode = rootNode.path("linkedPlanServices");
//                    IndexRequest planIndexRequest = new IndexRequest("plan_index");
//                    planIndexRequest.source(mapper.writeValueAsString(rootNode), XContentType.JSON); // grandparent
//                    IndexResponse planIndexResponse = esClient.index(planIndexRequest, RequestOptions.DEFAULT);
//
//                    planId = planIndexResponse.getId();
//                    System.out.println("id:"+ planId);
//                    IndexRequest serviceIndexRequest = new IndexRequest("plan_index").routing(planId);
//                    serviceIndexRequest.source(mapper.writeValueAsString(planCostSharesNode), XContentType.JSON);
//                    System.out.println("planCostsharesNode:");
//                    System.out.println(mapper.writeValueAsString(planCostSharesNode));
//                    esClient.index(serviceIndexRequest, RequestOptions.DEFAULT);

//                    esClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
                    indexDocument("plan","1111",message);
                } catch (Exception e) {
                    System.out.println("continue putting files on index");
                }
//                try {
//                    JsonNode rootNode = mapper.readTree(message);
//                    JsonNode planCostSharesNode = rootNode.path("planCostShares");
//                    IndexRequest serviceIndexRequest = new IndexRequest("plan_index").routing(planId);
//                    serviceIndexRequest.source(mapper.writeValueAsString(planCostSharesNode), XContentType.JSON);
//                    System.out.println("planCostsharesNode:");
//                    System.out.println(mapper.writeValueAsString(planCostSharesNode));
//                    esClient.index(serviceIndexRequest, RequestOptions.DEFAULT);
//                } catch(Exception e){
//                    System.out.println("continue putting planCostShares files on index");
//                }


                break;
            case "1":
                // update index
                System.out.println("case 1:");
                try{
                    updateDocument("plan","1111",message);
                } catch (Exception e) {
                    System.out.println("continue updating files");
                }
                break;
            case "2":
                // delete index
                System.out.println( "case 2:");
                try{
                    deleteIndex("plan");
                } catch (Exception e) {
                    System.out.println("continue deleting files");
                }

            default:
                break;

        }


//        IndexRequest request = new IndexRequest();
//        request.index("user2").id("1011");
//        request.source(message, XContentType.JSON);

//        IndexResponse response = esClient.index(request, RequestOptions.DEFAULT);
//        System.out.println(response.getResult());


//        IndexRequest indexRequest = new IndexRequest();
//        indexRequest.index("user").id("1003");
//        indexRequest.source(message, XContentType.JSON);
//        IndexResponse response = esClient.index(indexRequest, RequestOptions.DEFAULT);
//        System.out.println(response.getResult());

    }


// 方法用于将文档索引到 Elasticsearch
    public void indexDocument(String index, String id, String jsonDocument) throws IOException {
        IndexRequest indexRequest = new IndexRequest(index) // 索引名称
                .id(id)                                      // 文档ID
                .source(jsonDocument, XContentType.JSON);    // 文档内容
        esClient.index(indexRequest, RequestOptions.DEFAULT);
    }

    public void deleteIndex(String index) throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        esClient.indices().delete(request, RequestOptions.DEFAULT);
    }

    // 方法用于更新 Elasticsearch 中的现有文档
    public void updateDocument(String index, String id, String jsonDocument) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest(index, id)
                .doc(jsonDocument, XContentType.JSON);  // 更新内容
        esClient.update(updateRequest, RequestOptions.DEFAULT);
    }

    public void deleteDocument(String index, String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(index, id);
        esClient.delete(deleteRequest, RequestOptions.DEFAULT);
    }

    public void triggerError() {
        throw new RuntimeException("故意抛出的错误");
    }

//    @RabbitListener(queues = "first_queue")
//    public void receive(String message) throws IOException {
//        JSONObject jsonMessage = new JSONObject(message);
//        String operation = jsonMessage.getString("operation");
//        String index = "your_index";
//        String documentId = jsonMessage.getString("id");
//        String jsonData = jsonMessage.getJSONObject("data").toString();
//
//        switch (operation) {
//            case "create":
//                indexDocument(index, documentId, jsonData);
//                break;
//            case "update":
//                updateDocument(index, documentId, jsonData);
//                break;
//            default:
//                // 处理其他情况或记录错误
//                break;
//        }
//    }




}
