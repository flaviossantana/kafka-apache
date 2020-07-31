package br.com.kafka.producer;

import br.com.kafka.core.ProducerService;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static br.com.kafka.config.TopicConfig.STORE_NEW_ORDER;
import static br.com.kafka.config.TopicConfig.STORE_SEND_EMAIL;

public class NewOrder {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try (ProducerService produce = new ProducerService();) {
            for (int i = 0; i <= 100; i++) {
                String user = UUID.randomUUID().toString();
                produce.send(STORE_NEW_ORDER, user, "USER: " + user + ", BIKE, R$: 4323.00");
                produce.send(STORE_SEND_EMAIL, user, "USER: " + user + ". Thanks for your purchase!");
            }
        }
    }
}
