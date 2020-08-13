package br.com.kafka.dto;

public class User {

    private final String uuid;

    public User(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public String getReportName() {
        return this.uuid + "_report.txt";
    }
}
