package br.com.kafka.client;

import br.com.kafka.behavior.PrintRecorder;
import br.com.kafka.serialization.GsonDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import static br.com.kafka.constants.ServerConfig.IP_PORT;

public class ConsumerClient<T> implements Closeable {

    private Class<T> type;
    private PrintRecorder printRecorder;
    private KafkaConsumer<String, T> consumer;
    private Map<String, String> mapProperties;

    private ConsumerClient(String groupId, PrintRecorder printRecorder, Class<T> type, Map<String, String> mapProperties) {
        this.type = type;
        this.mapProperties = mapProperties;
        this.consumer = new KafkaConsumer(properties(groupId));
        this.printRecorder = printRecorder;
    }

    public ConsumerClient(String topic, Class<?> aClass, PrintRecorder printRecorder, Class<T> type) {
        this(aClass.getSimpleName(), printRecorder,type, new HashMap<>());
        this.consumer.subscribe(Collections.singletonList(topic));
    }

    public ConsumerClient(Pattern pattern, Class<?> aClass, PrintRecorder printRecorder, Class<T> type, Map<String, String> mapProperties) {
        this(aClass.getSimpleName(), printRecorder, type, mapProperties);
        this.consumer.subscribe(pattern);
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
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupIdConfig);
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        properties.setProperty(GsonDeserializer.TYPE_CONFIG, this.type.getName());
        properties.putAll(this.mapProperties);
        return properties;
    }

    private void processorRecord(ConsumerRecords<String, T> records) {
        if (!records.isEmpty()) {
            for (ConsumerRecord<String, T> record : records) {
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
