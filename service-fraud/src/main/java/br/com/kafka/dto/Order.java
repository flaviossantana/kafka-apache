package br.com.kafka.dto;

import java.math.BigDecimal;

public class Order {

    private String id;
    private String email;
    private BigDecimal amount;

    public Order(String id, String userId, BigDecimal amount) {
        this.id = id;
        this.amount = amount;
    }

    public String getEmail() {
        return email;
    }

    public BigDecimal getAmount() {
        return amount;
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
