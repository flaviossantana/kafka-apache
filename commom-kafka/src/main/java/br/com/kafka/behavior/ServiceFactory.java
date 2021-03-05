package br.com.kafka.behavior;

public interface ServiceFactory<T>{

    ConsumerService<T> create() throws Exception;
}
