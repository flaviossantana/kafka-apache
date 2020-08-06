package br.com.kafka.dto;

import java.math.BigDecimal;

public class Order {

    private String id;
    private String email;
    private BigDecimal amount;

    public Order(String id, String email, BigDecimal amount) {
        this.id = id;
        this.email = email;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", amount=" + amount +
                '}';
    }
}
