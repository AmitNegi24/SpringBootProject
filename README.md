# eKart Application

A full-stack e-commerce application built using **Spring Boot**, **ReactJS**, and **MongoDB**.  
This project allows users to browse products, manage cart, place orders, and includes secure user authentication.

Note: This is just backend

---

## Features

- ✅ User Registration and Login (**JWT Authentication**)
- ✅ Browse Products and View Details
- ✅ Add Products to Cart and Checkout
- ✅ Place Orders
- ✅ RESTful APIs built with Spring Boot
- ✅ Responsive Frontend built with ReactJS

---

## Tech Stack

| Layer | Technology |
|-------|------------|
| **Backend** | Java, Spring Boot, Spring Security, JWT, Hibernate |
| **Frontend** | ReactJS, Redux, HTML, CSS |
| **Database** | MongoDB |
| **Tools** | Docker, Postman, Maven, Git |

---

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/customer-api/register` | Register new user |
| POST | `/api/customer-api/login` | User login and get JWT token |
| POST | `/api/product-api/product` | Create a product |
| GET | `/api/product-api/products` | Get all products |
| GET | `/api/products/{id}` | Get single product details |
| POST | `/api/checkout-api/checkout` | Place an order |

---

## How to Run (Backend)

```bash
# Clone the repository
git clone https://github.com/AmitNegi24/SpringBootProject.git

# Navigate into project directory
cd ekart

# Run the application
mvn spring-boot:run
