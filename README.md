# Price Comparison REST API

A Spring Boot REST API for a price comparison application that allows users to search products, compare prices across stores, and manage product information.

The project focuses on building a maintainable backend application using RESTful API design, layered architecture, database modelling, automated testing, and CI/CD practices.

## Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [API Endpoints](#api-endpoints)
- [Running Locally](#running-locally)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [CI/CD](#cicd)
- [Challenges & Solutions](#challenges--solutions)
- [Future Improvements](#future-improvements)

---

## Features

#### Product Search

- Search products by name, category, and brand.
- Retrieve product details and available store prices.

#### Price Comparison

- Compare prices of the same product across different stores.
- Return product variations with pricing information.

#### Product Management

- Create, update, retrieve, and delete products.
- Manage relationships between products and stores.

---

## Technology Stack

- **Backend**: Java, Spring Boot
- **Database**: MySQL
- **Testing**: JUnit, Mockito
- **Build & CI/CD**: Maven, GitHub Actions
- **API Documentation**: Swagger / OpenAPI
- **Development**: Docker Compose

## Architecture

The application follows a layered architecture:

```
Controller
    |
Service
    |
Repository
    |
Database
```

Main responsibilities:

- **Controllers** handle HTTP requests and API responses.
- **Services** contain business logic.
- **Repositories** manage database access.
- **DTOs** are used to separate API models from database entities.

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/products/search` | Search products |
| GET | `/products` | Retrieve products |
| GET | `/products/{id}` | Retrieve product details |
| GET | `/products/{id}/variations` | Compare prices across stores |
| PUT | `/products/{id}` | Update product |
| DELETE | `/products/{id}` | Delete product |

Request and response examples can be found [here](https://gist.github.com/tn0909/938735954974764c2d99f60384c62fbf)


## Running Locally

#### Prerequisites

- Java 8+
- Maven
- Docker

#### Start Database

Run MySQL using Docker Compose:

```bash
docker compose up
```

#### Build Application

```bash
mvn clean install
```

#### Run Application

```bash
mvn spring-boot:run
```

The API will be available at:

```
http://localhost:8080
```

## API Documentation

Swagger UI:

```
http://localhost:8080/swagger-ui/
```

Use Swagger UI to explore available endpoints and test API requests.

## Testing

Run automated tests:

```bash
mvn test
```

The project uses:

- JUnit for unit testing.
- Mockito for mocking dependencies.

## CI/CD

GitHub Actions runs automated build and test checks when changes are pushed.

The pipeline helps verify that:

- The application builds successfully.
- Automated tests pass before changes are merged.


## Challenges & Solutions

#### Designing Product and Store Relationships

**Challenge**

A product can be available from multiple stores with different prices. The data model needed to represent product information while supporting price comparison.

**Solution**

Designed separate entities for products, stores, and product variations to maintain clear relationships and avoid duplicating product data.

## Future Improvements

- Add authentication and authorization using Spring Security
- Add pagination and sorting for product search
- Introduce caching for frequently accessed products
- Deploy the application using Docker and cloud infrastructure

## Contributing

Contributions are welcome! If you have suggestions for improving the API or adding new features, please open an issue to discuss or submit a pull request with your changes.

## License

Please credit the author if you use this project or any of its components in your own work.
