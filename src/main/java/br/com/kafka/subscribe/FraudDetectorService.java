package br.com.kafka.subscribe;

import br.com.kafka.bean.Order;
import br.com.kafka.client.ConsumerClient;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import static br.com.kafka.constants.TopicConfig.STORE_NEW_ORDER;

public class FraudDetectorService {

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

    public void print(ConsumerRecord<String, Order> record) {
        System.out.println("----------------------------------------------------");
        System.out.println("PROCESSING NEW ORDER");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.offset());
        System.out.println(record.partition());
    }
}
