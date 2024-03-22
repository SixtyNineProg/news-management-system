# News Management System

This application is a news management system that consists of two services: a comment service and a news service. Both services use PostgreSQL databases and are orchestrated using Docker Compose.

## Prerequisites

- Docker
- Docker Compose

## Getting Started

1. **Clone the repository**

   Clone this repository to your local machine.

2. **Build and run the application**
   1. Navigate to the root directory of the **comment-service** and run the following command:
      ```bash
      ./gradlew build
      ```
   2. Navigate to the root directory of the **news-service** and run the following command:
      ```bash
      ./gradlew build
      ```
   3. Navigate to the root directory of the project where the `docker-compose.yaml` file is located and run the following command:

      ```bash
      docker-compose up --build
      ```
- Swagger UI client-сервиса доступен по URL - `http://localhost:8081/v1/swagger-ui/index.html`
- Swagger UI news-сервиса доступен по URL - `http://localhost:8082/v1/swagger-ui/index.html`