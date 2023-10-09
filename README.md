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
- `POST /users`: Register new users ✅
- `GET /users/{userId}`: Get user by id ✅
- `POST /accounts`: Create new accounts (wallets) ✅
- `POST /accounts/{accountId}/deposit`: Deposit money into an account ✅
- `GET /accounts/{accountId}`: View account balance and transactions ❌(need to added transactions)
- `POST /transactions`: Transfer money from one account to another ❌

## Testing
- Unit tests for the business logic 
- Integration tests for the controllers
