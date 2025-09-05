package com.example.loan.dao;

import com.example.loan.model.Repayment;
import com.example.loan.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RepaymentDAO {

    public Repayment createRepayment(Repayment r) throws SQLException {
        String sql = "INSERT INTO repayments (loan_id, payment_date, amount, principal_paid, interest_paid, payment_mode) VALUES (?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, r.getLoanId());
            ps.setDate(2, Date.valueOf(r.getPaymentDate()));
            ps.setDouble(3, r.getAmount());
            ps.setDouble(4, r.getPrincipalPaid());
            ps.setDouble(5, r.getInterestPaid());
            ps.setString(6, r.getPaymentMode());
            int k = ps.executeUpdate();
            if (k == 1) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) r.setId(rs.getInt(1));
                }
                return r;
            }
        }
        return null;
    }

    // Transactional version using provided connection
    public Repayment createRepayment(Connection conn, Repayment r) throws SQLException {
        String sql = "INSERT INTO repayments (loan_id, payment_date, amount, principal_paid, interest_paid, payment_mode) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, r.getLoanId());
            ps.setDate(2, Date.valueOf(r.getPaymentDate()));
            ps.setDouble(3, r.getAmount());
            ps.setDouble(4, r.getPrincipalPaid());
            ps.setDouble(5, r.getInterestPaid());
            ps.setString(6, r.getPaymentMode());
            int k = ps.executeUpdate();
            if (k == 1) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) r.setId(rs.getInt(1));
                }
                return r;
            }
        }
        return null;
    }

    public List<Repayment> listByLoan(int loanId) throws SQLException {
        List<Repayment> out = new ArrayList<>();
        String sql = "SELECT * FROM repayments WHERE loan_id = ? ORDER BY payment_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, loanId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Repayment r = new Repayment();
                    r.setId(rs.getInt("id"));
                    r.setLoanId(rs.getInt("loan_id"));
                    Date pd = rs.getDate("payment_date");
                    if (pd != null) r.setPaymentDate(pd.toLocalDate());
                    r.setAmount(rs.getDouble("amount"));
                    r.setPrincipalPaid(rs.getDouble("principal_paid"));
                    r.setInterestPaid(rs.getDouble("interest_paid"));
                    r.setPaymentMode(rs.getString("payment_mode"));
                    out.add(r);
                }
            }
        }
        return out;
    }
}
