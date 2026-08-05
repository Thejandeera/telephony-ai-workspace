# Telephony AI - Event-Driven Microservices

A completely decoupled, asynchronous microservices architecture built with **Java Spring Boot 4.x** and **Apache Kafka**. It simulates a backend telephony system where live call events (e.g., `STARTED`, `ENDED`) trigger automated status updates for customer support agents without blocking HTTP requests.

---

## Architecture & Flow

Instead of services communicating directly via REST, they are decoupled using a Publisher/Subscriber model via Kafka.

```text
[ Postman / AI Voice Agent ]
             │ (POST Request)
             ▼
[ Call Event Service (:8082) ] ──▶ (Producer) Publishes JSON to Topic
                                          │
                                   [ Apache Kafka ] (Topic: telephony.events)
                                          │
[ Agent Status Service (:8081) ] ◀── (Consumer) Reads JSON & Updates DB
```

---

## Project Structure & File Roles

### 1. Call Event Service (Producer)

This service receives API requests and publishes them to the Kafka topic.

- **config/KafkaProducerConfig.java**  
  Explicitly configures the `KafkaTemplate` and sets up the modern Jackson JSON serializer to format outbound messages.

- **controller/CallEventController.java**  
  Exposes the REST endpoint (`/api/calls/events`) that accepts incoming call event requests.

- **model/CallEvent.java** & **CallEventType.java**  
  Define the data structures representing call events.

- **service/CallEventProcessor.java**  
  Contains the core business logic that publishes incoming events to the `telephony.events` Kafka topic.

---

### 2. Agent Status Service (Consumer)

This service owns the mock database and continuously listens for Kafka events.

- **config/KafkaConsumerConfig.java**  
  Configures the Kafka listener to bypass package headers and consume raw JSON messages.

- **controller/AgentController.java**  
  Exposes a GET endpoint (`/api/agents/{id}`) for retrieving an agent's current status.

- **dto/**  
  Contains local Data Transfer Objects used by Jackson to deserialize incoming Kafka messages.

- **model/**  
  Contains entity classes representing customer support agents and their statuses (`AVAILABLE`, `BUSY`, `OFFLINE`).

- **service/AgentService.java**  
  Manages the `ConcurrentHashMap` mock database for thread-safe agent storage and retrieval.

- **service/KafkaEventListener.java**  
  Implements the `@KafkaListener` that consumes Kafka events, parses the JSON payload, and updates the agent status.

---

## Getting Started

### 1. Start the Kafka Environment

Ensure Docker is running, then start the Kafka broker and UI.

```bash
docker-compose up -d
```

**Kafka Broker:** `localhost:9092`

**Kafka UI:** `http://localhost:8080`

---

### 2. Start the Microservices

Run both Spring Boot applications simultaneously.

```bash
# Terminal 1
cd agent-status
./mvnw spring-boot:run

# Terminal 2
cd call-event
./mvnw spring-boot:run
```

---

## Testing the Flow

### 1. Check Initial Agent Status

```bash
curl -X GET http://localhost:8081/api/agents/agent-1
```

Response:

```json
{
  "id": "agent-1",
  "name": "Thejandeera",
  "status": "AVAILABLE"
}
```

---

### 2. Send a Call Event

```bash
curl -X POST http://localhost:8082/api/calls/events \
-H "Content-Type: application/json" \
-d '{"callId":"call-1001","agentId":"agent-1","eventType":"STARTED"}'
```

Response:

```text
Event processed successfully
```

---

### 3. Verify the Agent Status Update

```bash
curl -X GET http://localhost:8081/api/agents/agent-1
```

Response:

```json
{
  "id": "agent-1",
  "name": "Thejandeera",
  "status": "BUSY"
}
```

---

### 4. Monitor the System

Open the Kafka UI:

```
http://localhost:8080
```

Navigate to the **telephony.events** topic to inspect the JSON messages being published and consumed in real time.