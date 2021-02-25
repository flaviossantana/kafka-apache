package br.com.kafka.subscribe;

import br.com.kafka.dto.Order;

import br.com.kafka.client.ConsumerClient;
import br.com.kafka.client.ProducerClient;
import br.com.kafka.dto.Message;
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

        System.out.println("-----------------------------------------------------");
        System.out.println("###### PROCESSING NEW ORDER, PREPARING EMAIL #######");
        System.out.println("LOGGING: " +  record.topic());
        System.out.println("KEY: " + record.key());
        System.out.println(record.value());

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
