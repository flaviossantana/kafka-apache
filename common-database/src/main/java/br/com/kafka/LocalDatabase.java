package br.com.kafka;

import java.sql.*;
import java.util.Properties;

public class LocalDatabase {

    private final Connection connection;

    public LocalDatabase(String urlDatabase) throws SQLException {
        this.connection = DriverManager.getConnection(urlDatabase, new Properties());
    }

    public boolean createTable(String sql) throws SQLException {
        return this.connection.createStatement().execute(sql);
    }

    public void update(String sql, String... params) throws SQLException {
        this.preparedStatement(sql, params).execute();
    }

    public boolean insert(String sql, String... params) throws SQLException {
        PreparedStatement preparedStatement = this.preparedStatement(sql, params);
        try(ResultSet result = preparedStatement.executeQuery()){
            return !result.next();
        }
    }

    private PreparedStatement preparedStatement(String sql, String... params) throws SQLException {
        PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setString(i+1, params[i]);
        }
        return preparedStatement;
    }

}
