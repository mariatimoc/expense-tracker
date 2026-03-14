# Expense Tracker App (Java Swing + JDBC + MySQL)

A small desktop application for managing **personal income and expenses** using a layered architecture.  
Users can add, update, delete, search, and filter transactions, view financial summaries, and export data to CSV.

---

## Features

- **Transactions**
  - Add / Edit / Delete transactions
  - Each transaction contains: title, amount, category, date, and type

- **Search**
  - Search transactions by title

- **Filtering**
  - View only **INCOME** transactions
  - View only **EXPENSE** transactions
  - Show all transactions

- **Financial Summary**
  - Displays:
    - Total Income
    - Total Expense
    - Balance (Income − Expense)

- **Export**
  - Export all transactions to a **CSV file**

---

## Technologies

- **Java**
- **Swing** (GUI)
- **JDBC**
- **MySQL**
- **Maven**

---

## Project Structure

- `GUI` – user interface (views + controller)
- `BusinessLogic` – application logic
- `DataAccess` – DAO layer (generic DAO + specific DAO)
- `Model` – entity classes
- `Connection` – database connection factory

---

## Database Setup (MySQL)

1. Create the database schema and tables:
   - Run `sql/schema.sql` in MySQL Workbench

2. (Optional) Insert demo data:
   - Run `sql/seed.sql`

---

## Configuration

This project reads DB credentials from a local config file.

1. Copy:
src/main/resources/db.example.properties → src/main/resources/db.properties


2. Edit `db.properties` with your MySQL settings:

```properties
db.url=jdbc:mysql://localhost:3306/expense_tracker
db.user=root
db.pass=YOUR_PASSWORD