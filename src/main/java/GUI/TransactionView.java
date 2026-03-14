package GUI;

import javax.swing.*;
import java.awt.*;

public class TransactionView extends JFrame {

    private JTextField titleField;
    private JTextField amountField;
    private JTextField categoryField;
    private JTextField dateField;
    private JComboBox<String> typeComboBox;
    private JTextField searchField;
    private JButton searchButton;

    private JTextField idField;

    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JButton allButton;
    private JButton incomeButton;
    private JButton expenseButton;
    private JButton exportButton;

    private JLabel totalIncomeLabel;
    private JLabel totalExpenseLabel;
    private JLabel balanceLabel;

    private JTable transactionTable;

    public TransactionView() {
        setTitle("Expense Tracker");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        formPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        idField.setEditable(false);
        formPanel.add(idField);

        formPanel.add(new JLabel("Title:"));
        titleField = new JTextField();
        formPanel.add(titleField);

        formPanel.add(new JLabel("Amount:"));
        amountField = new JTextField();
        formPanel.add(amountField);

        formPanel.add(new JLabel("Category:"));
        categoryField = new JTextField();
        formPanel.add(categoryField);

        formPanel.add(new JLabel("Date (yyyy-mm-dd):"));
        dateField = new JTextField();
        formPanel.add(dateField);

        formPanel.add(new JLabel("Type:"));
        typeComboBox = new JComboBox<>(new String[]{"INCOME", "EXPENSE"});
        formPanel.add(typeComboBox);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        refreshButton = new JButton("Refresh");
        exportButton = new JButton("Export CSV");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(exportButton);

        transactionTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(transactionTable);

        JPanel searchPanel = new JPanel(new FlowLayout());

        searchPanel.add(new JLabel("Search by title:"));
        searchField = new JTextField(20);
        searchButton = new JButton("Search");

        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        JPanel filterPanel = new JPanel(new FlowLayout());

        allButton = new JButton("Show All");
        incomeButton = new JButton("Show Income");
        expenseButton = new JButton("Show Expense");


        filterPanel.add(allButton);
        filterPanel.add(incomeButton);
        filterPanel.add(expenseButton);


        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.NORTH);

        JPanel middlePanel = new JPanel(new BorderLayout());
        middlePanel.add(searchPanel, BorderLayout.NORTH);
        middlePanel.add(filterPanel, BorderLayout.SOUTH);

        topPanel.add(middlePanel, BorderLayout.SOUTH);

        JPanel totalsPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        totalIncomeLabel = new JLabel("Total Income: 0");
        totalExpenseLabel = new JLabel("Total Expense: 0");
        balanceLabel = new JLabel("Balance: 0");

        totalsPanel.add(totalIncomeLabel);
        totalsPanel.add(totalExpenseLabel);
        totalsPanel.add(balanceLabel);


        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(buttonPanel, BorderLayout.NORTH);
        southPanel.add(totalsPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    public JTextField getTitleField() {
        return titleField;
    }

    public JTextField getAmountField() {
        return amountField;
    }

    public JTextField getCategoryField() {
        return categoryField;
    }

    public JTextField getDateField() {
        return dateField;
    }

    public JComboBox<String> getTypeComboBox() {
        return typeComboBox;
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    public JTextField getIdField() {
        return idField;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getUpdateButton() {
        return updateButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JButton getRefreshButton() {
        return refreshButton;
    }

    public JTable getTransactionTable() {
        return transactionTable;
    }

    public JButton getAllButton() {
        return allButton;
    }

    public JButton getIncomeButton() {
        return incomeButton;
    }

    public JButton getExpenseButton() {
        return expenseButton;
    }

    public JLabel getTotalIncomeLabel() {
        return totalIncomeLabel;
    }

    public JLabel getTotalExpenseLabel() {
        return totalExpenseLabel;
    }

    public JLabel getBalanceLabel() {
        return balanceLabel;
    }

    public JButton getExportButton() {
        return exportButton;
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}