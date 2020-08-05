package br.com.kafka.dto;

import java.math.BigDecimal;

public class Order {

    private String id;
    private String userId;
    private String email;
    private BigDecimal amount;

    public Order(String id, String userId, BigDecimal amount, String email) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return this.email;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", amount=" + amount +
                '}';
    }
}
