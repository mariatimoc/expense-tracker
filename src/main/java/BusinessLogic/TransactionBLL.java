package BusinessLogic;

import DataAccess.TransactionDAO;
import Model.Transaction;

import java.util.List;

public class TransactionBLL {
    private final TransactionDAO dao = new TransactionDAO();

    public List<Transaction> findAll() {
        return dao.findAll();
    }

    public Transaction findTransactionById(int id) {
        return dao.findById(id);
    }

    public void insertTransaction(Transaction t) {
        dao.insert(t);
    }

    public void updateTransaction(Transaction t) {
        dao.update(t);
    }

    public void deleteTransaction(int id) {
        dao.deleteById(id);
    }

    public List<Transaction> findTransactionsByTitle(String title) {
        return dao.findByTitle(title);
    }

    public List<Transaction> findTransactionsByType(String type) {
        return dao.findByType(type);
    }

    public TransactionDAO getDAO() {
        return dao;
    }
}