package com.example.async_microservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final SqsClient sqsClient;
    private final DynamoDbClient dynamoDbClient;

    @Value("${aws.sqs.queue-url}")
    private String queueUrl;

    @Value("${aws.dynamodb.table-name}")
    private String tableName;

    public TaskController(SqsClient sqsClient, DynamoDbClient dynamoDbClient) {
        this.sqsClient = sqsClient;
        this.dynamoDbClient = dynamoDbClient;
    }

    @PostMapping
    public ResponseEntity<String> submitTask(@RequestBody(required = false) String taskDetails) {

        String jobId = UUID.randomUUID().toString();

        Map<String, AttributeValue> item = new HashMap<>();
        item.put("jobId", AttributeValue.builder().s(jobId).build());
        item.put("status", AttributeValue.builder().s("In Progress").build());

        PutItemRequest putReq = PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build();

        dynamoDbClient.putItem(putReq);

        String timestamp = java.time.Instant.now().toString();
        String messageBody = "{ \"jobId\": \"" + jobId + "\", \"taskType\": \"export\", \"timestamp\": \"" + timestamp + "\" }";

        SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .build();

        try {
            sqsClient.sendMessage(sendMsgRequest);
            System.out.println("Message sent to SQS successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //sqsClient.sendMessage(sendMsgRequest);

        return ResponseEntity.accepted().body("Task accepted with Job ID: " + jobId);
    }
}
