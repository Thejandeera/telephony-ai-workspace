# Telephony AI - Microservices Architecture

A simple event-driven microservices project built with **Java Spring Boot** that simulates a backend telephony AI system. Live call events trigger automated status updates for customer support agents through REST-based communication between two independent services.

---

# Architecture

The project consists of two standalone Spring Boot microservices:

| Service | Port | Responsibility |
|---------|------|----------------|
| **Agent Status Service** | **8081** | Maintains agent information and their current availability. |
| **Call Event Service** | **8082** | Receives telephony events and updates agent status through REST APIs. |

---

# System Flow

```text
                    Incoming Telephony Event
                              │
                              ▼
                 Call Event Service (8082)
                              │
               REST API (HTTP PUT Request)
                              │
                              ▼
               Agent Status Service (8081)
                              │
            Updates Agent Availability State
```

### Event Processing Logic

| Call Event | Agent Status |
|------------|--------------|
| STARTED | BUSY |
| ANSWERED | BUSY |
| ENDED | AVAILABLE |

---

# Technology Stack

- Java 17
- Spring Boot
- Spring Web
- Maven
- REST APIs
- ConcurrentHashMap (In-Memory Storage)
- Postman / cURL for Testing

---

# Project Structure

```text
telephony-ai/
│
├── agent-status/
│   ├── src/
│   ├── pom.xml
│   └── mvnw
│
├── call-event/
│   ├── src/
│   ├── pom.xml
│   └── mvnw
│
└── README.md
```

---

# Getting Started

## Prerequisites

Make sure you have installed:

- Java 17 or higher
- Maven
- Git
- Postman (optional)

Verify installation:

```bash
java -version
mvn -version
```

---

# Running the Services

Both microservices must run simultaneously.

## Terminal 1 – Agent Status Service

```bash
cd agent-status
./mvnw spring-boot:run
```

Runs on:

```
http://localhost:8081
```

---

## Terminal 2 – Call Event Service

```bash
cd call-event
./mvnw spring-boot:run
```

Runs on:

```
http://localhost:8082
```

---

# API Reference

## Agent Status Service

Base URL

```
http://localhost:8081
```

### Get Agent

**Endpoint**

```
GET /api/agents/{id}
```

Example

```bash
curl -X GET http://localhost:8081/api/agents/agent-1
```

Response

```json
{
  "id": "agent-1",
  "name": "Alice",
  "status": "AVAILABLE"
}
```

---

### Update Agent Status

**Endpoint**

```
PUT /api/agents/{id}/status
```

Example

```bash
curl -X PUT http://localhost:8081/api/agents/agent-1/status \
-H "Content-Type: application/json" \
-d "{\"status\":\"BUSY\"}"
```

Response

```json
{
  "message": "Agent status updated successfully"
}
```

---

# Call Event Service

Base URL

```
http://localhost:8082
```

### Receive Call Event

**Endpoint**

```
POST /api/calls/events
```

Example

```bash
curl -X POST http://localhost:8082/api/calls/events \
-H "Content-Type: application/json" \
-d '{
      "callId":"call-1001",
      "agentId":"agent-1",
      "eventType":"STARTED"
    }'
```

Response

```
Event processed successfully
```

---

# Complete Call Lifecycle

## Step 1 – Verify Initial Agent Status

```http
GET http://localhost:8081/api/agents/agent-1
```

Expected Response

```json
{
  "id":"agent-1",
  "name":"Alice",
  "status":"AVAILABLE"
}
```

---

## Step 2 – Simulate Incoming Call

```http
POST http://localhost:8082/api/calls/events
```

Request Body

```json
{
  "callId":"1001",
  "agentId":"agent-1",
  "eventType":"STARTED"
}
```

---

## Step 3 – Verify Agent Became Busy

```http
GET http://localhost:8081/api/agents/agent-1
```

Expected Response

```json
{
  "id":"agent-1",
  "name":"Alice",
  "status":"BUSY"
}
```

---

## Step 4 – Simulate Call End

```http
POST http://localhost:8082/api/calls/events
```

Request Body

```json
{
  "callId":"1001",
  "agentId":"agent-1",
  "eventType":"ENDED"
}
```

---

## Step 5 – Verify Agent Is Available Again

```http
GET http://localhost:8081/api/agents/agent-1
```

Expected Response

```json
{
  "id":"agent-1",
  "name":"Alice",
  "status":"AVAILABLE"
}
```

---

# Internal Communication

The **Call Event Service** acts as an HTTP client.

When a call event is received:

### STARTED

```text
POST /api/calls/events
        │
        ▼
PUT /api/agents/{id}/status
Status = BUSY
```

---

### ANSWERED

```text
POST /api/calls/events
        │
        ▼
PUT /api/agents/{id}/status
Status = BUSY
```

---

### ENDED

```text
POST /api/calls/events
        │
        ▼
PUT /api/agents/{id}/status
Status = AVAILABLE
```

---

# Testing with Postman

### STARTED Event

```json
{
  "callId":"1001",
  "agentId":"agent-1",
  "eventType":"STARTED"
}
```

---

### ANSWERED Event

```json
{
  "callId":"1001",
  "agentId":"agent-1",
  "eventType":"ANSWERED"
}
```

---

### ENDED Event

```json
{
  "callId":"1001",
  "agentId":"agent-1",
  "eventType":"ENDED"
}
```



---

# Example Architecture Diagram

```text
                ┌──────────────────────────────┐
                │ Telephony System / AI Voice  │
                └──────────────┬───────────────┘
                               │
                               │ POST Call Event
                               ▼
                ┌──────────────────────────────┐
                │ Call Event Service (8082)    │
                └──────────────┬───────────────┘
                               │
                               │ REST API
                               ▼
                ┌──────────────────────────────┐
                │ Agent Status Service (8081)  │
                └──────────────┬───────────────┘
                               │
                               ▼
                    ConcurrentHashMap Storage
```

