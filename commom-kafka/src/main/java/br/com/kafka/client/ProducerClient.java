package br.com.kafka.client;

import br.com.kafka.dto.CorrelationId;
import br.com.kafka.dto.Message;
import br.com.kafka.serialization.GsonSerializer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static br.com.kafka.constants.ServerConfig.IP_PORT;

public class ProducerClient<T> implements Closeable {

    private KafkaProducer<String, Message<T>> producer;

    public ProducerClient() {
        this.producer = new KafkaProducer(properties());
    }

    public Future sendAsyc(CorrelationId correlationId, String topic, String key, T payload) throws InterruptedException, ExecutionException {
        Message<T> value = new Message<>(correlationId, payload);
        ProducerRecord record = new ProducerRecord(topic, key, value);
        return this.producer.send(record, senderCallback());
    }

    public void send(CorrelationId correlationId, String topic, String key, T payload) throws InterruptedException, ExecutionException {
        this.sendAsyc(correlationId.addParent(">" + topic + "<"), topic, key, payload).get();
    }

    private Properties properties() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, IP_PORT);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GsonSerializer.class.getName());
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
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
