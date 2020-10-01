package br.com.kafka.client;

import br.com.kafka.behavior.ReadRecorder;
import br.com.kafka.constants.TopicConfig;
import br.com.kafka.dto.CorrelationId;
import br.com.kafka.dto.Message;
import br.com.kafka.serialization.GsonDeserializer;
import br.com.kafka.serialization.GsonSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import static br.com.kafka.constants.ServerConfig.IP_PORT;

public class ConsumerClient<T> implements Closeable {

    private ReadRecorder readRecorder;
    private KafkaConsumer<String, Message<T>> consumer;
    private Map<String, String> mapProperties;


    private ConsumerClient(String groupId, ReadRecorder readRecorder, Map<String, String> mapProperties) {
        this.mapProperties = mapProperties;
        this.consumer = new KafkaConsumer(properties(groupId));
        this.readRecorder = readRecorder;
    }

    private Properties properties(String groupIdConfig) {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, IP_PORT);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupIdConfig);
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        properties.putAll(this.mapProperties);
        return properties;
    }

    public ConsumerClient(String topic, Class<?> aClass, ReadRecorder<T> readRecorder) {
        this(aClass.getSimpleName(), readRecorder, new HashMap<>());
        this.consumer.subscribe(Collections.singletonList(topic));
    }

    public ConsumerClient(Pattern pattern, Class<?> aClass, ReadRecorder<T> readRecorder, Map<String, String> mapProperties) {
        this(aClass.getSimpleName(), readRecorder, mapProperties);
        this.consumer.subscribe(pattern);
    }

    public void run() throws ExecutionException, InterruptedException {
        while (true) {
            processorRecord(consumer.poll(Duration.ofMillis(100)));
        }
    }

    private void processorRecord(ConsumerRecords<String, Message<T>> records) throws ExecutionException, InterruptedException {


        if (!records.isEmpty()) {
            for (ConsumerRecord<String, Message<T>> record : records) {
                try {
                    this.readRecorder.consumeRecorder(record);
                    Thread.sleep(250);
                } catch (Exception e) {
                    e.printStackTrace();
                    try (ProducerClient producerClient = new ProducerClient<>()) {
                        Message<T> message = record.value();
                        producerClient.send(
                                message.getCorrelation().addParent("DeadLetter"),
                                TopicConfig.STORE_DEAD_LETTER,
                                message.getCorrelation().getId(),
                                new GsonSerializer<>().serialize("", message)
                        );
                    }
                }

            }
        }
    }

    @Override
    public void close() {
        this.consumer.close();
    }
}
