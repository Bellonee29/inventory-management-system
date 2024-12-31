##Inventory Management System

An inventory management system designed to handle product, orders, track stock levels, and generate order reports. 
The system uses Spring Boot for the backend, MySQL for the database, and Kafka for messaging.
Features:

   - Order Management: Place orders and update product stock levels.
   - Product Management: Manage product details including name, description, price, and stock quantity.
   - Reporting: Generate reports based on order data.
   - Messaging: Use Kafka to publish order details and consume order reports.

##Prerequisites:
   - Java 8 or higher
   - MySQL database
   - Docker & Docker Compose
   - Apache Kafka

##Installation:
  Clone the Repository
  - git clone <git@github.com:Bellonee29/inventory-management-system.git>
  - cd inventory-management

  Start Kafka and Zookeeper:
   - docker-compose up -d

  Build the project:
   - mvn clean install
   - mvn spring-boot:run
