package br.com.kafka.producer;

import br.com.kafka.client.ProducerClient;
import br.com.kafka.data.GenereteData;
import br.com.kafka.dto.Order;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static br.com.kafka.constants.TopicConfig.STORE_NEW_ORDER;
import static br.com.kafka.constants.TopicConfig.STORE_SEND_EMAIL;

public class NewOrder {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try (ProducerClient orderProducer = new ProducerClient<Order>()) {
            try (ProducerClient emailProducer = new ProducerClient<String>()) {
                for (int i = 0; i <= 1000; i++) {

                    String email = GenereteData.email();

                    String orderId = UUID.randomUUID().toString();
                    BigDecimal amount = BigDecimal.valueOf(Math.random() * 5000 + 1);

                    Order order = new Order(orderId, email, amount);

                    orderProducer.send(STORE_NEW_ORDER, email, order);
                    emailProducer.send(STORE_SEND_EMAIL, email, "USER: " + email + ". Thanks for your purchase!");
                }
            }
        }
    }



}
