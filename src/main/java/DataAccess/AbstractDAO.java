package DataAccess;

import Connection.ConnectionFactory;

import javax.swing.table.DefaultTableModel;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());
    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
    }

    private String tableName() {
        return "`" + type.getSimpleName() + "`";
    }

    protected String createSelectAllQuery() {
        return "SELECT * FROM " + tableName();
    }

    protected String createSelectQuery(String field) {
        return "SELECT * FROM " + tableName() + " WHERE " + field + " = ?";
    }

    public List<T> findAll() {
        List<T> list = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(createSelectAllQuery());
             ResultSet rs = stmt.executeQuery()) {

            list = createObjects(rs);

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + " findAll: " + e.getMessage());
        }
        return list;
    }

    public T findById(int id) {
        T result = null;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(createSelectQuery("id"))) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                List<T> list = createObjects(rs);
                if (!list.isEmpty()) {
                    result = list.get(0);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + " findById: " + e.getMessage());
        }
        return result;
    }

    public void insert(T t) {
        Field[] fields = type.getDeclaredFields();

        StringBuilder columns = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();

        for (Field f : fields) {
            if (!"id".equalsIgnoreCase(f.getName())) {
                columns.append(f.getName()).append(", ");
                placeholders.append("?, ");
            }
        }

        if (columns.length() > 0) {
            columns.setLength(columns.length() - 2);
        }
        if (placeholders.length() > 0) {
            placeholders.setLength(placeholders.length() - 2);
        }

        String sql = "INSERT INTO " + tableName() + " (" + columns + ") VALUES (" + placeholders + ")";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            int idx = 1;
            for (Field f : fields) {
                if ("id".equalsIgnoreCase(f.getName())) {
                    continue;
                }
                PropertyDescriptor pd = new PropertyDescriptor(f.getName(), type);
                Object value = pd.getReadMethod().invoke(t);
                stmt.setObject(idx++, value);
            }

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int generatedId = keys.getInt(1);
                    PropertyDescriptor pdId = new PropertyDescriptor("id", type);
                    if (pdId.getWriteMethod() != null) {
                        pdId.getWriteMethod().invoke(t, generatedId);
                    }
                }
            }

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, type.getName() + " insert: " + e.getMessage());
        }
    }

    public void update(T t) {
        Field[] fields = type.getDeclaredFields();

        StringBuilder sql = new StringBuilder("UPDATE ").append(tableName()).append(" SET ");

        boolean first = true;
        for (Field f : fields) {
            if ("id".equalsIgnoreCase(f.getName())) {
                continue;
            }
            if (!first) {
                sql.append(", ");
            }
            sql.append(f.getName()).append(" = ?");
            first = false;
        }
        sql.append(" WHERE id = ?");

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            for (Field f : fields) {
                if ("id".equalsIgnoreCase(f.getName())) {
                    continue;
                }
                PropertyDescriptor pd = new PropertyDescriptor(f.getName(), type);
                Object value = pd.getReadMethod().invoke(t);
                stmt.setObject(idx++, value);
            }

            PropertyDescriptor pdId = new PropertyDescriptor("id", type);
            Object idVal = pdId.getReadMethod().invoke(t);
            stmt.setObject(idx, idVal);

            stmt.executeUpdate();

        } catch (SQLException | IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            LOGGER.log(Level.WARNING, type.getName() + " update: " + e.getMessage());
        }
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM " + tableName() + " WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + " deleteById: " + e.getMessage());
        }
    }

    protected List<T> createObjects(ResultSet rs) {
        List<T> list = new ArrayList<>();
        try {
            Constructor<T> ctor = type.getDeclaredConstructor();
            ctor.setAccessible(true);
            while (rs.next()) {
                T instance = ctor.newInstance();
                for (Field f : type.getDeclaredFields()) {
                    Object value = rs.getObject(f.getName());
                    PropertyDescriptor pd = new PropertyDescriptor(f.getName(), type);
                    pd.getWriteMethod().invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, type.getName() + " createObjects: " + e.getMessage());
        }
        return list;
    }

    public DefaultTableModel buildTableFromList(List<T> objects) {
        DefaultTableModel model = new DefaultTableModel();
        if (objects == null || objects.isEmpty()) {
            return model;
        }

        Field[] fields = type.getDeclaredFields();
        for (Field f : fields) {
            model.addColumn(f.getName());
        }

        for (T obj : objects) {
            Object[] row = new Object[fields.length];
            for (int i = 0; i < fields.length; i++) {
                try {
                    PropertyDescriptor pd = new PropertyDescriptor(fields[i].getName(), type);
                    row[i] = pd.getReadMethod().invoke(obj);
                } catch (Exception e) {
                    row[i] = null;
                }
            }
            model.addRow(row);
        }
        return model;
    }
}