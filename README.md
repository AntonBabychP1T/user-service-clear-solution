# UserService Spring Boot Project

## Introduction

This project implements a RESTful API for managing users in a Spring Boot application. The API supports creating, updating, deleting, and searching for users based on their birthdate. The implementation adheres to best practices in REST API design and includes error handling, validation, and comprehensive unit testing. The project goes beyond basic requirements by integrating database persistence and Docker support for improved development and deployment.

## Key Features

- CRUD Operations: Create, update, and delete users.
- Validation: Validates user inputs like email pattern and age.
- Search Functionality: Enables searching for users within a birth date range.
- Error Handling: Proper RESTful error responses.
- Extended Functionality: Includes database integration and Docker setup.

## Getting Started

These instructions will get your copy of the project up and running on your local machine for development and testing purposes.

### Installation

1. Clone the repository:
```git clone https://github.com/AntonBabychP1T/user-service-clear-solution```

2. Ensure Docker is running on your machine.

3. Build the project:
```./gradlew clean build```

4. Create a `.env` file in the root directory of the project and add the following environment variables:
```env
POSTGRESQL_USER=username
POSTGRESQL_ROOT_PASSWORD=password
POSTGRESQL_PASSWORD=password
POSTGRESQL_DATABASE=database_name
POSTGRESQL_LOCAL_PORT=5433
POSTGRESQL_DOCKER_PORT=5432
SPRING_LOCAL_PORT=8081
SPRING_DOCKER_PORT=8080
DEBUG_PORT=5005
```

### Running the Application

Run the application using Docker Compose:
```docker-compose up --build```

## User Controller

Endpoints for managing users. Detailed documentation is available via Swagger UI.

- **POST /api/users** - Create a new user.
  
  *Example of request body:*
  
  ```json
  {
    "firstName": "John",
    "lastName": "Doe",
    "birthDate": "1990-01-01",
    "email": "john.doe@example.com",
    "address": "123 Elm Street",
    "phoneNumber": "555-1234"
  }
  
- **PATCH /api/users/{id}** - Update specific fields for a user.

  *Example of request body:*

  ```json
  {
  "firstName": "Jane",
  "lastName": "Doe",
  "birthDate": "1991-02-01",
  "email": "jane.doe@example.com",
  "address": "123 Elm Street",
  "phoneNumber": "555-6789"
  }
  ```
- **PUT /api/users/{id}** - Completely update all fields for a user.
  
  *Example of request body:*

  ```json
  {
  "firstName": "Jane",
  "lastName": "Doe",
  "birthDate": "1991-02-01",
  "email": "jane.doe@example.com",
  "address": "123 Elm Street",
  "phoneNumber": "555-6789"
  }
  ```

- **DELETE /api/users/{id}** - Delete a user by ID.
  
- **GET /api/users/{id}** - Find a user by their unique identifier.
  
- **GET /api/users/search** - Find users whose birth date falls within a specified range.

*Example of request parameters:*

```
from=1990-01-01&to=1995-01-01
```






 
