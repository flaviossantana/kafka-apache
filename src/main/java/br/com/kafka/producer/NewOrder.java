package br.com.kafka.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static br.com.kafka.config.ServerConfig.IP_PORT;
import static br.com.kafka.config.TopicConfig.STORE_NEW_ORDER;
import static br.com.kafka.config.TopicConfig.STORE_SEND_EMAIL;

public class NewOrder {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        KafkaProducer producer = new KafkaProducer<String, String>(properties());

        for(int i = 0; i <=  1000; i++){
            String user = UUID.randomUUID().toString();
            sendNewOrder(user, producer);
            sendEmailNewOrder(user, producer);
        }
    }

    private static void sendEmailNewOrder(String user, KafkaProducer producer) throws InterruptedException, ExecutionException {
        String email = "USER: " +user+ ". Thanks for your purchase!";
        ProducerRecord emailRecorder = new ProducerRecord(STORE_SEND_EMAIL, user, email);
        producer.send(emailRecorder, senderCallback()).get();
    }

    private static void sendNewOrder(String user, KafkaProducer producer) throws InterruptedException, ExecutionException {
        String product = "USER: " + user + ", BIKE, R$: 4323.00";
        ProducerRecord orderRecord = new ProducerRecord(STORE_NEW_ORDER, user, product);
        producer.send(orderRecord, senderCallback()).get();
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
