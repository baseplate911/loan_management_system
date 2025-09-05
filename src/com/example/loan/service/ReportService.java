package com.example.loan.service;

import com.example.loan.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReportService {

    /**
     * Total approved loans and total principal
     */
    public String totalApprovedLoansSummary() throws Exception {
        String sql = "SELECT COUNT(*) AS loan_count, IFNULL(SUM(principal),0) AS total_principal FROM loans WHERE status='APPROVED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int count = rs.getInt("loan_count");
                double sum = rs.getDouble("total_principal");
                return String.format("Approved loans: %d | Total principal: %.2f", count, sum);
            }
        }
        return "No data";
    }

    /**
     * Default rate: defaults / total loans
     */
    public String defaultRate() throws Exception {
        String sql = "SELECT SUM(status='DEFAULTED') AS defaults, COUNT(*) AS total FROM loans";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int defs = rs.getInt("defaults");
                int tot = rs.getInt("total");
                double rate = tot == 0 ? 0.0 : (defs * 100.0 / tot);
                return String.format("Defaulted: %d / %d (%.2f%%)", defs, tot, rate);
            }
        }
        return "No data";
    }

    /**
     * Monthly collections (last 12 months simplified)
     */
    public void printMonthlyCollections() throws Exception {
        String sql = "SELECT DATE_FORMAT(payment_date, '%Y-%m') AS ym, IFNULL(SUM(amount),0) AS collected FROM repayments GROUP BY ym ORDER BY ym DESC LIMIT 12";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            System.out.println("Month | Collected");
            while (rs.next()) {
                System.out.printf("%s | %.2f%n", rs.getString("ym"), rs.getDouble("collected"));
            }
        }
    }
}
