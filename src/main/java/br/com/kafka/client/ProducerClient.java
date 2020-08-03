package br.com.kafka.client;

import br.com.kafka.serialization.GsonSerializer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static br.com.kafka.constants.ServerConfig.IP_PORT;

public class ProducerClient<T> implements Closeable {

    private KafkaProducer<String, T> producer;

    public ProducerClient() {
        this.producer = new KafkaProducer(properties());
    }

    public void send(String topic, String key, T value) throws InterruptedException, ExecutionException {
        ProducerRecord record = new ProducerRecord(topic, key, value);
        this.producer.send(record, senderCallback()).get();
    }

    private Properties properties() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, IP_PORT);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GsonSerializer.class.getName());
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
