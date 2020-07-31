package br.com.kafka.subscribe;

import br.com.kafka.core.ConsumerService;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.regex.Pattern;

import static br.com.kafka.config.TopicConfig.STORE_ALL_TOPICS;

public class LogService {

    public static void main(String[] args) {

        LogService logService = new LogService();
        try (ConsumerService consumerService = new ConsumerService(
                Pattern.compile(STORE_ALL_TOPICS),
                LogService.class,
                logService::printLog)) {
            consumerService.run();
        }

    }

    private void printLog(ConsumerRecord<String, String> record) {
        System.out.println("----------------------------------------------------");
        System.out.println("LOGGING STORE TOPICS");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.offset());
        System.out.println(record.partition());
    }

}
