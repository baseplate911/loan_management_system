package com.example.loan.service;

import com.example.loan.dao.LoanDAO;
import com.example.loan.dao.RepaymentDAO;
import com.example.loan.model.Loan;
import com.example.loan.model.Repayment;
import com.example.loan.util.DBConnection;

import java.sql.Connection;
import java.time.LocalDate;

public class LoanService {
    private LoanDAO loanDAO = new LoanDAO();
    private RepaymentDAO repaymentDAO = new RepaymentDAO();

    /**
     * Customer applies for loan (creates loan in PENDING state).
     */
    public Loan applyLoan(Loan loan) throws Exception {
        loan.setStatus("PENDING");
        return loanDAO.createLoan(loan);
    }

    /**
     * Approve loan: computes EMI and sets outstanding principal equal to principal.
     */
    public boolean approveLoan(int loanId) throws Exception {
        Loan l = loanDAO.findById(loanId);
        if (l == null || !"PENDING".equals(l.getStatus())) return false;
        double emi = FinanceUtil.computeEMI(l.getPrincipal(), l.getAnnualRate(), l.getTermMonths());
        double outstanding = l.getPrincipal();
        return loanDAO.setEMIAndOutstanding(loanId, emi, outstanding);
    }

    /**
     * Reject loan.
     */
    public boolean rejectLoan(int loanId) throws Exception {
        return loanDAO.updateStatus(loanId, "REJECTED");
    }

    /**
     * Make repayment in transaction:
     * - compute interest = outstanding * monthly_rate
     * - principal_paid = amount - interest (if negative, interest-only)
     * - update repayments table and loan.outstanding_principal
     * - if outstanding becomes <= epsilon, set status CLOSED
     */
    public boolean makeRepayment(int loanId, double amount, String paymentMode) throws Exception {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            Loan loan = loanDAO.findById(conn, loanId);
            if (loan == null) throw new IllegalArgumentException("Loan not found");
            if (!"APPROVED".equals(loan.getStatus()) && !"PENDING".equals(loan.getStatus())) {
                throw new IllegalStateException("Loan not in repayable state: " + loan.getStatus());
            }
            double outstanding = loan.getOutstandingPrincipal();
            if (outstanding <= 0) {
                throw new IllegalStateException("Loan already closed");
            }

            double monthlyRate = loan.getAnnualRate() / 12.0 / 100.0;
            double interest = round(outstanding * monthlyRate);
            double principalPaid = amount - interest;
            if (principalPaid < 0) {
                // Payment does not even cover interest; all goes to interest
                principalPaid = 0;
            }
            double newOutstanding = round(Math.max(0.0, outstanding - principalPaid));

            // create repayment row
            Repayment r = new Repayment();
            r.setLoanId(loanId);
            r.setPaymentDate(LocalDate.now());
            r.setAmount(amount);
            r.setInterestPaid(interest);
            r.setPrincipalPaid(principalPaid);
            r.setPaymentMode(paymentMode == null ? "CASH" : paymentMode);

            repaymentDAO.createRepayment(conn, r);

            // update loan outstanding
            loanDAO.updateOutstanding(conn, loanId, newOutstanding);

            // if closed
            if (newOutstanding <= 0.001) {
                try (java.sql.PreparedStatement ps = conn.prepareStatement("UPDATE loans SET status = 'CLOSED' WHERE id = ?")) {
                    ps.setInt(1, loanId);
                    ps.executeUpdate();
                }
            }

            conn.commit();
            return true;
        } catch (Exception ex) {
            if (conn != null) conn.rollback();
            throw ex;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    private double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
