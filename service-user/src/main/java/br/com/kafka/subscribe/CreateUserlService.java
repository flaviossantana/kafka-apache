package br.com.kafka.subscribe;

import br.com.kafka.LocalDatabase;
import br.com.kafka.behavior.ConsumerService;
import br.com.kafka.constants.DBConfig;
import br.com.kafka.core.StoreLogger;
import br.com.kafka.dto.Message;
import br.com.kafka.dto.Order;
import br.com.kafka.service.ServiceRunner;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.sql.SQLException;
import java.util.UUID;

import static br.com.kafka.constants.TopicConfig.STORE_NEW_ORDER;

public class CreateUserlService implements ConsumerService<Order> {

    private final LocalDatabase localDatabase;

    public CreateUserlService() throws SQLException {
        this.localDatabase = new LocalDatabase(DBConfig.URL_DB_STORE);
        this.localDatabase.createTable(DBConfig.CREATE_TB_USERS);
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
        this.localDatabase.update(DBConfig.INSERT_TB_USERS, UUID.randomUUID().toString(), email);
    }

    private boolean isNewuser(String email) throws SQLException {
        return this.localDatabase.insert(DBConfig.SELECT_TB_USERS_BY_EMAIL, email);
    }

}
