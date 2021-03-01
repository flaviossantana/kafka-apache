package br.com.kafka.subscribe;

import br.com.kafka.client.ProducerClient;
import br.com.kafka.core.StoreLogger;
import br.com.kafka.dto.Message;
import br.com.kafka.dto.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.concurrent.ExecutionException;

import static br.com.kafka.constants.TopicConfig.STORE_NEW_ORDER;
import static br.com.kafka.constants.TopicConfig.STORE_SEND_EMAIL;

public class EmailNewOrderService implements ConsumerService<String> {

    ProducerClient<String> producerClient = new ProducerClient<>();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        new ServiceProvider().run(EmailNewOrderService::new);
    }

    public String getTopic(){
        return STORE_NEW_ORDER;
    }

    public Class<?> getConsumerGroup(){
        return EmailNewOrderService.class;
    }

    public void parse(ConsumerRecord<String, Message<Order>> record) throws ExecutionException, InterruptedException {

        StoreLogger.info("-----------------------------------------------------");
        StoreLogger.info("###### PROCESSING NEW ORDER, PREPARING EMAIL ########");
        StoreLogger.info("LOGGING: " +  record.topic());
        StoreLogger.info("KEY: " + record.key());
        StoreLogger.info(record.value());

        String emailBody = "Thank you for your order! We are processing your order!";
        Order order = record.value().getPayload();

        producerClient.send(
                record.value().getCorrelation().addParent(EmailNewOrderService.class.getSimpleName()),
                STORE_SEND_EMAIL,
                order.getEmail(),
                emailBody
        );

    }

}
