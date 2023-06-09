# ebank-transaction-system  
  
## Environment
- IntelliJ IDEA
- Zookeeper on port 2181
- Kafka server on port 9092
- MySQL server on port 3306
- Java 17

## Running Procedure
1. Open a new terminal for running Zookeeper using .\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
2. Open a new terminal for running Kafka server using .\bin\windows\kafka-server-start.bat .\config\server.properties
3. Run EbankTransactionSystemApplication.java

## Database Entity Relationship Diagram  
![alt text](https://github.com/mkliac/ebank-transaction-system/blob/master/database_diagram.png?raw=true)

## System Architecture
![alt text](https://github.com/mkliac/ebank-transaction-system/blob/master/ebank_system_architecture.png?raw=true)

## API Endpoints
### Swagger UI
- Please navigate to /swagger-ui/index.html for details. Authorization is **REQUIRED** for using transaction endpoints. Bearer authorization is used and its value is the generated JWT token.   
![alt text](https://github.com/mkliac/ebank-transaction-system/blob/master/swagger_ui_view.png?raw=true))
### Authentication
- Endpoint: api/v1/auth/authenticate [**POST**]
- Description: This endpoint is used for logging in to the system. After the system successfully verifies your identity, it 
returns you a JWT token that lasts for 30 minutes. If the token is expired, you may authenticate again to get a new token.
- Request Body:  
{  
  "userId":"your user id",  
  "password":"your password"  
}
- Response Body:  
{  
"token":"jwt token"  
}
- **NOTICE**: For simplicity, the endpoint for registration is not provided. 
A user with userId **"0123456789"** and password **"1234"** is already initialized.  
- Example  
![alt text](https://github.com/mkliac/ebank-transaction-system/blob/master/authentication_example.png?raw=true))
### Get Transaction
- Endpoint: api/v1/transaction/get [**GET**]
- Description: This endpoint is used for getting a list of transaction records for different accounts.
- Request Parameters:  
{  
"year":"e.g. 2022",   
"month":"e.g. 5",  
"pageSize":"default size is 10",   
"pageNumber": "default page number is 1",   
"baseCurrency":"default base currency is HKD"  
}
- Response Body:  
{  
"transactions":[  
{  
"uuid":"uuid of transaction",  
"amount":"amount of transaction",  
"iban":"iban of the corresponding account",  
"date":"value date of transaction",  
"description":"description of transaction"  
},  
{... more other transactions ...}  
],  
"debits":"total flowing in of money in base currency",  
"credits":"total flowing out of money in base currency",  
"baseCurrency":"requested base currency"  
}
- **NOTICE**: 100 Transactions are randomly generated for each accounts(GBP, EUR, CHF) from 2022-1-1 to current date for
testing purpose. 
- Example:  
![alt text](https://github.com/mkliac/ebank-transaction-system/blob/master/get_transaction_example.png?raw=true))

## Demo  
[Demo Video Link](https://youtu.be/9rT5Io3IyvA)
