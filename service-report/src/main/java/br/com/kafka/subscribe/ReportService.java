package br.com.kafka.subscribe;

import br.com.kafka.behavior.ConsumerService;
import br.com.kafka.core.IO;
import br.com.kafka.core.StoreLogger;
import br.com.kafka.dto.Message;
import br.com.kafka.dto.User;
import br.com.kafka.service.ServiceRunner;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static br.com.kafka.constants.TopicConfig.STORE_REPORT_USER;

public class ReportService implements ConsumerService<User> {

    private static final InputStream IN = IO.getResourceAsStream("report_user.txt");

    public static void main(String[] args) {
        new ServiceRunner(ReportService::new).start(3);
    }

    @Override
    public String getTopic() {
        return STORE_REPORT_USER;
    }

    @Override
    public Class<?> getConsumerGroup() {
        return ReportService.class;
    }

    @Override
    public void parse(ConsumerRecord<String, Message<User>> record) throws Exception {
        try {
            StoreLogger.info("---------------------------------------------------");
            StoreLogger.info("PROCESSING REPORT FOR: " + record.value().getPayload().getUuid());

            Message<User> message = record.value();
            User user = message.getPayload();
            File target = IO.newResourceFile(user.getReportName());
            IO.copyTo(IN, target);
            IO.append(target, "Created for: " + user.getUuid());

        } catch (IOException e) {
            StoreLogger.severe(e);
        }
    }
}
