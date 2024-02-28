# Price Comparison RESTful API

This project aims to develop a RESTful APIs for a price comparison website. The API allows users to search for products and compare prices from various stores.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [API Documentation](#api-documentation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)
- [Acknowledgments](#acknowledgments)

## Features

- **Product Search**: Search for products by name, category, brand, etc.
- **Price Comparison**: Compare prices for the same product from different stores.
- **Admin Operations**: CRUD operations for managing products and stores.

## Technologies Used

- **Java**: Programming language for backend development
- **Spring Boot**: Framework for building RESTful APIs
- **MySQL**: Database management system for storing product and store information
- **Auth0**: Authentication and authorization service to secure create/update/delete APIs
- **JUnit and Mockito**: Testing frameworks for unit and integration testing
- **Maven**: Dependency management and build automation
- **GitHub Actions**: CI framework for automating the process of testing code changes upon each push to the repository

## Installation

### Prerequisites

- Java Development Kit (JDK) 8 or later
- Maven
- MySQL

### Steps

1. Clone the repository:
```
git clone https://github.com/tn0909/prico.git
```

2. Configure the database connection in `application.yml`.

3. Build the project:
```
cd prico
mvn clean install
```

4. Run the application:
```
mvn spring-boot:run
```

6. The API will be accessible at `http://localhost:8080`.

## API Documentation

The API documentation is available at `http://localhost:8080/swagger-ui/`. Use this interface to explore available endpoints and test API functionality.

## Usage

The API supports various endpoints for searching products, retrieving price comparisons, managing products, and more. Refer to the API documentation for full details on available endpoints, request/response formats, and authentication requirements.

[Here](https://gist.github.com/tn0909/938735954974764c2d99f60384c62fbf) are request/response samples for the following endpoints:

```
POST   ​/products/search
GET    ​/products/{id}/variations
GET    ​/products
GET    ​/products/{id}
PUT    ​/products/{id}
DELETE ​/products/{id}
```

## Contributing

Contributions are welcome! If you have suggestions for improving the API or adding new features, please open an issue to discuss or submit a pull request with your changes.

## License

Please credit the author if you use this project or any of its components in your own work.

## Acknowledgments

Libraries and frameworks used
