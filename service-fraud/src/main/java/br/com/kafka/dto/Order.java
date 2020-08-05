package br.com.kafka.dto;

import java.math.BigDecimal;

public class Order {

    private String id;
    private String userId;
    private BigDecimal amount;

    public Order(String id, String userId, BigDecimal amount) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public BigDecimal getAmount() {
        return amount;
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
