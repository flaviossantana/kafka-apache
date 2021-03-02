package br.com.kafka.service;

import br.com.kafka.behavior.ConsumerService;
import br.com.kafka.behavior.ServiceFactory;
import br.com.kafka.client.ConsumerClient;

import java.util.concurrent.Callable;

public class ServiceProvider<T> implements Callable<Void> {

    private final ServiceFactory<T> factory;

    public ServiceProvider(ServiceFactory<T> factory) {
        this.factory = factory;
    }

    public Void call() throws Exception {

        ConsumerService<T> emailNewOrderService = factory.create();
        try (ConsumerClient<T> consumerClient = new ConsumerClient<>(
                emailNewOrderService.getTopic(),
                emailNewOrderService.getConsumerGroup(),
                emailNewOrderService::parse)) {
            consumerClient.run();
        }
        return null;
    }


}
