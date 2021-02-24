package br.com.kafka.subscribe;

public interface ServiceFactory<T>{

    ConsumerService<T> create();
}
