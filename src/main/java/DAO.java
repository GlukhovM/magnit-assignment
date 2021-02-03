import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO {

    private String dbName;
    private String host;
    private int port;
    private String user;
    private String pass;

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    private String buildURL() {
        return String.format("jdbc:postgresql://%s:%d/%s",
                host, port, dbName);
    }

    public void fillDB(String tableName, String columnName, int entriesCount) {
        try (Connection connection = DriverManager.getConnection(buildURL(), user, pass)) {
            String insertSQL = "INSERT INTO " +
                    tableName +
                    " (" + columnName + ") " +
                    "VALUES (?)";
            String truncateSQL = "TRUNCATE " + tableName;

            connection.createStatement().execute(truncateSQL);
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);

            for (int i = 1; i <= entriesCount; i++) {
                preparedStatement.setInt(1, i);
                preparedStatement.addBatch();
                if (i % 1000 == 0) {
                    preparedStatement.executeBatch();
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            //TODO use logger LOGBACK
        }
    }

    public List<Integer> getAll(String tableName) {
        List<Integer> result = new ArrayList<>();
        String select = "SELECT * FROM " +
                tableName;
        try (Connection connection = DriverManager.getConnection(buildURL(), user, pass);
             ResultSet resultSet = connection.createStatement().executeQuery(select)) {
            while (resultSet.next()) {
                result.add(resultSet.getInt(1));
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            //TODO use logger LOGBACK
        }
        return result;
    }
}
