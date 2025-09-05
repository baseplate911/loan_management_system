-- === File: sql/schema_fixed.sql ===
CREATE DATABASE IF NOT EXISTS loan_system;
USE loan_system;

CREATE TABLE IF NOT EXISTS customers (
  id INT AUTO_INCREMENT PRIMARY KEY,
  full_name VARCHAR(150) NOT NULL,
  email VARCHAR(120),
  phone VARCHAR(20),
  aadhar VARCHAR(20),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS loans (
  id INT AUTO_INCREMENT PRIMARY KEY,
  customer_id INT NOT NULL,
  principal DECIMAL(12,2) NOT NULL,
  annual_rate DECIMAL(5,2) NOT NULL,
  term_months INT NOT NULL,
  disburse_date DATE NOT NULL,
  status ENUM('PENDING','APPROVED','REJECTED','CLOSED','DEFAULTED') DEFAULT 'PENDING',
  emi_amount DECIMAL(12,2),
  outstanding_principal DECIMAL(12,2),
  last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS repayments (
  id INT AUTO_INCREMENT PRIMARY KEY,
  loan_id INT NOT NULL,
  payment_date DATE NOT NULL,
  amount DECIMAL(12,2) NOT NULL,
  principal_paid DECIMAL(12,2),
  interest_paid DECIMAL(12,2),
  payment_mode VARCHAR(50),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (loan_id) REFERENCES loans(id) ON DELETE CASCADE
);

CREATE INDEX idx_loans_status ON loans(status);
CREATE INDEX idx_repayments_loan ON repayments(loan_id);

-- sample data
INSERT INTO customers (full_name, email, phone, aadhar) VALUES
('Test User', 'test@example.com', '9876543210', '123412341234');

INSERT INTO loans (customer_id, principal, annual_rate, term_months, disburse_date, status)
VALUES (1, 50000, 10.5, 24, CURDATE(), 'PENDING');
