package DataAccess;

import Connection.ConnectionFactory;
import Model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class TransactionDAO extends AbstractDAO<Transaction> {

    public TransactionDAO() {
        super();
    }

    public List<Transaction> findByTitle(String title) {
        List<Transaction> list = new ArrayList<>();
        String query = "SELECT * FROM `Transaction` WHERE title LIKE ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, "%" + title + "%");
            resultSet = statement.executeQuery();

            list = createObjects(resultSet);

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "TransactionDAO findByTitle: " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return list;
    }

    public List<Transaction> findByType(String type) {
        List<Transaction> list = new ArrayList<>();
        String query = "SELECT * FROM `Transaction` WHERE type = ?";

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try{
            connection = ConnectionFactory.getConnection();
            stmt=connection.prepareStatement(query);
            stmt.setString(1, type);
            rs = stmt.executeQuery();

            list = createObjects(rs);

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING,"TransactionDAO findByType "+ e.getMessage());

        }
        finally{
            ConnectionFactory.close(rs);
            ConnectionFactory.close(stmt);
            ConnectionFactory.close(connection);
        }
        return list;
    }
}