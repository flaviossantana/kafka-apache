package br.com.kafka.behavior;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ReadRecorder<T> {
    void consumeRecorder(ConsumerRecord<String, T> record);
}
