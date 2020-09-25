package br.com.kafka.subscribe;

import br.com.kafka.client.ProducerClient;
import br.com.kafka.constants.TopicConfig;
import br.com.kafka.dto.CorrelationId;
import br.com.kafka.dto.Message;
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

    private void print(ConsumerRecord<String, Message<Order>> record) {
        try {
            System.out.println("----------------------------------------------------");

            Message<Order> message = record.value();
            Order order = message.getPayload();

            if (isRejected(order)) {
                System.out.println("PROCESSING ORDER REJECTED");
                emailProducer.send(
                        new CorrelationId(FraudDetectorService.class.getSimpleName()),
                        TopicConfig.STORE_ORDER_REJECTED,
                        order.getEmail(),
                        order);
            } else {
                System.out.println("PROCESSING ORDER APROVED");
                emailProducer.send(
                        new CorrelationId(FraudDetectorService.class.getSimpleName()),
                        TopicConfig.STORE_ORDER_APPROVED,
                        order.getEmail(),
                        order);
            }

            System.out.println("KEY: " + record.key());
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
