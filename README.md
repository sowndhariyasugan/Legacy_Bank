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

## Project Structure

```
src/main/java/com/Legacy/LegacyBank/
├── Config/
│   └── OpenApiConfig.java
├── Controller/
│   ├── AuthController.java
│   ├── UserController.java
│   ├── AccountController.java
│   ├── TransactionController.java
│   ├── CardController.java
│   └── SettingsController.java
├── Model/
│   ├── User.java
│   ├── Account.java
│   ├── Transaction.java
│   ├── Card.java
│   ├── Settings.java
│   ├── Notification.java
│   ├── TransactionType.java
│   ├── TransactionStatus.java
│   ├── AccountType.java
│   ├── ERole.java
│   └── Role.java
├── Repository/
│   ├── UserRepository.java
│   ├── AccountRepository.java
│   ├── TransactionRepository.java
│   ├── CardRepository.java
│   ├── SettingsRepository.java
│   ├── NotificationRepository.java
│   └── RoleRepository.java
├── Service/
│   ├── UserService.java
│   ├── UserServiceImpl.java
│   ├── AccountService.java
│   ├── AccountServiceImpl.java
│   ├── TransactionService.java
│   ├── TransactionServiceImpl.java
│   ├── NotificationService.java
│   ├── JwtService.java
│   ├── UserDetailsImpl.java
│   └── UserDetailsServiceImpl.java
└── Security/
    ├── WebSecurityConfig.java
    └── Jwt/
        ├── AuthTokenFilter.java
        ├── AuthEntryPointJwt.java
        └── JwtUtils.java
```

## Getting Started

### Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven

### Installation

1. Clone the repository
2. Configure your MySQL database in `application.properties` (update username/password if needed)
3. Run the application:

```bash
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

### Users

#### Get All Users

- **URL**: `/api/users`
- **Method**: GET
- **Auth required**: Yes (JWT)
- **Success Response**:

```json
[
  {
    "id": 1,
    "username": "johndoe",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "phone": "+1234567890",
    "profileImage": "profile.jpg",
    "accounts": [
      {
        "id": 1,
        "accountType": "SAVINGS",
        "accountNumber": "ACC12345678",
        "balance": 1000.5,
        "currency": "USD",
        "status": "ACTIVE"
      }
    ]
  }
]
```

#### Get User by ID

- **URL**: `/api/users/{id}`
- **Method**: GET
- **Auth required**: Yes (JWT)
- **Success Response**:

```json
{
  "id": 1,
  "username": "johndoe",
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "+1234567890",
  "profileImage": "profile.jpg",
  "accounts": [
    {
      "id": 1,
      "accountType": "SAVINGS",
      "accountNumber": "ACC12345678",
      "balance": 1000.5,
      "currency": "USD",
      "status": "ACTIVE"
    }
  ]
}
```

- **Error Response**:

```json
{
  "error": "User not found"
}
```

#### Update User

- **URL**: `/api/users/{id}`
- **Method**: PUT
- **Auth required**: Yes (JWT)
- **Payload**:

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "phone": "+1234567890",
  "profileImage": "profile.jpg"
}
```

- **Success Response**:

```json
{
  "id": 1,
  "username": "johndoe",
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "+1234567890",
  "profileImage": "profile.jpg"
}
```

- **Error Response**:

```json
{
  "error": "User not found"
}
```

#### Delete User

- **URL**: `/api/users/{id}`
- **Method**: DELETE
- **Auth required**: Yes (JWT)
- **Success Response**: 204 No Content
- **Error Response**:

```json
{
  "error": "User not found"
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
    "accountNumber": "ACC12345678",
    "balance": 1000.5,
    "currency": "USD",
    "status": "ACTIVE",
    "user": {
      "id": 1,
      "username": "johndoe",
      "firstName": "John",
      "lastName": "Doe"
    }
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
  "balance": 1000.5,
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
  "accountNumber": "ACC12345678",
  "balance": 0.0,
  "currency": "USD",
  "status": "ACTIVE",
  "user": {
    "id": 1,
    "username": "johndoe",
    "firstName": "John",
    "lastName": "Doe"
  }
}
```

- **Error Response**:

```json
{
  "error": "Invalid account type"
}
```

#### Get User Accounts

- **URL**: `/api/accounts/user/{userId}`
- **Method**: GET
- **Auth required**: Yes (JWT)

#### Update Account

- **URL**: `/api/accounts/{id}`
- **Method**: PUT
- **Auth required**: Yes (JWT)
- **Payload**:

```json
{
  "status": "ACTIVE"
}
```

- **Success Response**:

```json
{
  "id": 1,
  "accountType": "SAVINGS",
  "balance": 1000.5,
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

#### Delete Account

- **URL**: `/api/accounts/{id}`
- **Method**: DELETE
- **Auth required**: Yes (JWT)
- **Success Response**: 204 No Content
- **Error Response**:

```json
{
  "error": "Account not found"
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
  "amount": 100.5,
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
    "amount": 100.5,
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

#### Transfer Between Users

- **URL**: `/api/transactions/transfer`
- **Method**: POST
- **Auth required**: Yes (JWT)
- **Payload**:

```json
{
  "fromAccountId": 1,
  "toAccountId": 2,
  "amount": 200.0,
  "description": "Rent payment"
}
```

- **Success Response**:

```json
{
  "id": 1,
  "type": "TRANSFER",
  "amount": 200.0,
  "fromAccount": {
    "id": 1,
    "accountNumber": "ACC12345678",
    "user": {
      "id": 1,
      "username": "johndoe",
      "firstName": "John",
      "lastName": "Doe"
    }
  },
  "toAccount": {
    "id": 2,
    "accountNumber": "ACC87654321",
    "user": {
      "id": 2,
      "username": "janesmith",
      "firstName": "Jane",
      "lastName": "Smith"
    }
  },
  "status": "COMPLETED",
  "timestamp": "2024-03-21T10:00:00",
  "description": "Rent payment"
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
      "type": "TRANSFER",
      "amount": 200.0,
      "fromAccount": {
        "id": 1,
        "accountNumber": "ACC12345678",
        "user": {
          "id": 1,
          "username": "johndoe",
          "firstName": "John",
          "lastName": "Doe"
        }
      },
      "toAccount": {
        "id": 2,
        "accountNumber": "ACC87654321",
        "user": {
          "id": 2,
          "username": "janesmith",
          "firstName": "Jane",
          "lastName": "Smith"
        }
      },
      "status": "COMPLETED",
      "timestamp": "2024-03-21T10:00:00",
      "description": "Rent payment"
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
    "status": "ACTIVE",
    "user": {
      "id": 1,
      "username": "johndoe",
      "firstName": "John",
      "lastName": "Doe"
    },
    "account": {
      "id": 1,
      "accountType": "SAVINGS",
      "accountNumber": "ACC12345678",
      "balance": 1000.5
    }
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
  "status": "ACTIVE",
  "user": {
    "id": 1,
    "username": "johndoe",
    "firstName": "John",
    "lastName": "Doe"
  },
  "account": {
    "id": 1,
    "accountType": "SAVINGS",
    "accountNumber": "ACC12345678",
    "balance": 1000.5
  }
}
```

- **Error Response**:

```json
{
  "error": "Account not found"
}
```

#### Get User Cards

- **URL**: `/api/cards/user/{userId}`
- **Method**: GET
- **Auth required**: Yes (JWT)

#### Get Card Details

- **URL**: `/api/cards/{id}`
- **Method**: GET
- **Auth required**: Yes (JWT)

#### Update Card

- **URL**: `/api/cards/{id}`
- **Method**: PUT
- **Auth required**: Yes (JWT)
- **Payload**:

```json
{
  "status": "ACTIVE"
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
  "status": "ACTIVE",
  "user": {
    "id": 1,
    "username": "johndoe",
    "firstName": "John",
    "lastName": "Doe"
  },
  "account": {
    "id": 1,
    "accountType": "SAVINGS",
    "accountNumber": "ACC12345678",
    "balance": 1000.5
  }
}
```

- **Error Response**:

```json
{
  "error": "Card not found"
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

#### Get User Settings

- **URL**: `/api/settings/user/{userId}`
- **Method**: GET
- **Auth required**: Yes (JWT)

#### Update User Settings

- **URL**: `/api/settings/{id}`
- **Method**: PUT
- **Auth required**: Yes (JWT)
- **Payload**:

```json
{
  "emailNotifications": true,
  "smsNotifications": true,
  "twoFactorAuth": true,
  "language": "EN",
  "timezone": "UTC"
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

## Data Relationships

### User-Account Relationship

- Each user can have multiple accounts
- Each account belongs to exactly one user
- When creating an account, it is automatically associated with the authenticated user

### User-Card Relationship

- Each user can have multiple cards
- Each card belongs to exactly one user
- Each card is associated with one account
- When creating a card, it is automatically associated with the authenticated user and the specified account

### Transaction Relationships

- Each transaction involves at least one account
- For transfers, a transaction involves two accounts (from and to)
- Transactions maintain references to both the accounts and their associated users
- Transaction history can be filtered by account, user, or transaction type

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

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Account and Card Management

### Account Linking

- Each account is automatically linked to the user who creates it
- Users can have multiple accounts of different types (SAVINGS, CHECKING, etc.)
- Each account has a unique account number
- Accounts maintain their own balance and currency

### Card Linking

- Cards are linked to both a user and an account
- Each card belongs to exactly one user and one account
- Cards cannot be created for accounts that don't belong to the user
- Cards have security features like CVV and expiry date
- Card numbers are automatically generated and masked in responses

### Transfer Functionality

The API supports transfers between accounts with the following features:

1. **Same-User Transfers**

   - Transfer between accounts owned by the same user
   - Example: Transfer from savings to checking account
   - Requires authentication and account ownership verification

2. **Different-User Transfers**
   - Transfer between accounts owned by different users
   - Example: Transfer to another user's account
   - Requires authentication and account ownership verification
   - Both accounts must have the same currency

#### Transfer Implementation

The transfer functionality is implemented through a two-layer service architecture:

1. **AccountService Layer**

   - Handles high-level transfer authorization
   - Verifies account ownership
   - Delegates the actual transfer to TransactionService

2. **TransactionService Layer**
   - Handles the actual transfer logic
   - Updates account balances
   - Creates transaction records
   - Performs currency validation
   - Ensures atomicity of the operation

#### Transfer Rules and Validations

- Source account must belong to the authenticated user
- Both accounts must have the same currency
- Source account must have sufficient funds
- Transfers are atomic (either both accounts are updated or neither is)
- Transfers cannot be made between accounts with different currencies
- Transfers cannot be made to the same account
- All transfers are recorded in the transaction history
- Transfer amounts must be positive

#### Transfer Process

1. **Authorization Check**

   - Verify user authentication
   - Verify account ownership
   - Check account existence

2. **Validation**

   - Validate transfer amount
   - Check currency compatibility
   - Verify sufficient funds
   - Validate account status

3. **Execution**

   - Update source account balance
   - Update destination account balance
   - Create transaction record
   - All operations are wrapped in a transaction

4. **Response**
   - Return transaction details
   - Include updated account information
   - Provide transaction status

### Security Measures

- All card operations require authentication
- Card details are masked in responses
- Users can only access their own accounts and cards
- Transfer operations verify account ownership
- Currency validation for transfers
- Balance checks before transfers
