## Application Architecture
Using the **Hexagonal Architecture** where we can find the following directories:
- `adapter`
  - `controller`: REST Controllers
  - `persistence`: Database Interactions (in our case with Spring Data JPA)
- `application`
  - `service`: Application business logic
  - `exception`: Custom Exceptions
- `configuration`: Configuration classes
- `domain`: Domain Entities

## REST API endpoints
- `POST /api/v1/users`: Register new user ✅
- `GET /api/v1/users/{userId}`: Get user by id ✅
- `POST /api/v1/accounts`: Create new account (wallets) ✅
- `POST /api/v1/accounts/{accountId}/deposit`: Deposit money into an account ✅
- `GET /api/v1/accounts/{accountId}`: View account balance ✅
- `GET /api/v1/transactions/account/{accountId}`: View account movements (transactions) ✅
- `POST /api/v1/transactions`: Transfer money from one account to another ✅

### Accessing Swagger UI
The API documentation is available through the Swagger UI interface, allowing you to explore and interact with the available endpoints.

URL: http://localhost:8080/swagger-ui.html

Instructions: Navigate to the Swagger UI URL in your browser to view and test the available API endpoints.

## Testing
- Unit tests for the application business logic coverage 100% ✅
![coverage](src/main/resources/static/coverage.png)
- Integration tests for the controllers to simulate a successful transaction ✅

## Interact with the application
In order to simplify things a [Makefile](Makefile) was created and by running the command `make start`
that will spin up a Docker container where our latest image of our application will run on port `:8080`.
Only prerequisite is having Docker running on the background on your machine
