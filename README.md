# Shopping Cart API 🛒🛍️

A RESTful **eCommerce API** for managing users, authentication, products, categories, carts, orders, and images.

Built with **Java**, **Spring Boot**, **Spring Security**, **JWT authentication** and **JPA/Hibernate**, following clean architecture and modern backend best practices.

This project was designed for learning, real-world practice, and as a solid foundation for scalable backend systems.


## 📌 Table of Contents

- [Shopping Cart API 🛒🛍️](#shopping-cart-api-️)
  - [📌 Table of Contents](#-table-of-contents)
  - [🌐 Overview](#-overview)
  - [⚙️ Features](#️-features)
  - [🔐 Authentication \& Authorization](#-authentication--authorization)
    - [Authentication](#authentication)
    - [Authorization](#authorization)
  - [💻 Technologies Used](#-technologies-used)
  - [🗝️ Key Takeaways](#️-key-takeaways)
  - [📁 Project Structure](#-project-structure)
  - [🚀 How to Run the Project](#-how-to-run-the-project)
    - [1️⃣ Clone the repository](#1️⃣-clone-the-repository)
    - [2️⃣ Configure environment variables](#2️⃣-configure-environment-variables)
    - [3️⃣ Run the application](#3️⃣-run-the-application)
  - [📄 API Endpoints](#-api-endpoints)
    - [🔑 Authentication](#-authentication)
    - [👤 Users](#-users)
    - [🛒 Cart](#-cart)
    - [🧩 Cart Items](#-cart-items)
    - [📦 Orders](#-orders)
    - [🏷️ Categories](#️-categories)
    - [🛍️ Products](#️-products)
    - [🖼️ Images](#️-images)
  - [🤝 Contributing](#-contributing)
  - [💬 Contact](#-contact)

## 🌐 Overview

The **Shopping Cart API** provides a complete backend solution for an eCommerce platform.

It supports:

* User registration and authentication
* Role-based access control (USER / ADMIN)
* Product and category management
* Shopping cart and cart items
* Order placement and retrieval
* Image upload and download

The API is **stateless**, secured with **JWT**, and designed following REST principles.

## ⚙️ Features

* ✅ **JWT-based authentication** (stateless)
* 🔐 **Role-based authorization**
* 👤 **User management** (admin-controlled)
* 🛒 **Shopping cart** per authenticated user
* 📦 **Cart items** management (add, update, remove)
* 🧾 **Order creation and history**
* 🏷️ **Product & category management**
* 🖼️ **Image upload/download**
* 🧠 **DTO mapping** with ModelMapper
* 🧪 **Centralized exception handling**
* 🧱 Clean, layered architecture (Controller → Service → Repository)

## 🔐 Authentication & Authorization

The API uses **Spring Security + JWT**.

### Authentication

* Users authenticate via `/auth/login`
* A JWT token is returned
* The token must be sent in the `Authorization` header:

```http
Authorization: Bearer <JWT_TOKEN>
```

### Authorization

* **USER** → can manage their own cart and orders
* **ADMIN** → can manage users, products, categories, images, and orders

## 💻 Technologies Used

* **Java 21** — Modern Java features and long-term support
* **Spring Boot 4** — Application framework
* **Spring Web MVC** — REST API development
* **Spring Security** — Authentication & authorization
* **JWT (jjwt)** — Stateless authentication
* **Spring Data JPA / Hibernate** — ORM and persistence
* **MySQL** — Relational database
* **ModelMapper** — Entity ↔ DTO mapping
* **Bean Validation (Jakarta Validation)** — Request validation
* **Lombok** — Reduces boilerplate code
* **Maven** — Dependency management & build tool

## 🗝️ Key Takeaways

1. Clean REST API design with Spring Boot
2. Stateless authentication using JWT
3. Secure role-based authorization
4. Separation of concerns with layered architecture
5. Real-world eCommerce domain modeling

## 📁 Project Structure

```bash
shopping-cart-api/
├── src/main/java/com/github/cauebf/shoppingcartapi/
│   ├── controller/        # REST controllers
│   ├── data/              # Initial database data
│   ├── service/           # Business logic
│   ├── repository/        # JPA repositories
│   ├── model/             # JPA entities
│   ├── dto/               # DTOs
│   ├── enums/             # Global enums
│   ├── request/           # Request payloads
│   ├── response/          # API responses
│   ├── security/          # Security (JWT, filters, config)
│   ├── exceptions/        # Custom exceptions & handlers
│   └── ShoppingCartApiApplication.java
├── src/main/resources/
│   └── application.properties
│   └── ...
├── pom.xml
└── README.md
```

## 🚀 How to Run the Project

### 1️⃣ Clone the repository

```bash
git clone https://github.com/Cauebf/shopping-cart-api.git
cd shopping-cart-api
```

### 2️⃣ Configure environment variables

Create a `.env` file or set system variables:

```env
DB_URL=jdbc:mysql://localhost:3306/shopping_cart
DB_USER=root
DB_PASSWORD=yourpassword
JWT_SECRET=your_jwt_secret
```

### 3️⃣ Run the application

```bash
mvn spring-boot:run
```

The API will run at:

```
http://localhost:8080
```

## 📄 API Endpoints

### 🔑 Authentication

* `POST /auth/login` → Authenticate user and return JWT

### 👤 Users

* `POST /users` → Register new user
* `GET /users/{id}` → Get user by ID (**ADMIN**)
* `PUT /users/{id}` → Update user (**ADMIN**)
* `DELETE /users/{id}` → Delete user (**ADMIN**)

### 🛒 Cart

* `GET /cart/me` → Get authenticated user's cart
* `DELETE /cart/{id}` → Delete cart (**ADMIN**)

### 🧩 Cart Items

* `GET /cart/items/{productId}` → Get cart item
* `POST /cart/items` → Add item to cart
* `PUT /cart/items/{productId}` → Update item quantity
* `DELETE /cart/items/{productId}` → Remove item
* `DELETE /cart/items` → Clear cart

### 📦 Orders

* `POST /orders` → Place order
* `GET /orders` → Get authenticated user's orders
* `GET /orders/{orderId}` → Get order by ID (**ADMIN**)

### 🏷️ Categories

* `GET /categories`
* `GET /categories/{id}`
* `GET /categories/name/{name}`
* `POST /categories` (**ADMIN**)
* `PUT /categories/{id}` (**ADMIN**)
* `DELETE /categories/{id}` (**ADMIN**)

### 🛍️ Products

* `GET /products`
* `GET /products/{id}`
* `POST /products` (**ADMIN**)
* `PUT /products/{id}` (**ADMIN**)
* `DELETE /products/{id}` (**ADMIN**)
* `GET /products/count`

### 🖼️ Images

* `GET /images/image/download/{imageId}`
* `POST /images/upload` (**ADMIN**)
* `PUT /images/image/{imageId}` (**ADMIN**)
* `DELETE /images/image/{imageId}` (**ADMIN**)

## 🤝 Contributing

Contributions are welcome! Feel free to open issues or pull requests for any improvements or bug fixes.

## 💬 Contact

For any inquiries or collaboration opportunities, feel free to reach out via:

[![Gmail](https://img.shields.io/badge/Gmail-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:cauebrolesef@gmail.com)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/cauebrolesef/)
[![Instagram](https://img.shields.io/badge/-Instagram-%23E4405F?style=for-the-badge&logo=instagram&logoColor=white)](https://www.instagram.com/cauebf_/)
[![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/Cauebf)

<p align="right">(<a href="#shopping-cart-api-">back to top</a>)</p>