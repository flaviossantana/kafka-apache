package br.com.kafka.behavior;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface PrintRecorder {
    void print(ConsumerRecord<String, String> record);
}
