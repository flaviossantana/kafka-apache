package br.com.kafka.subscribe;

import br.com.kafka.behavior.ConsumerService;
import br.com.kafka.core.StoreLogger;
import br.com.kafka.dto.Message;
import br.com.kafka.dto.Order;
import br.com.kafka.service.ServiceRunner;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.*;
import java.util.Properties;
import java.util.UUID;

import static br.com.kafka.constants.DBConfig.*;
import static br.com.kafka.constants.TopicConfig.STORE_NEW_ORDER;

public class CreateUserlService implements ConsumerService<Order> {

    private final Connection connection;

    public CreateUserlService() throws SQLException {
        this.connection = DriverManager.getConnection(URL_DB, new Properties());
        this.connection.createStatement().execute(CREATE_TB_USERS);
    }

    public static void main(String[] args) {
        new ServiceRunner<Order>(CreateUserlService::new).start(1);
    }

    @Override
    public String getTopic() {
        return STORE_NEW_ORDER;
    }

    @Override
    public Class<?> getConsumerGroup() {
        return CreateUserlService.class;
    }

    @Override
    public void parse(ConsumerRecord<String, Message<Order>> record) {


        try {

            Message<Order> message = record.value();
            Order order = message.getPayload();

            if (isNewuser(order.getEmail())) {
                createUser(order.getEmail());

                StoreLogger.info("----------------------------------------------------");
                StoreLogger.info("CREATING A NEW USER: " + order.getEmail());
                StoreLogger.info("KEY: " + record.key());
                StoreLogger.info(record.value());
                StoreLogger.info(record.offset());
                StoreLogger.info(record.partition());

            }
        } catch (SQLException e) {
            StoreLogger.severe(e);
        }
    }

    private void createUser(String email) throws SQLException {
        try(PreparedStatement insert = this.connection.prepareStatement(INSERT_TB_USERS)){
            insert.setString(1, UUID.randomUUID().toString());
            insert.setString(2, email);
            insert.execute();
        }
    }

    private boolean isNewuser(String email) throws SQLException {
        try(PreparedStatement exists = this.connection.prepareStatement(SELECT_TB_USERS_BY_EMAIL)){
            exists.setString(1, email);
            try(ResultSet result = exists.executeQuery()){
                return !result.next();
            }
        }
    }

}
