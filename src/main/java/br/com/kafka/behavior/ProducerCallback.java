package br.com.kafka.behavior;

import org.apache.kafka.clients.producer.Callback;

public interface ProducerCallback {
    Callback sender();
}
