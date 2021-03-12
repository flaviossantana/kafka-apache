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

    public ResultSet query(String sql, String... params) throws SQLException {
        PreparedStatement preparedStatement = this.preparedStatement(sql, params);
        return preparedStatement.executeQuery();
    }

    public void insert(String sql, String... params) throws SQLException {
        PreparedStatement preparedStatement = this.preparedStatement(sql, params);
        int i = preparedStatement.executeUpdate();
        System.out.println(i);
    }

    private PreparedStatement preparedStatement(String sql, String... params) throws SQLException {
        PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setString(i+1, params[i]);
        }
        return preparedStatement;
    }

    public void close() throws SQLException {
        this.connection.close();
    }
}
