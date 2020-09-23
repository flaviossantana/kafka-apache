package br.com.kafka.dto;

public class Message<T> {

    private CorrelationId correlation;
    private T payload;

    public Message(CorrelationId id, T payload) {
        this.correlation = id;
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + correlation +
                ", payload=" + payload +
                '}';
    }

    public T getPayload() {
        return payload;
    }

    public CorrelationId getCorrelation() {
        return correlation;
    }
}
