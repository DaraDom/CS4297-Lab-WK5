# CS4297-Lab-WK5

Asynchronous Microservice Architecture with AWS Lambda, SQS, and DynamoDB

## Overview

This is a Spring Boot application that demonstrates how to refactor a synchronous, long-running API into an asynchronous, scalable architecture using AWS services. The project showcases the integration of:

- **Amazon SQS** for message queuing
- **AWS Lambda** for serverless processing
- **Amazon DynamoDB** for state management

## Architecture

```
Client Request → Spring Boot API → DynamoDB (In Progress)
                      ↓
                  Amazon SQS
                      ↓
                  AWS Lambda → Process Task (10s)
                      ↓
                  DynamoDB (Completed)
```

## Features

- **Synchronous Endpoint**: `/export` - Simulates a 10-second long-running task
- **Asynchronous Endpoint**: `/tasks` - Returns immediately with job ID
- **SQS Integration**: Publishes messages to queue for background processing
- **DynamoDB Integration**: Tracks job status (In Progress → Completed)

## Quick Start

### Prerequisites

- Java 21
- Gradle/Maven
- AWS Account (Free Tier)
- AWS IAM User with SQS and DynamoDB permissions

### Configuration

Edit `src/main/resources/application.properties`:

```properties
spring.application.name=async-microservice

aws.region=YOUR_REGION
aws.sqs.queue-url=YOUR_QUEUE_URL
aws.dynamodb.table-name=JobTable
```

### Environment Variables

Set these variables before running:

```bash
AWS_ACCESS_KEY_ID=your-access-key
AWS_SECRET_ACCESS_KEY=your-secret-key
```

### Run

```bash
cd async-microservice
./gradlew bootRun
```

## API Endpoints

### Synchronous Export (Step 1)

```bash
GET http://localhost:8080/export
```

**Response**: Takes 10 seconds, returns "Export completed"

### Asynchronous Task (Step 2+)

```bash
POST http://localhost:8080/tasks
Content-Type: application/json

{}
```

**Response**: Returns immediately with job ID

```json
{
  "status": 202,
  "body": "Task accepted with Job ID: 9a9cfc9b-cac4-4dac-9786-ac11ad9f1063"
}
```

## Related Repository

**Lambda Function**: [export-lambda-function](https://github.com/Tanxunze/export-lambda-function)

The Lambda function repository contains the serverless backend that processes messages from the SQS queue and updates DynamoDB job status.

## Project Structure

```
async-microservice/
├── src/
│   └── main/
│       ├── java/com/example/async_microservice/
│       │   ├── AsyncMicroserviceApplication.java
│       │   ├── AwsConfig.java
│       │   └── controller/
│       │       ├── ExportController.java      # Synchronous endpoint
│       │       └── TaskController.java        # Asynchronous endpoint
│       └── resources/
│           └── application.properties
├── build.gradle
└── README.md
```

## Dependencies

- Spring Boot 3.5.6
- Spring Web
- AWS SDK for Java (SQS & DynamoDB)
- Java 21

## Lab Instructions

This project is part of CS4297 Lab Week 5. For complete lab instructions and step-by-step guide, refer to the course materials.

