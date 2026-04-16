System
🚀 Overview

SwiftPay is a distributed payment processing system built using Spring Boot microservices, Kafka event-driven architecture, Redis for idempotency, and PostgreSQL for persistence.

The system ensures:

✅ Reliable payment processing
✅ Idempotent transaction handling
✅ Asynchronous communication via Kafka
✅ Scalability and fault tolerance
🏗️ Architecture
Client
   ↓
Transaction Gateway (Service A)
   ├─ REST API
   ├─ Redis (Idempotency)
   ├─ Kafka Producer → payment-events
   ↓
Kafka
   ↓
Ledger Service (Service B)
   ├─ Kafka Consumer
   ├─ Balance Validation
   ├─ DB Updates (Postgres)
   ├─ Kafka Producer → payment-status
   ↓
Transaction Gateway
   ├─ Kafka Consumer
   ├─ Update Transaction Status
🧰 Tech Stack
Java 17
Spring Boot
Spring Data JPA
Apache Kafka
Redis
PostgreSQL
Docker & Docker Compose
Swagger (OpenAPI)
GitHub Actions (CI/CD)
📦 Project Structure
swiftpay/
│
├── transaction-gateway/   # API + Kafka Producer + Status Consumer
├── ledger-service/        # Kafka Consumer + Balance Processing
├── docker-compose.yml     # Infra setup (Kafka, Redis, Postgres)
⚙️ Features
✅ Payment Processing
POST /v1/payments
Asynchronous processing via Kafka
✅ Transaction Status
GET /v1/payments/{transactionId}
✅ Transaction History
GET /v1/payments/history/{userId}
✅ Idempotency
Redis-based (24-hour TTL)
Prevents duplicate transactions
✅ Balance Validation
Pre-check via Ledger API (optional)
Final validation in Ledger service
✅ Event-Driven Communication
payment-events → Gateway → Ledger
payment-status → Ledger → Gateway
📖 API Documentation

Swagger UI available at:

http://localhost:8080/swagger-ui.html
🧪 Sample API Requests
➤ Create Payment
POST /v1/payments
{
  "senderId": 1,
  "receiverId": 2,
  "amount": 500,
  "currency": "INR"
}
➤ Get Transaction Status
GET /v1/payments/{transactionId}
➤ Get Transaction History
GET /v1/payments/history/{userId}
🛡️ Error Handling

Standard error response:

{
  "message": "Insufficient balance",
  "status": 400,
  "timestamp": 1712345678900
}
🔁 Resilience
Kafka consumer retry mechanism (with backoff)
Handles transient failures (e.g., DB downtime)
Prevents system crash on bad messages
📊 Observability
Health Check
http://localhost:8080/actuator/health
Logging
Structured logs for tracing transactions
Includes transactionId for correlation
🐳 Running with Docker
🔹 Step 1: Build project
mvn clean install
🔹 Step 2: Start services
docker-compose up -d
🔹 Step 3: Verify
Gateway → http://localhost:8080
Ledger → http://localhost:8081
