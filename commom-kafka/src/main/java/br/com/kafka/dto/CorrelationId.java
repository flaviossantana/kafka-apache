package br.com.kafka.dto;

import java.util.UUID;

public class CorrelationId {

    private final String id;

    public CorrelationId(String className) {
        this.id = className +"("+UUID.randomUUID().toString() + ")";
    }

    @Override
    public String toString() {
        return "CorrelationId{" +
                "id='" + id + '\'' +
                '}';
    }

    public CorrelationId addParent(String className) {
        return new CorrelationId(id +"|"+ className);
    }
}
