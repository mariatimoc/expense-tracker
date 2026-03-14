package Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.InputStream;
import java.util.Properties;

/**
 * A factory class for managing database connections.
 * Provides methods to create and close connections, statements, and result sets.
 */
public class ConnectionFactory {

    private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
    private static final Properties props = new Properties();

    static {
        try (InputStream in = ConnectionFactory.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (Exception ignored) {
        }
    }

    private static final String DBURL = props.getProperty("db.url", "jdbc:mysql://localhost:3306/expense_tracker");
    private static final String USER = props.getProperty("db.user", "root");
    private static final String PASS = props.getProperty("db.pass", "");

    private static final ConnectionFactory singleInstance = new ConnectionFactory();

    private ConnectionFactory() {
    }

    /**
     * Obtains a database connection using the configured factory instance.
     *
     * @return a new Connection object or null if the connection fails
     */
    public static Connection getConnection() {
        return singleInstance.createConnection();
    }

    /**
     * Creates a new database connection.
     *
     * @return a Connection to the database or null if the connection fails
     */
    private Connection createConnection() {
        try {
            return DriverManager.getConnection(DBURL, USER, PASS);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Connection error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Closes the provided Connection.
     *
     * @param conn the Connection to close, can be null
     */
    public static void close(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Close connection error: " + e.getMessage());
        }
    }

    /**
     * Closes the provided Statement.
     *
     * @param stmt the Statement to close, can be null
     */
    public static void close(Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Close statement error: " + e.getMessage());
        }
    }

    /**
     * Closes the provided ResultSet.
     *
     * @param rs the ResultSet to close, can be null
     */
    public static void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Close resultSet error: " + e.getMessage());
        }
    }
}