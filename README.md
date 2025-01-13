# Loan Management API

The Loan Management API is a Spring Boot application that enables users to register, log in, and manage loan transactions. The application uses MySQL as the database for storing user and loan information. Users can borrow money from lenders and repay their loans using this API.

## Features

1. **User Registration and Login**
   - New users can register by providing their details.
   - Registered users can log in to access the system.

2. **Borrowing Money**
   - Users can request loans from lenders.
   - Loan details such as amount, interest rate, and repayment terms are stored.

3. **Loan Repayment**
   - Users can repay their loans partially or fully.
   - The repayment process updates the loan status in the database.

4. **Loosely Coupled Design**
   - The service is modular and adheres to clean architecture principles, ensuring flexibility and scalability.

## Technologies Used

- **Java**: Backend programming language
- **Spring Boot**: Framework for building the application
- **MySQL**: Database for storing user and loan data
- **JPA/Hibernate**: ORM for database operations
- **Maven**: Dependency management and build tool
- **RESTful APIs**: To facilitate communication between the client and the server

## Prerequisites

- **Java 17** or later
- **MySQL Server**
- **Maven**
- A REST client (e.g., Postman) for testing the API

## Setup and Installation

1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd loan-management-api
   ```

2. **Configure MySQL Database**
   - Create a MySQL database named `loan_management`.
   - Update the `application.properties` file in the `src/main/resources` directory with your database credentials:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/loan_management
     spring.datasource.username=<your-mysql-username>
     spring.datasource.password=<your-mysql-password>
     spring.jpa.hibernate.ddl-auto=update
     ```

3. **Build the Application**
   ```bash
   mvn clean install
   ```

4. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

5. **Test the API**
   Use Postman or another REST client to interact with the endpoints. The base URL for the API is `http://localhost:8080`.
   https://www.postman.com/avionics-explorer-29622376/dmadinidvidual/collection/i2uqw94/loan-management-api?action=share&creator=29599021&active-environment=29599021-a8a16648-5b48-4e2d-90ea-dc64eeebab99

## API Endpoints

### User Management
- **Register a New User**
  - `POST /api/users/register`
  - Request Body:
    ```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "+12345678901",
  "password": "password123",
  "address": "123 Main Street, Springfield",
  "dateOfBirth": "1990-05-15"
}
    ```

- **Login**
  - `POST /api/users/login`
  - Request Body:
    ```json
    {
      "username": "john_doe",
      "password": "secure_password"
    }
    ```

### Loan Management
- **Borrow Money**
  - `POST /api/loans/borrow`
  - Request Body:
    ```json
   {
    "userId": "139fe0ab-6966-4b19-abab-8d435d4c2c87",
    "amount": 200000,
    "termMonths": 5,
    "purpose": "valentine",
    "lenderId": "b6eb60f7-0128-45d5-8843-b9d036b9e016"
}
    ```

- **Repay Loan**
  - `POST /api/loans/repay`
  - Request Body:
    ```json
{{URL}}api/v1/borrow/repay?lenderId=b6eb60f7-0128-45d5-8843-b9d036b9e016&amount=1025138.30&userId=139fe0ab-6966-4b19-abab-8d435d4c2c87
    ```

### Loan Details
- **Get Loan Details**
  - `GET /api/loans/{loanId}`

- **Get All Loans for a User**
{{URL}}api/v1/borrow/1ad12bc7-0064-4e3f-8daf-8d1393e72d5d


```

## Future Enhancements

1. Add authentication and authorization using JWT.
2. Implement advanced loan analytics and reporting.
3. Introduce notifications for due payments.
4. Enhance scalability with microservices architecture.

## Contributing

Contributions are welcome! Please fork the repository and create a pull request with your changes.

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.

## Contact

For any questions or issues, please contact:
- Name: [Your Name]
- Email: [your.email@example.com]
