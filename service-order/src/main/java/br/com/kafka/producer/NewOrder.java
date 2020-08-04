package br.com.kafka.producer;

import br.com.kafka.dto.Order;
import br.com.kafka.client.ProducerClient;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static br.com.kafka.constants.TopicConfig.STORE_NEW_ORDER;
import static br.com.kafka.constants.TopicConfig.STORE_SEND_EMAIL;

public class NewOrder {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try (ProducerClient orderProducer = new ProducerClient<Order>()) {
            try (ProducerClient emailProducer = new ProducerClient<String>()) {
                for (int i = 0; i <= 10; i++) {

                    String orderId = UUID.randomUUID().toString();
                    String userId = UUID.randomUUID().toString();
                    BigDecimal amount = BigDecimal.valueOf(Math.random() * 5000 + 1);

                    Order order = new Order(orderId, userId, amount);

                    orderProducer.send(STORE_NEW_ORDER, userId, order);
                    emailProducer.send(STORE_SEND_EMAIL, userId, "USER: " + userId + ". Thanks for your purchase!");
                }
            }
        }
    }
}
