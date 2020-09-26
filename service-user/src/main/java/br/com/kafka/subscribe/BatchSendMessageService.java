package br.com.kafka.subscribe;

import br.com.kafka.client.ConsumerClient;
import br.com.kafka.client.ProducerClient;
import br.com.kafka.dto.CorrelationId;
import br.com.kafka.dto.Message;
import br.com.kafka.dto.User;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.Closeable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static br.com.kafka.constants.DBConfig.*;
import static br.com.kafka.constants.TopicConfig.STORE_SEND_MESSAGE_TO_ALL_USERS;

public class BatchSendMessageService implements Closeable {

    private final Connection connection;
    private final ProducerClient reportUserProducer = new ProducerClient<User>();

    public BatchSendMessageService() throws SQLException {
        this.connection = DriverManager.getConnection(URL_DB);
        this.connection.createStatement().execute(CREATE_TB_USERS);
    }

    public static void main(String[] args) throws SQLException {
        BatchSendMessageService batchSendMessageService = new BatchSendMessageService();
        try (ConsumerClient consumerClient = new ConsumerClient<>(
                STORE_SEND_MESSAGE_TO_ALL_USERS,
                BatchSendMessageService.class,
                batchSendMessageService::consumer)) {
            consumerClient.run();
        }
    }

    private void consumer(ConsumerRecord<String, Message<String>> record) {
        try {

            Message<String> message = record.value();
            String topic = message.getPayload();
            for (User user : getAllUsers()) {
                reportUserProducer.send(
                        message.getCorrelation().addParent(BatchSendMessageService.class.getSimpleName()),
                        topic,
                        user.getUuid(),
                        user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private List<User> getAllUsers() throws SQLException {
        PreparedStatement select = this.connection.prepareStatement(SELECT_TB_USERS_ALL);
        ResultSet resultSet = select.executeQuery();
        List<User> users = new ArrayList<>();
        while (resultSet.next()){
            users.add(new User(resultSet.getString("UUID")));
        }
        return users;
    }

    @Override
    public void close() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
