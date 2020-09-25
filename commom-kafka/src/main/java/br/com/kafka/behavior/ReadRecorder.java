package br.com.kafka.behavior;

import br.com.kafka.dto.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ReadRecorder<T> {
    void consumeRecorder(ConsumerRecord<String, Message<T>> record);
}
