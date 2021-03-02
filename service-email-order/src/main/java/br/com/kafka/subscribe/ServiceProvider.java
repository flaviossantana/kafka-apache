package br.com.kafka.subscribe;

import br.com.kafka.client.ConsumerClient;
import br.com.kafka.dto.Order;

import java.util.concurrent.Callable;

public class ServiceProvider<T> implements Callable<Void> {

    private final ServiceFactory<T> factory;

    public ServiceProvider(ServiceFactory<T> factory) {
        this.factory = factory;
    }

    public Void call() throws Exception {

        ConsumerService<T> emailNewOrderService = factory.create();
        try (ConsumerClient<Order> consumerClient = new ConsumerClient<>(
                emailNewOrderService.getTopic(),
                emailNewOrderService.getConsumerGroup(),
                emailNewOrderService::parse)) {
            consumerClient.run();
        }
        return null;
    }


}
