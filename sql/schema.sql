CREATE DATABASE expense_tracker;
USE expense_tracker;

CREATE TABLE Transaction (
     id INT AUTO_INCREMENT PRIMARY KEY,
     title VARCHAR(100) NOT NULL,
     amount DOUBLE NOT NULL,
     category VARCHAR(50) NOT NULL,
     date DATE NOT NULL,
     type VARCHAR(20) NOT NULL
);