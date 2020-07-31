package br.com.kafka.subscribe;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.regex.Pattern;

import static br.com.kafka.config.ServerConfig.IP_PORT;
import static br.com.kafka.config.TopicConfig.STORE_ALL_TOPICS;

public class LogService {

    public static void main(String[] args) {

        KafkaConsumer consumer = new KafkaConsumer<String, String>(properties());
        consumer.subscribe(Pattern.compile(STORE_ALL_TOPICS));

        while (true){
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            processorRecord(records);
        }

    }

    private static Properties properties() {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, IP_PORT);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, LogService.class.getSimpleName());
        return properties;
    }

    private static void processorRecord(ConsumerRecords<String, String> records) {
        if (!records.isEmpty()) {
            records.forEach(record -> {
                printLog(record);
            });
        }
    }

    private static void printLog(ConsumerRecord<String, String> record) {
        System.out.println("----------------------------------------------------");
        System.out.println("LOGGING STORE TOPICS");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.offset());
        System.out.println(record.partition());
    }

}
