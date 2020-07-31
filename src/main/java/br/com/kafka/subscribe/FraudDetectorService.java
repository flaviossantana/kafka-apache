package br.com.kafka.subscribe;

import br.com.kafka.core.ConsumerService;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import static br.com.kafka.config.TopicConfig.STORE_NEW_ORDER;

public class FraudDetectorService {

    public static void main(String[] args) {

        FraudDetectorService fraudDetectorService = new FraudDetectorService();
        try (ConsumerService consumerService = new ConsumerService(
                STORE_NEW_ORDER,
                FraudDetectorService.class,
                fraudDetectorService::print)) {
            consumerService.run();
        }

    }

    public void print(ConsumerRecord<String, String> record) {
        System.out.println("----------------------------------------------------");
        System.out.println("PROCESSING NEW ORDER");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.offset());
        System.out.println(record.partition());
    }
}
