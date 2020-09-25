package br.com.kafka.subscribe;

import br.com.kafka.client.ConsumerClient;
import br.com.kafka.core.IO;
import br.com.kafka.dto.Message;
import br.com.kafka.dto.User;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static br.com.kafka.constants.TopicConfig.STORE_REPORT_USER;

public class ReportService {

    private static final InputStream IN = IO.getResourceAsStream("report_user.txt");

    public static void main(String[] args) {
        ReportService emailService = new ReportService();
        try (ConsumerClient consumerClient = new ConsumerClient<>(
                STORE_REPORT_USER,
                ReportService.class,
                emailService::print,
                User.class)) {
            consumerClient.run();
        }
    }

    private void print(ConsumerRecord<String, Message<User>> record) {
        try {
            System.out.println("---------------------------------------------------");
            System.out.println("PROCESSING REPORT FOR: " + record.value().getPayload().getUuid());

            Message<User> message = record.value();
            User user = message.getPayload();
            File target = IO.newResourceFile(user.getReportName());
            IO.copyTo(IN, target);
            IO.append(target, "Created for: " + user.getUuid());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
