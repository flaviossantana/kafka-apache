package br.com.kafka.core;

import br.com.kafka.behavior.PrintRecorder;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.regex.Pattern;

import static br.com.kafka.config.ServerConfig.IP_PORT;

public class ConsumerService implements Closeable {

    private KafkaConsumer<String, String> consumer;
    private PrintRecorder printRecorder;

    public ConsumerService(String topic, Class<?> aClass, PrintRecorder printRecorder) {
        this.consumer = new KafkaConsumer(properties(aClass.getSimpleName()));
        this.consumer.subscribe(Collections.singletonList(topic));
        this.printRecorder = printRecorder;
    }

    public ConsumerService(Pattern pattern, Class<?> aClass, PrintRecorder printRecorder) {
        this.consumer = new KafkaConsumer(properties(aClass.getSimpleName()));
        this.consumer.subscribe(pattern);
        this.printRecorder = printRecorder;
    }

    public void run(){
        while (true){
            processorRecord(consumer.poll(Duration.ofMillis(100)));
        }
    }

    private Properties properties(String groupIdConfig) {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, IP_PORT);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupIdConfig);
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        return properties;
    }

    private void processorRecord(ConsumerRecords<String, String> records) {
        if (!records.isEmpty()) {
            for (ConsumerRecord<String, String> record : records) {
                this.printRecorder.print(record);
                sleep();
            }
        }
    }

    private void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        this.consumer.close();
    }
}
