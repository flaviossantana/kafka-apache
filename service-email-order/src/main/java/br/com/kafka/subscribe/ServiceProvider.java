package br.com.kafka.subscribe;

import br.com.kafka.client.ConsumerClient;
import br.com.kafka.dto.Order;

import java.util.concurrent.ExecutionException;

public class ServiceProvider {

    public <T> void run(ServiceFactory<T> factory) throws ExecutionException, InterruptedException {

        EmailNewOrderService emailNewOrderService = (EmailNewOrderService) factory.create();
        try (ConsumerClient<Order> consumerClient = new ConsumerClient<>(
                emailNewOrderService.getTopic(),
                emailNewOrderService.getConsumerGroup(),
                emailNewOrderService::parse)) {
            consumerClient.run();
        }
    }
}
