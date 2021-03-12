package br.com.kafka.subscribe;

import br.com.kafka.LocalDatabase;
import br.com.kafka.behavior.ConsumerService;
import br.com.kafka.client.ProducerClient;
import br.com.kafka.constants.DBConfig;
import br.com.kafka.constants.TopicConfig;
import br.com.kafka.core.StoreLogger;
import br.com.kafka.dto.CorrelationId;
import br.com.kafka.dto.Message;
import br.com.kafka.dto.Order;
import br.com.kafka.service.ServiceRunner;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import static br.com.kafka.constants.DBConfig.SELECT_TB_FRAUDS_ORDER_BY_ID;
import static br.com.kafka.constants.TopicConfig.STORE_NEW_ORDER;

public class FraudDetectorService implements ConsumerService<Order> {

    private final ProducerClient<Order> emailProducer = new ProducerClient<>();
    private LocalDatabase localDatabase;


    public FraudDetectorService() throws SQLException {
        this.localDatabase = new LocalDatabase(DBConfig.URL_DB_FRAUDS);
        this.localDatabase.createTable(DBConfig.CREATE_TB_FRAUDS_ORDER);
    }

    public static void main(String[] args) {
        new ServiceRunner<>(FraudDetectorService::new).start(1);
    }

    @Override
    public String getTopic() {
        return STORE_NEW_ORDER;
    }

    @Override
    public Class<?> getConsumerGroup() {
        return FraudDetectorService.class;
    }

    @Override
    public void parse(ConsumerRecord<String, Message<Order>> record) throws SQLException {
        try {
            StoreLogger.info("----------------------------------------------------");

            Message<Order> message = record.value();
            Order order = message.getPayload();

            if(isProcessed(order)){
                StoreLogger.info("WAS ALREADY PROCESSED");
                return;
            }

            if (isRejected(order)) {

                this.localDatabase.update(DBConfig.INSERT_TB_FRAUDS_ORDER, order.getId(), "true");

                StoreLogger.info("PROCESSING ORDER REJECTED");
                emailProducer.send(
                        new CorrelationId(FraudDetectorService.class.getSimpleName()),
                        TopicConfig.STORE_ORDER_REJECTED,
                        order.getEmail(),
                        order);
            } else {

                this.localDatabase.update(DBConfig.INSERT_TB_FRAUDS_ORDER, order.getId(), "false");

                StoreLogger.info("PROCESSING ORDER APROVED");
                emailProducer.send(
                        new CorrelationId(FraudDetectorService.class.getSimpleName()),
                        TopicConfig.STORE_ORDER_APPROVED,
                        order.getEmail(),
                        order);
            }

            StoreLogger.info("KEY: " + record.key());
            StoreLogger.info(record.value());
            StoreLogger.info(record.offset());
            StoreLogger.info(record.partition());

        } catch (InterruptedException|ExecutionException e) {
            StoreLogger.severe(e);
        }
    }

    private boolean isProcessed(Order order) throws SQLException {
        return this.localDatabase.query(SELECT_TB_FRAUDS_ORDER_BY_ID, order.getId()).next();
    }

    private boolean isRejected(Order order) {
        return order.getAmount().compareTo(new BigDecimal("2000")) >= 0;
    }

}
