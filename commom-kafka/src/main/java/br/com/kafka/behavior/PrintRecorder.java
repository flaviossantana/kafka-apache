package br.com.kafka.behavior;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface PrintRecorder<T> {
    void print(ConsumerRecord<String, T> record);
}
