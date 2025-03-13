 # Legacy Bank API

A RESTful API for a banking application with user authentication, account management, transaction processing, card management, and user settings.

## Features

- JWT Authentication
- User Registration and Login
- Account Management
- Transaction Processing (deposits, withdrawals, transfers)
- Card Management
- User Settings Management (security, notifications, privacy)
- Comprehensive Error Handling
- Input Validation
- Pagination for Large Result Sets

## Technologies

- Java 17
- Spring Boot 3.1.5
- Spring Security with JWT
- Spring Data JPA
- MySQL Database
- Lombok
- OpenAPI/Swagger Documentation

## Getting Started

### Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven

### Installation

1. Clone the repository
2. Configure your MySQL database in `application.properties` (update username/password if needed)
3. Run the application:

```
mvn spring-boot:run
```

4. The API will be available at `http://localhost:8080`
5. Swagger documentation can be accessed at `http://localhost:8080/swagger-ui.html`

## API Endpoints

### Authentication

#### Login

- **URL**: `/api/auth/login`
- **Method**: POST
- **Auth required**: No
- **Payload**:
```json
{
  "username": "string",
  "password": "string"
}
```
- **Success Response**:
```json
{
  "token": "JWT_TOKEN",
  "tokenType": "Bearer",
  "username": "string"
}
```
- **Error Response**:
```json
{
  "error": "Invalid username or password"
}
```

#### Register

- **URL**: `/api/auth/register`
- **Method**: POST
- **Auth required**: No
- **Payload**:
```json
{
  "username": "string",
  "email": "string",
  "password": "string",
  "firstName": "string",
  "lastName": "string"
}
```
- **Success Response**:
```json
{
  "message": "User registered successfully",
  "userId": 1,
  "username": "string"
}
```
- **Error Response**:
```json
{
  "error": "Username is already taken"
}
```

#### Logout

- **URL**: `/api/auth/logout`
- **Method**: POST
- **Auth required**: Yes (JWT)
- **Success Response**:
```json
{
  "message": "Logged out successfully"
}
```

### Accounts

#### Get All Accounts

- **URL**: `/api/accounts`
- **Method**: GET
- **Auth required**: Yes (JWT)
- **Success Response**:
```json
[
  {
    "id": 1,
    "accountType": "SAVINGS",
    "balance": 1000.50,
    "currency": "USD",
    "status": "ACTIVE",
    "createdAt": "2023-01-01T12:00:00",
    "accountNumber": "ACC12345678"
  }
]
```

#### Get Account by ID

- **URL**: `/api/accounts/{id}`
- **Method**: GET
- **Auth required**: Yes (JWT)
- **Success Response**:
```json
{
  "id": 1,
  "accountType": "SAVINGS",
  "balance": 1000.50,
  "currency": "USD",
  "status": "ACTIVE",
  "createdAt": "2023-01-01T12:00:00",
  "accountNumber": "ACC12345678"
}
```
- **Error Response**:
```json
{
  "error": "Account not found"
}
```

#### Create Account

- **URL**: `/api/accounts`
- **Method**: POST
- **Auth required**: Yes (JWT)
- **Payload**:
```json
{
  "accountType": "SAVINGS",
  "currency": "USD"
}
```
- **Success Response**:
```json
{
  "id": 1,
  "accountType": "SAVINGS",
  "balance": 0.00,
  "currency": "USD",
  "status": "ACTIVE",
  "createdAt": "2023-01-01T12:00:00",
  "accountNumber": "ACC12345678"
}
```
- **Error Response**:
```json
{
  "error": "Invalid account type"
}
```

### Transactions

#### Deposit

- **URL**: `/api/transactions/deposit`
- **Method**: POST
- **Auth required**: Yes (JWT)
- **Payload**:
```json
{
  "accountId": 1,
  "amount": 100.50,
  "description": "Salary deposit"
}
```
- **Success Response**:
```json
{
  "message": "Deposit successful",
  "transaction": {
    "id": 1,
    "type": "DEPOSIT",
    "amount": 100.50,
    "timestamp": "2023-01-01T12:00:00",
    "status": "COMPLETED",
    "description": "Salary deposit"
  }
}
```
- **Error Response**:
```json
{
  "error": "Deposit amount must be positive"
}
```

#### Withdraw

- **URL**: `/api/transactions/withdraw`
- **Method**: POST
- **Auth required**: Yes (JWT)
- **Payload**:
```json
{
  "accountId": 1,
  "amount": 50.25,
  "description": "ATM withdrawal"
}
```
- **Success Response**:
```json
{
  "message": "Withdrawal successful",
  "transaction": {
    "id": 1,
    "type": "WITHDRAWAL",
    "amount": 50.25,
    "timestamp": "2023-01-01T12:00:00",
    "status": "COMPLETED",
    "description": "ATM withdrawal"
  }
}
```
- **Error Response**:
```json
{
  "error": "Insufficient funds"
}
```

#### Transfer

- **URL**: `/api/transactions/transfer`
- **Method**: POST
- **Auth required**: Yes (JWT)
- **Payload**:
```json
{
  "fromAccountId": 1,
  "toAccountId": 2,
  "amount": 200.00,
  "description": "Rent payment"
}
```
- **Success Response**:
```json
{
  "message": "Transfer successful",
  "transaction": {
    "id": 1,
    "type": "TRANSFER",
    "amount": 200.00,
    "fromAccount": { "id": 1 },
    "toAccount": { "id": 2 },
    "timestamp": "2023-01-01T12:00:00",
    "status": "COMPLETED",
    "description": "Rent payment"
  }
}
```
- **Error Response**:
```json
{
  "error": "Insufficient funds in source account"
}
```

#### Transaction History

- **URL**: `/api/transactions/history`
- **Method**: GET
- **Auth required**: Yes (JWT)
- **Query Parameters**:
  - `accountId` (optional): Filter by account ID
  - `type` (optional): Filter by transaction type (DEPOSIT, WITHDRAWAL, TRANSFER)
  - `status` (optional): Filter by status (COMPLETED, PENDING, FAILED)
  - `page` (optional): Page number
  - `size` (optional): Page size
- **Success Response**:
```json
{
  "content": [
    {
      "id": 1,
      "type": "DEPOSIT",
      "amount": 100.50,
      "timestamp": "2023-01-01T12:00:00",
      "status": "COMPLETED",
      "description": "Salary deposit"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

### Cards

#### Get All Cards

- **URL**: `/api/cards`
- **Method**: GET
- **Auth required**: Yes (JWT)
- **Success Response**:
```json
[
  {
    "id": 1,
    "cardType": "VISA",
    "cardNumber": "**** **** **** 1234",
    "expiryDate": "01/25",
    "cvv": "***",
    "status": "ACTIVE"
  }
]
```

#### Create Card

- **URL**: `/api/cards`
- **Method**: POST
- **Auth required**: Yes (JWT)
- **Payload**:
```json
{
  "cardType": "VISA",
  "accountId": 1
}
```
- **Success Response**:
```json
{
  "id": 1,
  "cardType": "VISA",
  "cardNumber": "**** **** **** 1234",
  "expiryDate": "01/25",
  "cvv": "***",
  "status": "ACTIVE"
}
```
- **Error Response**:
```json
{
  "error": "Account not found"
}
```

#### Delete Card

- **URL**: `/api/cards/{id}`
- **Method**: DELETE
- **Auth required**: Yes (JWT)
- **Success Response**:
```json
{
  "message": "Card successfully deleted"
}
```
- **Error Response**:
```json
{
  "error": "Card not found"
}
```

### Settings

#### Get Settings

- **URL**: `/api/settings`
- **Method**: GET
- **Auth required**: Yes (JWT)
- **Success Response**:
```json
{
  "id": 1,
  "twoFactor": false,
  "loginNotifications": false,
  "emailNotifications": true,
  "smsNotifications": false,
  "transactionNotifications": true,
  "marketingNotifications": false,
  "showBalance": true,
  "activityTracking": true,
  "dataSharing": false
}
```

#### Update Security Settings

- **URL**: `/api/settings/security`
- **Method**: PUT
- **Auth required**: Yes (JWT)
- **Payload**:
```json
{
  "twoFactor": true,
  "loginNotifications": true
}
```
- **Success Response**:
```json
{
  "id": 1,
  "twoFactor": true,
  "loginNotifications": true,
  "emailNotifications": true,
  "smsNotifications": false,
  "transactionNotifications": true,
  "marketingNotifications": false,
  "showBalance": true,
  "activityTracking": true,
  "dataSharing": false
}
```

#### Update Notification Settings

- **URL**: `/api/settings/notifications`
- **Method**: PUT
- **Auth required**: Yes (JWT)
- **Payload**:
```json
{
  "emailNotifications": true,
  "smsNotifications": true,
  "transactionNotifications": true,
  "marketingNotifications": false
}
```
- **Success Response**:
```json
{
  "id": 1,
  "twoFactor": true,
  "loginNotifications": true,
  "emailNotifications": true,
  "smsNotifications": true,
  "transactionNotifications": true,
  "marketingNotifications": false,
  "showBalance": true,
  "activityTracking": true,
  "dataSharing": false
}
```

#### Update Privacy Settings

- **URL**: `/api/settings/privacy`
- **Method**: PUT
- **Auth required**: Yes (JWT)
- **Payload**:
```json
{
  "showBalance": true,
  "activityTracking": false,
  "dataSharing": false
}
```
- **Success Response**:
```json
{
  "id": 1,
  "twoFactor": true,
  "loginNotifications": true,
  "emailNotifications": true,
  "smsNotifications": true,
  "transactionNotifications": true,
  "marketingNotifications": false,
  "showBalance": true,
  "activityTracking": false,
  "dataSharing": false
}
```

## Security

- All endpoints except `/api/auth/login` and `/api/auth/register` require JWT authentication
- JWT tokens expire after 24 hours by default (configurable in application.properties)
- Passwords are encrypted using BCrypt
- Authorization checks are implemented to ensure users can only access their own resources
- Rate limiting is implemented to prevent abuse

## Error Handling

The API returns appropriate HTTP status codes:

- 200 OK: Successful request
- 201 Created: Resource successfully created
- 400 Bad Request: Invalid request data
- 401 Unauthorized: Missing or invalid authentication
- 403 Forbidden: Authenticated but not authorized to access the resource
- 404 Not Found: Resource not found
- 500 Internal Server Error: Unexpected server error

Error responses include a descriptive message to help troubleshoot issues.
