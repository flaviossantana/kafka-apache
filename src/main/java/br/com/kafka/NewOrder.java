package br.com.kafka;

import br.com.kafka.config.ServerConfig;
import br.com.kafka.config.TopicConfig;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static br.com.kafka.config.ServerConfig.IP_PORT;
import static br.com.kafka.config.TopicConfig.STORE_NEW_ORDER;

public class NewOrder {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        KafkaProducer producer = new KafkaProducer<String, String>(properties());
        String message = "BIKE,4323";
        ProducerRecord record = new ProducerRecord(STORE_NEW_ORDER, message, message);
        producer.send(record, senderCallback()).get();
    }

    private static Callback senderCallback() {
        return new Callback() {
            public void onCompletion(RecordMetadata data, Exception ex) {
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
            }
        };
    }

    private static Properties properties() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, IP_PORT);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return properties;
    }

}
