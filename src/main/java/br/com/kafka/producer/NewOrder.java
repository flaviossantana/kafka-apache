package br.com.kafka.producer;

import br.com.kafka.core.ProducerService;
import org.apache.kafka.clients.producer.Callback;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static br.com.kafka.config.TopicConfig.STORE_NEW_ORDER;
import static br.com.kafka.config.TopicConfig.STORE_SEND_EMAIL;

public class NewOrder {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        NewOrder newOrder = new NewOrder();
        ProducerService orderProducer = new ProducerService(STORE_NEW_ORDER, newOrder::senderCallback);
        ProducerService emailProducer = new ProducerService(STORE_SEND_EMAIL, newOrder::senderCallback);

        for(int i = 0; i <=  100; i++){
            String user = UUID.randomUUID().toString();
            orderProducer.send(user, "USER: " + user + ", BIKE, R$: 4323.00");
            emailProducer.send(user, "USER: " +user+ ". Thanks for your purchase!");
        }
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

}
