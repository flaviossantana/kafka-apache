package br.com.kafka.subscribe;

import br.com.kafka.client.ProducerClient;
import br.com.kafka.constants.TopicConfig;
import br.com.kafka.dto.Order;
import br.com.kafka.client.ConsumerClient;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

import static br.com.kafka.constants.TopicConfig.STORE_NEW_ORDER;

public class FraudDetectorService {

    private final ProducerClient emailProducer = new ProducerClient<Order>();

    public static void main(String[] args) {

        FraudDetectorService fraudDetectorService = new FraudDetectorService();
        try (ConsumerClient<Order> consumerClient = new ConsumerClient<>(
                STORE_NEW_ORDER,
                FraudDetectorService.class,
                fraudDetectorService::print,
                Order.class)) {
            consumerClient.run();
        }

    }

    private void print(ConsumerRecord<String, Order> record) {
        try {
            System.out.println("----------------------------------------------------");

            Order order = record.value();
            if (isRejected(order)) {
                System.out.println("PROCESSING ORDER REJECTED");
                emailProducer.send(TopicConfig.STORE_ORDER_REJECTED, order.getUserId(), order);
            } else {
                System.out.println("PROCESSING ORDER APROVED");
                emailProducer.send(TopicConfig.STORE_ORDER_APPROVED, order.getUserId(), order);
            }

            System.out.println(record.key());
            System.out.println(record.value());
            System.out.println(record.offset());
            System.out.println(record.partition());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    private boolean isRejected(Order order) {
        return order.getAmount().compareTo(new BigDecimal("2000")) >= 0;
    }
}
