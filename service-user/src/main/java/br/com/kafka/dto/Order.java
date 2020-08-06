package br.com.kafka.dto;

import java.math.BigDecimal;

public class Order {

    private String id;
    private String email;
    private BigDecimal amount;

    public Order(String id, BigDecimal amount, String email) {
        this.id = id;
        this.amount = amount;
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                '}';
    }
}
