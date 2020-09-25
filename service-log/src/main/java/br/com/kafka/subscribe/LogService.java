package br.com.kafka.subscribe;

import br.com.kafka.client.ConsumerClient;
import br.com.kafka.dto.Message;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.HashMap;
import java.util.regex.Pattern;

import static br.com.kafka.constants.TopicConfig.STORE_ALL_TOPICS;
import static java.lang.System.out;

public class LogService {

    public static void main(String[] args) {

        LogService logService = new LogService();

        HashMap<String, String> properties = new HashMap<>();
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        try (ConsumerClient<String> consumerClient = new ConsumerClient<>(
                Pattern.compile(STORE_ALL_TOPICS),
                LogService.class,
                logService::printLog,
                String.class,
                properties)) {
            consumerClient.run();
        }
    }

    private void printLog(ConsumerRecord<String, Message<String>> record) {
        out.println("----------------------------------------------------");
        out.println("LOGGING: " +  record.topic());
        out.println("KEY: " + record.key());
        out.println(record.value());
    }

}
