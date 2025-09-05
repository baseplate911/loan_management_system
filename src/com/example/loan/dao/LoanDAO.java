package com.example.loan.dao;

import com.example.loan.model.Loan;
import com.example.loan.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {

    public Loan createLoan(Loan loan) throws SQLException {
        String sql = "INSERT INTO loans (customer_id, principal, annual_rate, term_months, disburse_date, status) VALUES (?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, loan.getCustomerId());
            ps.setDouble(2, loan.getPrincipal());
            ps.setDouble(3, loan.getAnnualRate());
            ps.setInt(4, loan.getTermMonths());
            ps.setDate(5, Date.valueOf(loan.getDisburseDate()));
            ps.setString(6, loan.getStatus() == null ? "PENDING" : loan.getStatus());
            int r = ps.executeUpdate();
            if (r == 1) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) loan.setId(rs.getInt(1));
                }
                return loan;
            }
        }
        return null;
    }

    public Loan findById(int id) throws SQLException {
        String sql = "SELECT * FROM loans WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    public List<Loan> listPending() throws SQLException {
        List<Loan> out = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE status = 'PENDING' ORDER BY disburse_date";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) out.add(map(rs));
        }
        return out;
    }

    public boolean updateStatus(int loanId, String status) throws SQLException {
        String sql = "UPDATE loans SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, loanId);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean setEMIAndOutstanding(int loanId, double emi, double outstanding) throws SQLException {
        String sql = "UPDATE loans SET emi_amount = ?, outstanding_principal = ?, status = 'APPROVED' WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, emi);
            ps.setDouble(2, outstanding);
            ps.setInt(3, loanId);
            return ps.executeUpdate() == 1;
        }
    }

    // Methods that operate using provided Connection (used for transactional repayment)
    public Loan findById(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM loans WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return map(rs);
            }
        }
        return null;
    }

    public boolean updateOutstanding(Connection conn, int loanId, double newOutstanding) throws SQLException {
        String sql = "UPDATE loans SET outstanding_principal = ?, last_updated = CURRENT_TIMESTAMP WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newOutstanding);
            ps.setInt(2, loanId);
            return ps.executeUpdate() == 1;
        }
    }

    public boolean deleteById(int id) throws SQLException {
        String sql = "DELETE FROM loans WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    private Loan map(ResultSet rs) throws SQLException {
        Loan l = new Loan();
        l.setId(rs.getInt("id"));
        l.setCustomerId(rs.getInt("customer_id"));
        l.setPrincipal(rs.getDouble("principal"));
        l.setAnnualRate(rs.getDouble("annual_rate"));
        l.setTermMonths(rs.getInt("term_months"));
        Date d = rs.getDate("disburse_date");
        if (d != null) l.setDisburseDate(d.toLocalDate());
        l.setStatus(rs.getString("status"));
        l.setEmiAmount(rs.getDouble("emi_amount"));
        l.setOutstandingPrincipal(rs.getDouble("outstanding_principal"));
        return l;
    }
}
