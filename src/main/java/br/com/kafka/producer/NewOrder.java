package br.com.kafka.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static br.com.kafka.config.ServerConfig.IP_PORT;
import static br.com.kafka.config.TopicConfig.STORE_NEW_ORDER;
import static br.com.kafka.config.TopicConfig.STORE_SEND_EMAIL;

public class NewOrder {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        KafkaProducer producer = new KafkaProducer<String, String>(properties());

        String product = "BIKE,4323,"+ new Date().getTime();
        ProducerRecord orderRecord = new ProducerRecord(STORE_NEW_ORDER, product, product);
        producer.send(orderRecord, senderCallback()).get();


        String email = "Thanks for your purchase!";
        ProducerRecord emailRecorder = new ProducerRecord(STORE_SEND_EMAIL, email, email);
        producer.send(emailRecorder, senderCallback()).get();

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
