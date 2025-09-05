package com.example.loan.model;

import java.time.LocalDate;

public class Repayment {
    private int id;
    private int loanId;
    private LocalDate paymentDate;
    private double amount;
    private double principalPaid;
    private double interestPaid;
    private String paymentMode;

    public Repayment() {}

    // getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getLoanId() { return loanId; }
    public void setLoanId(int loanId) { this.loanId = loanId; }
    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public double getPrincipalPaid() { return principalPaid; }
    public void setPrincipalPaid(double principalPaid) { this.principalPaid = principalPaid; }
    public double getInterestPaid() { return interestPaid; }
    public void setInterestPaid(double interestPaid) { this.interestPaid = interestPaid; }
    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    @Override
    public String toString() {
        return String.format("Repayment[%d] loan=%d date=%s amount=%.2f principal=%.2f interest=%.2f",
                id, loanId, paymentDate, amount, principalPaid, interestPaid);
    }
}
