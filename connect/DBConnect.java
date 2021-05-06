package connect;

import java.sql.*;

public class DBConnect {
    private final String url = "jdbc:postgresql://localhost:5432/milktea-eng";
    private final String username = "postgres";
    private final String password = "1472";
    private static Statement statement = null;
    private static Connection connection = null;

    public DBConnect() {
        try {
            connection = DriverManager.getConnection(url, username, password);
            if (connection != null) {
                System.out.println("Successfully connected.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ResultSet excuteQuerySelect(String queryStatement) {
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(queryStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public boolean excuteQueryUpdate(String queryStatement) {
        int result = 0;
        try {
            statement = connection.createStatement();
            result = statement.executeUpdate(queryStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result > 0;
    }
}
