package br.com.kafka.behavior;

import br.com.kafka.dto.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumerService<T> {

    String getTopic();
    Class<?> getConsumerGroup();
    void parse(ConsumerRecord<String, Message<T>> record) throws Exception;
}
