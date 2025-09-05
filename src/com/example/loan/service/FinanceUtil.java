package com.example.loan.service;

public class FinanceUtil {

    /**
     * Compute EMI for fixed-rate loan
     * principal P, annualRate in percent (e.g., 10.5), months n
     */
    public static double computeEMI(double principal, double annualRate, int months) {
        double r = annualRate / 12.0 / 100.0;
        if (r == 0) {
            return round(principal / months);
        }
        double pow = Math.pow(1 + r, months);
        double emi = principal * r * pow / (pow - 1);
        return round(emi);
    }

    public static double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
