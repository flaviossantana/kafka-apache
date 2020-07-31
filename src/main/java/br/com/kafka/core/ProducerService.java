package br.com.kafka.core;

import br.com.kafka.behavior.ProducerCallback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static br.com.kafka.config.ServerConfig.IP_PORT;

public class ProducerService {

    private String topic;
    private KafkaProducer producer;
    private ProducerCallback producerCallback;

    public ProducerService(String topic, ProducerCallback producerCallback) {
        this.topic = topic;
        this.producerCallback = producerCallback;
        this.producer = new KafkaProducer<String, String>(properties());
    }

    public void send(String key, String value) throws InterruptedException, ExecutionException {
        ProducerRecord record = new ProducerRecord(this.topic, key, value);
        this.producer.send(record, this.producerCallback.sender()).get();
    }

    private Properties properties() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, IP_PORT);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

}
