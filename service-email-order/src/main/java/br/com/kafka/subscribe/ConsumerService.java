package br.com.kafka.subscribe;

import br.com.kafka.dto.Message;
import br.com.kafka.dto.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumerService<T> {

    String getTopic();
    Class<?> getConsumerGroup();
    void parse(ConsumerRecord<String, Message<Order>> record) throws Exception;
}
