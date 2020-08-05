package br.com.kafka.dto;

import java.math.BigDecimal;

public class Order {

    private String id;
    private String userId;
    private String email;
    private BigDecimal amount;

    public Order(String id, String userId, String email, BigDecimal amount) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", amount=" + amount +
                '}';
    }
}
