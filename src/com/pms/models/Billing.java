package com.pms.models;

public class Billing {
    private String billing_id;
    private double total_amount;
    private double amount_paid;
    private PaymentStatus status;

    public Billing(String billing_id, double total_amount) {
        this.billing_id = billing_id;
        this.total_amount = total_amount;
        this.amount_paid = 0.0;
        this.status = PaymentStatus.PENDING;
    }

    public void record_payment(double amount) {
        this.amount_paid += amount;
        if (this.amount_paid >= this.total_amount) {
            this.status = PaymentStatus.PAID;
        } else if (this.amount_paid > 0) {
            this.status = PaymentStatus.PARTIAL;
        }
    }

    public String generate_invoice() {
        return "Invoice " + billing_id + " - Total: " + total_amount + ", Paid: " + amount_paid + ", Status: " + status;
    }

    public double balance_due() {
        return total_amount - amount_paid;
    }

    // Getters and Setters
    public String getBilling_id() {
        return billing_id;
    }

    public void setBilling_id(String billing_id) {
        this.billing_id = billing_id;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public double getAmount_paid() {
        return amount_paid;
    }

    public void setAmount_paid(double amount_paid) {
        this.amount_paid = amount_paid;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}
