package br.com.kafka.core;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static br.com.kafka.config.ServerConfig.IP_PORT;

public class ProducerService implements Closeable {

    private KafkaProducer producer;

    public ProducerService() {
        this.producer = new KafkaProducer<String, String>(properties());
    }

    public void send(String topic, String key, String value) throws InterruptedException, ExecutionException {
        ProducerRecord record = new ProducerRecord(topic, key, value);
        this.producer.send(record, senderCallback()).get();
    }

    private Properties properties() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, IP_PORT);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

    private Callback senderCallback() {
        return (data, ex) -> {
            if(ex != null){
                ex.printStackTrace();
                return;
            }
            System.out.println(
                    "SENT SUCCESSFULY: " +
                            "TOPIC: " +
                            data.topic() + " | " +
                            "PARTITION: " +
                            data.partition() +" | " +
                            "OFFSET: " +
                            data.offset() +" | "+
                            "TIMESTAMP: " +
                            data.timestamp());
        };
    }

    @Override
    public void close() {
        this.producer.close();
    }
}
