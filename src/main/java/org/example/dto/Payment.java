package org.example.dto;

import java.time.Instant;
import java.util.UUID;

public class Payment {
    private UUID id;
    private double amount;
    private String status;
    private String paymentMethod;
    private Instant createdAt;
    private String environment;

    // Constructors
    public Payment() {}

    public Payment(double amount, String status, String paymentMethod) {
        this.amount = amount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.createdAt = Instant.now();
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }

    @Override
    public String toString() {
        return String.format("Payment{id=%s, amount=%.2f, status='%s', method='%s', created=%s}",
                id, amount, status, paymentMethod, createdAt);
    }
}