package GUI;

import BusinessLogic.TransactionBLL;
import Model.Transaction;

import javax.swing.table.DefaultTableModel;
import java.sql.Date;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;

public class Controller {

    private final TransactionView view;
    private final TransactionBLL bll;

    public Controller(TransactionView view) {
        this.view = view;
        this.bll = new TransactionBLL();
        this.view.getSearchButton().addActionListener(e -> searchTransaction());
        this.view.getAllButton().addActionListener(e -> loadTable());
        this.view.getIncomeButton().addActionListener(e -> filterByType("INCOME"));
        this.view.getExpenseButton().addActionListener(e -> filterByType("EXPENSE"));
        this.view.getExportButton().addActionListener(e -> exportCSV());

        loadTable();

        this.view.getAddButton().addActionListener(e -> addTransaction());
        this.view.getUpdateButton().addActionListener(e -> updateTransaction());
        this.view.getDeleteButton().addActionListener(e -> deleteTransaction());
        this.view.getRefreshButton().addActionListener(e -> loadTable());
        this.view.getTransactionTable().getSelectionModel().addListSelectionListener(e -> {
            int row = view.getTransactionTable().getSelectedRow();

            if (row != -1) {
                view.getIdField().setText(view.getTransactionTable().getValueAt(row, 0).toString());
                view.getTitleField().setText(view.getTransactionTable().getValueAt(row, 1).toString());
                view.getAmountField().setText(view.getTransactionTable().getValueAt(row, 2).toString());
                view.getCategoryField().setText(view.getTransactionTable().getValueAt(row, 3).toString());
                view.getDateField().setText(view.getTransactionTable().getValueAt(row, 4).toString());
                view.getTypeComboBox().setSelectedItem(view.getTransactionTable().getValueAt(row, 5).toString());
            }
        });
    }

    private void loadTable() {
        List<Transaction> transactions = bll.findAll();
        DefaultTableModel model = bll.getDAO().buildTableFromList(transactions);
        view.getTransactionTable().setModel(model);
        updateTotals(transactions);
    }

    private void addTransaction() {
        try {
            String title = view.getTitleField().getText();
            double amount = Double.parseDouble(view.getAmountField().getText());
            String category = view.getCategoryField().getText();
            Date date = Date.valueOf(view.getDateField().getText());
            String type = (String) view.getTypeComboBox().getSelectedItem();

            Transaction transaction = new Transaction(title, amount, category, date, type);
            bll.insertTransaction(transaction);

            view.showMessage("Transaction added successfully!");
            clearFields();
            loadTable();

        } catch (Exception ex) {
            view.showMessage("Invalid input data!");
        }
    }

    private void updateTransaction() {
        try {
            int id = Integer.parseInt(view.getIdField().getText());
            String title = view.getTitleField().getText();
            double amount = Double.parseDouble(view.getAmountField().getText());
            String category = view.getCategoryField().getText();
            Date date = Date.valueOf(view.getDateField().getText());
            String type = (String) view.getTypeComboBox().getSelectedItem();

            Transaction transaction = new Transaction(id, title, amount, category, date, type);
            bll.updateTransaction(transaction);

            view.showMessage("Transaction updated successfully!");
            clearFields();
            loadTable();

        } catch (Exception ex) {
            view.showMessage("Invalid input data!");
        }
    }

    private void deleteTransaction() {
        try {
            int id = Integer.parseInt(view.getIdField().getText());
            bll.deleteTransaction(id);

            view.showMessage("Transaction deleted successfully!");
            clearFields();
            loadTable();

        } catch (Exception ex) {
            view.showMessage("Invalid ID!");
        }
    }

    private void searchTransaction() {
        String title = view.getSearchField().getText();

        if (title.isEmpty()) {
            loadTable();
            return;
        }

        List<Transaction> transactions = bll.findTransactionsByTitle(title);
        DefaultTableModel model = bll.getDAO().buildTableFromList(transactions);
        view.getTransactionTable().setModel(model);
        updateTotals(transactions);

        if (transactions.isEmpty()) {
            view.showMessage("No transactions found.");
        }
    }

    private void filterByType(String type) {
        List<Transaction> transactions = bll.findTransactionsByType(type);
        DefaultTableModel model = bll.getDAO().buildTableFromList(transactions);
        view.getTransactionTable().setModel(model);
        updateTotals(transactions);

        if (transactions.isEmpty()) {
            view.showMessage("No transactions found for type: " + type);
        }
    }

    private void updateTotals(List<Transaction> transactions) {
        double totalIncome = 0;
        double totalExpense = 0;

        for (Transaction transaction : transactions) {
            if ("INCOME".equalsIgnoreCase(transaction.getType())) {
                totalIncome += transaction.getAmount();
            } else if ("EXPENSE".equalsIgnoreCase(transaction.getType())) {
                totalExpense += transaction.getAmount();
            }
        }

        double balance = totalIncome - totalExpense;

        view.getTotalIncomeLabel().setText("Total Income: " + totalIncome);
        view.getTotalExpenseLabel().setText("Total Expense: " + totalExpense);
        view.getBalanceLabel().setText("Balance: " + balance);
    }

    private void exportCSV() {
        List<Transaction> transactions = bll.findAll();

        try (FileWriter writer = new FileWriter("transactions.csv")) {
            writer.write("ID,Title,Amount,Category,Date,Type\n");

            for (Transaction transaction : transactions) {
                writer.write(transaction.getId() + ","
                        + transaction.getTitle() + ","
                        + transaction.getAmount() + ","
                        + transaction.getCategory() + ","
                        + transaction.getDate() + ","
                        + transaction.getType() + "\n");
            }

            view.showMessage("CSV exported successfully! File name: transactions.csv");

        } catch (IOException e) {
            view.showMessage("Error exporting CSV!");
            e.printStackTrace();
        }
    }

    private void clearFields() {
        view.getIdField().setText("");
        view.getTitleField().setText("");
        view.getAmountField().setText("");
        view.getCategoryField().setText("");
        view.getDateField().setText("");
        view.getTypeComboBox().setSelectedIndex(0);
    }
}