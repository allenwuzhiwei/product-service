# 🛒 Product MicroService - E-commerce Backend Module

## Overview

The **Product Service** is a core microservice within our e-commerce platform.  
It is responsible for managing the life cycle of product-related data, including product information, media resources, and customer feedback.

It is built with **Spring Boot**, **MyBatis-Plus**, and integrates with other microservices via **Eureka** (service discovery) and **Spring Cloud Config**.

---

## Project Structure

The `product-service` project follows a standard **Spring Boot + MyBatis Plus** architecture,  
with clear separation of concerns across configuration, controller, service, DAO, and entity layers.


- `config/`  
  ➤ Configuration classes including API response wrappers, Swagger config, Security settings, and global exception handling.

- `controller/`  
  ➤ RESTful API controllers for Product, ProductMedia, and ProductFeedback modules.

- `dao/`  
  ➤ MyBatis Plus mapper interfaces for interacting with the database.

- `entity/`  
  ➤ Domain model classes that map to database tables.

- `exception/`  
  ➤ Custom exception definitions used for error handling.

- `service/` and `service/impl/`  
  ➤ Interfaces and implementations of business logic.

- `resources/`  
  ➤ Configuration files (`application.properties`) and static resources.

- `ProductServiceApplication.java`  
  ➤ The main entry point of the Spring Boot application.

- `pom.xml`  
  ➤ Maven configuration for dependencies and build settings.


---

## Implemented Features 

### 1️⃣ Product Module

**Basic Functions**
- ✅ Create a new product
- ✅ Retrieve all products
- ✅ View product by ID
- ✅ Update product information
- ✅ Delete product

**Extended Features**
- ✅ Pagination
- ✅ Keyword search (name / description)
- ✅ Filter by multiple criteria (name, category, price, status, rating)
- ✅ Sort by price, rating or time
- ✅ Auto-attach product cover image (from ProductMedia)

---

### 2️⃣ ProductMedia Module

**Basic Functions**
- ✅ Upload new media (image / video)
- ✅ Retrieve all media records
- ✅ View media by ID
- ✅ Retrieve media by product ID
- ✅ Update media info (type, URL)
- ✅ Delete media record

---

### 3️⃣ ProductFeedback Module

**Basic Functions**
- ✅ Submit feedback (rating + comment)
- ✅ View all feedback entries
- ✅ View feedback by ID
- ✅ View feedback by product ID
- ✅ Update feedback
- ✅ Delete feedback

**Extended Features**
- ✅ Get average rating of a product
- ✅ Get total number of feedback
- ✅ Pagination + sorting of feedback (by rating/time)

---

## 👥 Collaborators 

| Name         | Role               | Description                                                                                          |
|--------------|--------------------|------------------------------------------------------------------------------------------------------|
| **Song Yinrui** | Backend Developer  | Responsible for core backend development, including Product, ProductMedia, and ProductFeedback modules. Implemented RESTful APIs, business logic, and API documentation/testing. |
| **Wu Zhiwei**   | System Architect    | Designing and setting up the microservice architecture and framework.  Creating and managing the database tables for the Product microservice. |

---
## Author
- Song Yinrui
- National University of Singapore, Institute of Systems Science (NUS-ISS)
- Development Start: April 2025
---