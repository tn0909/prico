# Price Comparison RESTful API

This project aims to develop a RESTful APIs for a price comparison website. The API allows users to search for products and compare prices from various stores.

## Features

- **Product Search**: Search for products by name, category, brand, etc.
- **Price Comparison**: Compare prices for the same product from different stores.
- **Admin Operations**: CRUD operations for managing products and stores.

## Technologies Used

- **Java**: Programming language for backend development
- **Spring Boot**: Framework for building RESTful APIs
- **MySQL**: Database management system for storing product and store information
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

# Endpoints for Product Search
```
# Request
POST    ​/products/search
{
  "brand": "Yoplait",
  "category": "Yoghurt",
  "name": "mango"
}

# Response
[
  {
    "id": 5,
    "name": "Yoplait Mango Yoghurt | 1kg",
    "description": "Yoplait Mango Yoghurt | 1kg",
    "imageUrl": null,
    "category": {
      "id": 2,
      "name": "Frozen Yoghurt",
      "description": "Frozen Yoghurt"
    },
    "brand": {
      "id": 1,
      "name": "Yoplait",
      "description": "Yoplait"
    }
  }
]
```

# Endpoints for get variations of a product
```
# Request
GET    ​/products/{id}/variations

# Response
{
  "productId": 5,
  "productName": "Yoplait Mango Yoghurt | 1kg",
  "productImageUrl": null,
  "stores": [
    {
      "id": 2,
      "name": "Coles",
      "website": "coles.com.au",
      "variations": [
        {
          "id": 2,
          "name": "Yoplait Mango Yoghurt | 1kg",
          "url": "https://www.coles.com.au/product/yoplait-mango-yoghurt-1kg-5892512",
          "imageUrl": "https://shop.coles.com.au/wcsstore/Coles-CAS/images/5/8/9/5892512-zm.jpg",
          "price": null
        }
      ]
    },
    {
      "id": 1,
      "name": "Woolworths",
      "website": "woolworths.com.au",
      "variations": [
        {
          "id": 1,
          "name": "Yoplait Mango Yoghurt 1kg",
          "url": "https://www.woolworths.com.au/shop/productdetails/253784/yoplait-mango-yoghurt",
          "imageUrl": "https://cdn0.woolworths.media/content/wowproductimages/large/253784.jpg",
          "price": null
        }
      ]
    }
  ]
}

```

# Endpoints for CRUD operations on Products

1. Get all products
```
# Request
GET    ​/products

# Response
[
  {
    "id": 1,
    "name": "yarnart jeans",
    "description": "Cotton & acrylic yarns for amigurumi",
    "imageUrl": "https://prico.com/images/yarnart-jeans.jpg",
    "category": {
        "id": 3,
        "name": "Yarn",
        "description": "Yarn for handmade purpose"
    },
    "brand": {
      "id": 2,
      "name": "YarnArts",
      "description": "Turkish yarn producer"
    }
  },
  {
    "id": 5,
    "name": "Yoplait Mango Yoghurt | 1kg",
    "description": "Yoplait Mango Yoghurt | 1kg",
    "imageUrl": "https://prico.com/images/yoplait-yoghurt.jpg",
    "category": {
      "id": 2,
      "name": "Frozen Yoghurt",
      "description": "Frozen Yoghurt"
    },
    "brand": {
      "id": 1,
      "name": "Yoplait",
      "description": "Yoplait"
    }
  }
]
```

2. Add a product
```
# Request
POST    ​/products
{
  "brandId": 1,
  "categoryId": 1,
  "name": "Toffee Honeycomb Twist Yoghurt 160g",
  "description": "Sweet honeycomb with a rich toffee twist, this tub of gold will delight the soul.",
  "imageUrl": "https://prico.com/images/twist-yoghurt.jpg",
}

# Response
{
  "message": "Product has been created successfully"
}
```

3. Get a product
```
# Request
GET    ​/products/{id}

# Response
{
  "id": 1,
  "name": "yarnart jeans",
  "description": "Cotton & acrylic yarns for amigurumi",
  "imageUrl": "https://prico.com/images/yarnart-jeans.jpg",
  "category": {
      "id": 3,
      "name": "Yarn",
      "description": "Yarn for handmade purpose"
  },
  "brand": {
    "id": 2,
    "name": "YarnArts",
    "description": "Turkish yarn producer"
  }
}
```

4. Update a product
```
# Request
PUT    ​/products/{id}
{
  "brandId": 1,
  "categoryId": 1,
  "name": "Yoplait Yoghurt Mixed Berry | 1kg",
  "description": "Made in Australia and available in 4 delicious flavours including Vanilla, Strawberry, Mango and Passionfruit.",
  "imageUrl": "https://prico.com/images/mixed-yoghurt.jpg",
}

# Response
{
  "message": "Product has been updated successfully"
}
```

5. Delete a product
```
# Request
DELETE    ​/products/{id}

# Response
{
  "message": "Product has been deleted successfully"
}
```

## Contributing

Contributions are welcome! If you have suggestions for improving the API or adding new features, please open an issue to discuss or submit a pull request with your changes.

## License

Please credit the author if you use this project or any of its components in your own work.

## Acknowledgments

Libraries and frameworks used
