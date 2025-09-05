package com.example.loan.model;

import java.time.LocalDate;

public class Loan {
    private int id;
    private int customerId;
    private double principal;
    private double annualRate;
    private int termMonths;
    private LocalDate disburseDate;
    private String status;
    private double emiAmount;
    private double outstandingPrincipal;

    public Loan() {}

    // getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public double getPrincipal() { return principal; }
    public void setPrincipal(double principal) { this.principal = principal; }
    public double getAnnualRate() { return annualRate; }
    public void setAnnualRate(double annualRate) { this.annualRate = annualRate; }
    public int getTermMonths() { return termMonths; }
    public void setTermMonths(int termMonths) { this.termMonths = termMonths; }
    public LocalDate getDisburseDate() { return disburseDate; }
    public void setDisburseDate(LocalDate disburseDate) { this.disburseDate = disburseDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getEmiAmount() { return emiAmount; }
    public void setEmiAmount(double emiAmount) { this.emiAmount = emiAmount; }
    public double getOutstandingPrincipal() { return outstandingPrincipal; }
    public void setOutstandingPrincipal(double outstandingPrincipal) { this.outstandingPrincipal = outstandingPrincipal; }

    @Override
    public String toString() {
        return String.format("Loan[%d] cust=%d principal=%.2f rate=%.2f%% term=%dmo status=%s emi=%.2f outstanding=%.2f",
                id, customerId, principal, annualRate, termMonths, status, emiAmount, outstandingPrincipal);
    }
}
