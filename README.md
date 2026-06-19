# AI-Powered Fitness Microservices Platform

A cloud-native fitness tracking platform built using **Spring Boot Microservices** and **Spring Cloud**. 
The application enables users to securely manage their fitness activities while leveraging a scalable microservices architecture with centralized authentication 
using **Keycloak**.

# Features

- Secure user authentication and authorization using **Keycloak**
- JWT-based authentication with **OAuth2 Resource Server**
- API Gateway for centralized request routing
- Service discovery using **Netflix Eureka**
- Centralized configuration using **Spring Cloud Config Server**
- Automatic user synchronization from Keycloak to User Service
- RESTful APIs for user and activity management
- Microservices communication using **Spring WebClient**
- Asynchronous communication between AI service and Google Gemini using Apache Kafka to generation AI powered recommendation
- Scalable and loosely coupled architecture

# Microservices Architecture

The project consists of the following services:

### API Gateway
- Entry point for all client requests
- Validates JWT tokens issued by Keycloak
- Routes requests to respective microservices
- Synchronizes authenticated users with User Service

### User Service
- Stores user profile information
- Handles user-related APIs
- Registers new users coming from Keycloak

### Activity Service
- Manages workout and fitness activities
- Stores user activity records
- Retrieves activity history

### AI Recommendation Service (Google Gemini)
- Generates personalized fitness recommendations
- Suggests workout plans based on activity history

### Eureka Server
- Service Registry
- Enables dynamic service discovery

### Config Server
- Provides centralized configuration for all microservices

### Keycloak
- Identity and Access Management
- OAuth2 Authentication and Authorization Server
- Issues JWT access tokens

# Tech Stack

## Backend
- Java 21
- Spring Boot
- Spring Security
- Spring WebFlux (WebClient)
- Spring Cloud Gateway
- Spring Cloud Config
- Netflix Eureka

## Authentication
- Keycloak
- OAuth2

## Database
- MySQL
- MongoDB

## Build Tool
- Maven

## Tools
- Git
- GitHub
- Postman
- VS Code
  

**Event-Driven AI Recommendation Flow**

Instead of directly invoking the AI service synchronously, the application uses Apache Kafka to decouple services.

User requests an AI recommendation.
Activity Service publishes the request to a Kafka topic.
AI Recommendation Service consumes the event.
AI Recommendation Service sends the prompt to Google Gemini API.
Gemini generates a personalized recommendation.
The AI service publishes the response to another Kafka topic.
The client receives the processed recommendation.

This asynchronous architecture improves scalability, resilience, and service decoupling.



# Project Structure

fitness-application
│
├── api-gateway
├── user-service
├── activity-service
├── ai-service
├── eureka-server
├── config-server
└── README.md

# Authentication Flow

1. User logs in through Keycloak.
2. Keycloak issues a JWT Access Token.
3. Client sends the token to the API Gateway.
4. API Gateway validates the token.
5. Gateway synchronizes the user with the User Service (if not already registered).
6. Gateway forwards the request to the target microservice.

# Request Flow


Client
   │
   ▼
API Gateway
   │
   ├────────► Keycloak (JWT Validation)
   │
   ├────────► User Service
   │
   ├────────► Activity Service
   │
   └────────► AI Recommendation Service

# Getting Started

## Prerequisites

- Java 17+
- Maven
- PostgreSQL / MySQL
- Keycloak
- Git

---

## Clone Repository

git clone https://github.com/your-username/fitness-application.git


Access all APIs through the API Gateway.

# Key Highlights

- Microservices Architecture
- Secure Authentication with Keycloak
- JWT Token Validation
- Spring Cloud Gateway
- Eureka Service Discovery
- Config Server
- WebClient-based Inter-Service Communication
- RESTful API Design
- Centralized Security

# Author

Developed as a learning project to explore **Spring Boot Microservices**, **Spring Cloud**, and **Keycloak-based Authentication** while following industry-standard backend architecture.
