# Price Comparison RESTful API

This project aims to develop a RESTful APIs for a price comparison website. The API allows users to search for products and compare prices from various stores.

## Features

- **Product Search**: Search for products by name, category, brand, etc.
- **Price Comparison**: Compare prices for the same product from different stores.
- **Store Listings**: Retrieve a list of stores offering a particular product.
- **Admin Operations**: CRUD operations for managing products, stores, and reviews.

## Technologies Used

- **Java**: Programming language for backend development
- **Spring Boot**: Framework for building RESTful APIs
- **MySQL**: Database management system for storing product and store information
- **JUnit and Mockito**: Testing frameworks for unit and integration testing
- **Maven**: Dependency management and build automation

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

The API supports various endpoints for searching products, retrieving price comparisons, managing products, and more. Refer to the API documentation for details on available endpoints, request/response formats, and authentication requirements.

## Contributing

Contributions are welcome! If you have suggestions for improving the API or adding new features, please open an issue to discuss or submit a pull request with your changes.

## License
Please credit the author if you use this project or any of its components in your own work.

## Acknowledgments
- Libraries and frameworks used
