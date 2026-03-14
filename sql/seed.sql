USE expense_tracker;

INSERT INTO Transaction (title, amount, category, date, type) VALUES
      ('Salary', 4000, 'Job', '2025-03-01', 'INCOME'),
      ('Freelance', 800, 'Work', '2025-03-10', 'INCOME'),
      ('Groceries', 150, 'Food', '2025-03-12', 'EXPENSE'),
      ('Netflix', 30, 'Entertainment', '2025-03-15', 'EXPENSE'),
      ('Transport', 50, 'Transport', '2025-03-16', 'EXPENSE');