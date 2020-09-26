package br.com.kafka.subscribe;

import br.com.kafka.client.ConsumerClient;
import br.com.kafka.dto.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import static br.com.kafka.constants.TopicConfig.STORE_SEND_EMAIL;

public class EmailService {

    public static void main(String[] args) {
        EmailService emailService = new EmailService();
        try (ConsumerClient consumerClient = new ConsumerClient<>(
                STORE_SEND_EMAIL,
                EmailService.class,
                emailService::printEmail
        )) {
            consumerClient.run();
        }
    }

    private void printEmail(ConsumerRecord<String, Message<String>> record) {
        System.out.println("----------------------------------------------------");
        System.out.println("SEND EMAIL FOR NEW ORDER");
        System.out.println("KEY:" + record.key());
        System.out.println(record.value());
        System.out.println(record.offset());
        System.out.println(record.partition());
    }

}
