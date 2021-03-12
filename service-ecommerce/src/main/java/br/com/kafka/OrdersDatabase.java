package br.com.kafka;

import br.com.kafka.constants.DBConfig;
import br.com.kafka.dto.Order;

import java.io.Closeable;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrdersDatabase implements Closeable {

    private final LocalDatabase database;

    public OrdersDatabase() throws SQLException {
        this.database = new LocalDatabase(DBConfig.URL_DB_ORDERS);
        this.database.createTable(DBConfig.CREATE_TB_ORDER);
    }

    public boolean save(Order order) throws SQLException {
        if(isExistOrder(order.getId())){
            return false;
        }

        database.insert(DBConfig.INSERT_TB_ORDER, order.getId());
        return true;
    }

    public boolean isExistOrder(String uuid) throws SQLException {
        ResultSet query = this.database.query(DBConfig.SELECT_TB_ORDER_BY_ID, uuid);
        return query.next();
    }

    @Override
    public void close() throws IOException {
        try {
            this.database.close();
        } catch (SQLException e) {
            throw new IOException();
        }
    }
}
