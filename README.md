# Product Management Application Documentation

## Introduction:
The Product Management Application is a web-based system designed to manage product information using a RESTful API. It is built with Spring Boot and MongoDB.

## Architecture Overview:
The application follows a client-server architecture where the server exposes RESTful endpoints to perform CRUD operations on product data stored in a MongoDB database.

## Installation and Setup:
1. Ensure you have Java JDK and MongoDB installed on your system.
2. Clone the repository from GitHub: `git clone https://github.com/decoderrs/Backend-Java-Led-to-Rev.git`
3. Navigate to the project directory and build the application: `gradlew build`
4. Start MongoDB service.
5. Run the application: `gradlew bootRun`

## Development Tools:
- Spring-tool-suite-4-4.13.0 - For creating application and writing business logic.
- Mongo Compass - For storing product data as documents or entities.
- Post Man - For testing API endpoints and getting the expected results.

## Spring Boot Dependency:
- Spring Web
- Spring Data JPA
- Spring Data MongoDB
  
## Configuration:
- The application configuration can be found in `application.properties`.
- MongoDB connection settings can be configured in `application.properties`.
- Application Prperties:
  - `spring.data.mongodb.uri=mongodb://localhost:27017/ledToRev`
  - `server.port = 9080`

## Setting up MongoDB Database and Seeding Initial Data

This guide will walk you through the process of setting up a MongoDB database and seeding it with some initial data.

**Step 1:** Install MongoDB
- If you haven't already installed MongoDB, you can download and install it from the [MongoDB Download Center](https://www.mongodb.com/try/download/community).

**Step 2:** Create a Database
- The spring boot application automatically creates a database for collection when the application is started. 
- The database is named by taking parameter of `spring.data.mongodb.uri` uri from the application.properties file.
- `spring.data.mongodb.uri=mongodb://localhost:27017/ledToRev`, Here `ledToRev` is database name and `mongodb://localhost:27017/` is database connection url.
  
**Step 3: Create Collections and Insert Initial Data**
- The spring boot application creates a collection using "product repository interface" where collection name is given using annotation `@Document(collection= "products")`.
- The variables of the "Entity Class" defines document structure or schema.
- Start the spring boot application using command `./gradlew bootRun` on terminal. The terminal should be opened in directory where `gradle.build` file is located.
- After the spring boot application has started, open Postman, create new `POST` Request with url `http://localhost:9080/products/import-json`.
- In request body, select form option and add a `file` field and from the static folder upload the `jsondata.json` file and send the request.
- The application will process the request and create a collection which will store the json data from file in mongodb.
- After the successful insertion of data, response message will be shown as `Data imported Successfully.`
- For more information about the importing json file endpoint, refer to [API Documentation](#api-documentation) section.

**Step 4:** Verify the data
- To verify the data we can use mongodb compass or use API endpoint.
- Using API endpoint,
  - Open Postman, create new `GET` request with url `http://localhost:9080/products/all-products` and send the Request.
  - This request will return all the data we have just seeded into the database in response body.

## Important Note:
- The [static folder](https://github.com/decoderrs/Backend-Java-Led-to-Rev/tree/main/src/main/resources/static) contains the json data file (jsondata.json), which has initial data that can be seeded into the collection in Mongodb.
- The [static folder](https://github.com/decoderrs/Backend-Java-Led-to-Rev/tree/main/src/main/resources/static) also contains collection of API requests, which has request body, parameters and path variables already preset in the requests and can  be send directly after the application has started running. This collection can be imported in PostMan, and the all request can be executed at the same to get response for all the endpoints.This is done to test the API endpoints of the application. 


## Data Models:
### Product
| Attribute    | Data Type    | Description                                      |
|--------------|--------------|--------------------------------------------------|
| id           | String       | A unique identifier for the product.             |
| name         | String       | The name of the product.                         |
| description  | String       | A text description of the product.               |
| price        | Double       | The price of the product.                        |
| categories   | Array[String]| An array of categories the product belongs to.   |
| attributes   | Array[Object]| An array of key-value pairs for additional attributes such as size, color and brand |
| availability | Object       | An object containing availability information.   |
| ratings      | Array[Object]| An array of objects representing user userid,ratings and comments(optional  |

### Attributes
| Attribute    | Data Type    | Description                                      |
|--------------|--------------|--------------------------------------------------|
| size         | String       | The size of the product (e.g., small, medium, large). |
| color        | String       | The color of the product.                        |
| brand        | String       | The brand of the product.                        |

### Availability
| Attribute              | Data Type    | Description                                      |
|------------------------|--------------|--------------------------------------------------|
| inStock   | Boolean      | A boolean indicating if the product is in stock. |
| quantity  | Integer      | An integer representing the available quantity. |

### Ratings
| Attribute      | Data Type | Description                                               |
|----------------|-----------|-----------------------------------------------------------|
| userId | String    | A unique identifier for the user who gave the rating.     |
| rating | Number    | A numerical rating value.                                 |
| comment| String    | An optional text comment on the rating (if available).    |


## Database Schema:
- **Collections:**
- products
  - _id: ObjectId
  - name: String
  - description: String
  - price: Double
  - categories: Array[String]
  - attributes: Array[Object{String: String}]
    - attributes.size: String
    - attributes.color: String
    - attributes.brand: String
  - availabitily: Object
    - availability.inStock: Boolean
    - availability.quantity: Integer
  - ratings: Array[Object]
    - ratings.userId: String
    - ratings.rating: Integer
    - ratings.comment: String
  
<a name="api-documentation"></a>
## API Documentation:

### Base Url : 
`http://localhost:9080/products`

### Import JSON Data and Store in Mongodb
- **Endpoint:** POST `/import-json`
- **Description:** Retrieve a list of all products.
- **Request Body:**
  - File(Multipart file) :  jsondata.json (located in static folder)
- **Response Body:**
  ```json
  Data imported Successfully.
  ```
  
### Get All Products
- **Endpoint:** GET `/api/products?pageNumber=0&pageSize=3`
- **Description:** Retrieve a list of all products.
- **Parameters:**
  - pageNumber : Integer, specify which page to return. Here pageNumber = 0
  - pageSize : Integer, specify the number of records to return in 1 page.Here pageSize = 3
- **Response Body:**
```json
[
    {
        "id": "6627cadf4280060fab8741f5",
        "name": "Product 1",
        "description": "Description of Product 1",
        "price": 19.99,
        "categories": [
            "Category A",
            "Category B"
        ],
        "attributes": [
            {
                "size": "Small"
            },
            {
                "color": "Red"
            },
            {
                "brand": "Brand X"
            }
        ],
        "availability": {
            "inStock": true,
            "quantity": 100
        },
        "ratings": [
            {
                "userId": "user1",
                "rating": 4,
                "comment": "Good product"
            },
            {
                "userId": "user2",
                "rating": 5,
                "comment": "Excellent"
            },
            {
                "userId": "user3",
                "rating": 3,
                "comment": "Average"
            }
        ]
    },
    {
        "id": "6627cadf4280060fab8741f6",
        "name": "Product 2",
        "description": "Description of Product 2",
        "price": 29.99,
        "categories": [
            "Category B",
            "Category C"
        ],
        "attributes": [
            {
                "size": "Medium"
            },
            {
                "color": "Blue"
            },
            {
                "brand": "Brand Y"
            }
        ],
        "availability": {
            "inStock": true,
            "quantity": 50
        },
        "ratings": [
            {
                "userId": "user4",
                "rating": 5,
                "comment": "Very satisfied"
            },
            {
                "userId": "user5",
                "rating": 4,
                "comment": "Good quality"
            },
            {
                "userId": "user6",
                "rating": 4,
                "comment": "Nice product"
            }
        ]
    }
]

```
### Get Product by ID
- **Endpoint:** GET `/find-product/{productId}`
- **Description:** Retrieve a product by its ID, ex- `http://localhost:9080/products/find-product/6627cadf4280060fab8741f5`
- **Parameters:** productId: `6627cadf4280060fab8741f5`
- **Response Body:** 
```json
{
    "id": "6627cadf4280060fab8741f5",
    "name": "Product 1",
    "description": "Description of Product 1",
    "price": 19.99,
    "categories": [
        "Category A",
        "Category B"
    ],
    "attributes": [
        {
            "size": "Small"
        },
        {
            "color": "Red"
        },
        {
            "brand": "Brand X"
        }
    ],
  ....

```
### Filter products using name
- **Endpoint:** GET `/search`
- **Description:** Filter products using all the attributes of filter.
- **Request Body:**
```json
{
    "name": null,
    "categories" :["Category B","Category E"],
    "attributes" : [{"size":"Medium"},{"color":"Black"},{"brand":"Brand W"}]
}
```
- **Response Body:** 
```json
[
    {
        "id": "662834e7acfe5f46bc3880db",
        "name": "Product 4",
        "description": "Description of Product 4",
        "price": 49.99,
        "categories": [
            "Category B",
            "Category E"
        ],
        "attributes": [
            {
                "size": "Medium"
            },
            {
                "color": "Black"
            },
            {
                "brand": "Brand W"
            }
        ],
        "availability": {
            "inStock": true,
            "quantity": 20
        },
        "ratings": [
            {
                "userId": "user10",
                "rating": 4,
                "comment": "Good value"
            },
            {
                "userId": "user11",
                "rating": 5,
                "comment": "Highly recommended"
            },
            {
                "userId": "user12",
                "rating": 4,
                "comment": "Satisfied with purchase"
            }
        ]
    }
]
```

### Filter Products using Categories with Pagination
- **Endpoint:** GET `/search?pageNumber=0&pageSize=5`
- **Description:** Filter products using `categories` attribute.
- **Parameters (Optional):**
  - pageNumber : Integer, specify which page to return. Here pageNumber = 0
  - pageSize : Integer, specify the number of records to return in 1 page.Here pageSize = 5
- **Request Body:**
```json
  {
    "name": null,
    "categories" : ["Category B","Category C"],
    "attributes" : null
}
```
- **Response Body:**
```json
 [
    {
        "id": "662834e7acfe5f46bc3880d9",
        "name": "Product 2",
        "description": "Description of Product 2",
        "price": 29.99,
        "categories": [
            "Category B",
            "Category C"
        ]
      
    },
    {
        "id": "662834e7acfe5f46bc3880f6",
        "name": "Product 31",
        "description": "Description of Product 31",
        "price": 319.99,
        "categories": [
            "Category C",
            "Category F"
        ]
    },
    {
        "id": "662834e7acfe5f46bc3880dc",
        "name": "Product 5",
        "description": "Description of Product 5",
        "price": 59.99,
        "categories": [
            "Category C",
            "Category F"
        ],
        
    },
    {
        "id": "662834e7acfe5f46bc3880f3",
        "name": "Product 28",
        "description": "Description of Product 28",
        "price": 289.99,
        "categories": [
            "Category Z",
            "Category C"
        ],
      
]
```

### Filter Products using Attributes
- **Endpoint:** GET `/search`
- **Description:** Filter products using 'attributes' attribute.
- **Request Body:**
```json
  {
    "name": null,
    "categories" : null,
    "attributes" : [{"size":"Large"},{"color": "Red"},{"brand":"Brand Y"}]
}
}
```
- **Response Body:**
```json
[
    {
        "id": "662834e7acfe5f46bc3880e2",
        "name": "Product 11",
        "description": "Description of Product 11",
        "price": 119.99,
        "categories": [
            "Category I",
            "Category L"
        ],
        "attributes": [
            {
                "size": "Large"
            },
            {
                "color": "Red"
            },
            {
                "brand": "Brand Y"
            }
        ],
        "availability": {
            "inStock": true,
            "quantity": 40
        },
        "ratings": [
            {
                "userId": "user31",
                "rating": 5,
                "comment": "Excellent product"
            },
            {
                "userId": "user32",
                "rating": 4,
                "comment": "Great buy"
            },
            {
                "userId": "user33",
                "rating": 5,
                "comment": "Highly recommended"
            }
        ]
    },
    {
        "id": "662834e7acfe5f46bc3880e5",
        "name": "Product 14",
        "description": "Description of Product 14",
        "price": 149.99,
        "categories": [
            "Category L",
            "Category O"
        ],
        "attributes": [
            {
                "size": "Large"
            },
            {
                "color": "Red"
            },
            {
                "brand": "Brand Y"
            }
        ],
....
    },
    {
        "id": "662834e7acfe5f46bc3880e8",
        "name": "Product 17",
        "description": "Description of Product 17",
        "price": 179.99,
        "categories": [
            "Category O",
            "Category R"
        ],
        "attributes": [
            {
                "size": "Large"
            },
            {
                "color": "Red"
            },
            {
                "brand": "Brand Y"
            }
        ],
        ....
    },
]
```

### Add New Product
- **Endpoint:** POST `/add-product`
- **Description:** Add a new product.
- **Request Body:** 
```json
 {
    "name": "Product 34",
    "description": "Description of Product 34",
    "price": 349.99,
    "categories": ["Category F", "Category I"],
    "attributes": [
      {"size": "Small"}, {"color": "Blue"}, {"brand": "Brand X"}
    ],
    "availability": {"inStock": true, "quantity": 80},
    "ratings": [
      {"userId": "user100", "rating": 4, "comment": "Good quality"},
      {"userId": "user101", "rating": 5, "comment": "Impressive"},
      {"userId": "user102", "rating": 4, "comment": "Satisfied with purchase"}
    ]
  }
```
- **Response Body:**
```json
Product added successfully! 
com.leadtorev.product.entity.Product@b688a366
```

### Update a Product
- **Endpoint:** PUT `/updateproducts/{productId}`
- **Description:** Update a product by using productId, Ex-`/update-product/66265762335b114104f4c111`
- **Path Variable:** productId: `66265762335b114104f4c111`
- **Request Body:**
  ```json
   {
    "name": "Product 33",
    "description": "Description of Product 33",
    "price": 339.99,
    "categories": ["Category E", "Category H"],
    "attributes": [
      {"size": "Medium", "color": "Green", "brand": "Brand Z"}
    ],
    "availability": {"inStock": false, "quantity": 0},
    "ratings": [
      {"userId": "user97", "rating": 3, "comment": "Okay product"},
      {"userId": "user98", "rating": 2, "comment": "Not as expected"},
      {"userId": "user99", "rating": 1, "comment": "Disappointing"}
    ]
  }
  ```
- **Response Body:**
  ```json
  Updated Product: AcknowledgedUpdateResult{matchedCount=0, modifiedCount=1, upsertedId=null}
  ```
  
### Delete a Product
- **Endpoint:** DELETE `/delete-product/{productId}`
- **Description:** Delete a product by finding it with productId, ex-`/delete-product/66265762335b114104f4c111`
- **Path Variable:** productId: `662834e7acfe5f46bc3880e2`
- **Response Body:**
  ```json
  message :The product is deleted successfully!!
  ```
  
### Add new rating
- **Endpoint** PUT `/{productId}/add-ratings`
- **Description:** add a new rating to the ratings array for a product by finding it with productId `http://localhost:9080/products/6627cadf4280060fab8741f6/add-ratings`
-  **Path Variable** productId- `6627cadf4280060fab8741f6`
- **Request Body:**
  ```json
    {"userId": "user102", "rating": 4, "comment": "Satisfied with purchase"}
  ```
- **Response Body:**
  ```json
  {
    "id": "6627cadf4280060fab8741f6",
    "name": "Product 2",
    "description": "Description of Product 2",
    "price": 29.99,
    "categories": [
        "Category B",
        "Category C"
    ],
    "attributes": [
        {
            "size": "Medium"
        },
        {
            "color": "Blue"
        },
        {
            "brand": "Brand Y"
        }
    ],
    "availability": {
        "inStock": true,
        "quantity": 50
    },
    "ratings": [
        {
            "userId": "user4",
            "rating": 5,
            "comment": "Very satisfied"
        },
        {
            "userId": "user5",
            "rating": 4,
            "comment": "Good quality"
        },
        {
            "userId": "user6",
            "rating": 4,
            "comment": "Nice product"
        }
    ]}
  ```
  
### Change rating in a ratings object
- **Endpoint:** PUT `/{productId}/{userId}/update-rates`
- **Description:** Retrieve a product by its ID, ex- `http://localhost:9080/products/66265762335b114104f4c110/user5/update-rates?newRating=2`
- **Url Parameters:** newRating : 2
- **Path Variable:** productId: 66265762335b114104f4c110, userId: user5
- **Response Body:**
  ```json
  {
    "id": "6627cadf4280060fab8741f5",
    "name": "Product 1",
    "description": "Description of Product 1",
    "price": 19.99,
    "categories": [
        "Category A",
        "Category B"
    ],
    "attributes": [
        {
            "size": "Small"
        },
        {
            "color": "Red"
        },
        {
            "brand": "Brand X"
        }
    ],
    "availability": {
        "inStock": true,
        "quantity": 100
    },
    "ratings": [
        {
            "userId": "user1",
            "rating": 4,
            "comment": "Good product"
        },
        {
            "userId": "user2",
            "rating": 3,
            "comment": "Excellent"
        },
        {
            "userId": "user3",
            "rating": 3,
            "comment": "Average"
        }
    ]}
  ```
  
## Security:
- Authentication and authorization are not implemented in this version.

## Error Handling:
- HTTP status codes and error messages are returned for invalid requests.

## Testing:
- Currently there  are no unit and integration tests for the  application.

## Deployment:
- Deploy the application to a server with Java and MongoDB installed.

## Monitoring and Logging:
- Logging is configured using Lombok.

## Maintenance and Support:
- For bug reports or feature requests, contact the development team.

## Additional Resources:
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [MongoDB Documentation](https://docs.mongodb.com/)
