# Loan Management System 

This is a **console-based Loan Management System** built with **Java, JDBC, and MySQL**.  
It started as a way to practice database design and JDBC, but turned into a mini project that covers the **entire loan lifecycle**: from customer registration to loan approval, EMI calculation, repayments, and reporting.

---

##  What it does
-  **Register customers** with basic details  
-  **Apply for loans** (principal, interest rate, term)  
-  **Admin approves or rejects** loan requests  
-  **Calculate EMIs** and split into principal + interest  
-  **Record repayments** and update outstanding balance automatically  
-  **Generate reports** for total loans, collections, and defaults  
-  Supports full **CRUD operations** (Create, Read, Update, Delete)  

---

##  How it’s built
- **Java (8+)** → main application logic  
- **JDBC** → database interaction  
- **MySQL** → backend database  
- **SQL** → schema, indexes, and transactions  

---

##  Project structure
loan-management-system/
├─ lib/ # MySQL connector jar
├─ sql/
│ └─ schema.sql # Database schema + sample data
├─ src/com/example/loan/
│ ├─ model/ # Customer, Loan, Repayment classes
│ ├─ dao/ # DAO classes for database operations
│ ├─ service/ # Business logic (EMI, repayments, reports)
│ └─ App.java # Console menu (main entry point)
└─ out/ # Compiled class files


---

##  Getting started
1. Clone the repo:
   ```bash
   git clone https://github.com/baseplate911/loan-management-system.git
   cd loan-management-system
Download MySQL Connector/J
 and drop the .jar file into the lib/ folder.

Create the database and tables:

mysql -u root -p < sql/schema.sql


Update DBConnection.java with your MySQL username & password.

Compile & run:

mkdir out
javac -cp "lib/mysql-connector-j-8.0.xx.jar;." -d out -sourcepath src src/com/example/loan/App.java
java -cp "lib/mysql-connector-j-8.0.xx.jar;out" com.example.loan.App

 Demo workflow

Register a customer

Apply for a loan

Admin approves → EMI is calculated

Make a repayment → outstanding balance updates

View reports → track collections & defaults

 Future ideas

Web interface (Spring Boot + React)

Authentication & role-based access (Admin/Customer)

Analytics dashboard with Power BI

 License

MIT License. Free to use, learn, and improve!

💡 This project helped me practice Java + SQL integration and understand how financial systems handle transactions. If you find it useful or want to suggest improvements, feel free to open an issue or fork it!
