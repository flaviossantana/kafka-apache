package br.com.kafka.subscribe;

import br.com.kafka.client.ConsumerClient;
import br.com.kafka.dto.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.*;

import static br.com.kafka.constants.DBConfig.*;
import static br.com.kafka.constants.TopicConfig.STORE_NEW_ORDER;

public class CreateUserlService {

    private final Connection connection;

    public CreateUserlService() throws SQLException {
        this.connection = DriverManager.getConnection(URL_DB);
        this.connection.createStatement().execute(CREATE_TB_USERS);
    }

    public static void main(String[] args) throws SQLException {
        CreateUserlService createUserService = new CreateUserlService();
        try (ConsumerClient consumerClient = new ConsumerClient<>(
                STORE_NEW_ORDER,
                CreateUserlService.class,
                createUserService::consumeOrder,
                Order.class)) {
            consumerClient.run();
        }
    }

    private void consumeOrder(ConsumerRecord<String, Order> record) {


        try {
            Order order = record.value();
            if (isNewuser(order.getEmail())) {
                createUser(order.getUserId(), order.getEmail());

                System.out.println("----------------------------------------------------");
                System.out.println("CREATING A NEW USER: " + order.getEmail());
                System.out.println(record.key());
                System.out.println(record.value());
                System.out.println(record.offset());
                System.out.println(record.partition());

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createUser(String uuid, String email) throws SQLException {
        PreparedStatement insert = this.connection.prepareStatement(INSERT_TB_USERS);
        insert.setString(1, uuid);
        insert.setString(2, email);
        insert.execute();
    }

    private boolean isNewuser(String email) throws SQLException {
        PreparedStatement exists = this.connection.prepareStatement(SELECT_TB_USERS_BY_EMAIL);
        exists.setString(1, email);
        ResultSet result = exists.executeQuery();
        return !result.next();
    }

}
