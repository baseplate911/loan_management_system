package com.example.loan.service;

import com.example.loan.dao.LoanDAO;
import com.example.loan.model.Loan;

import java.util.List;

public class AdminService {
    private LoanDAO loanDAO = new LoanDAO();

    public List<Loan> listPendingLoans() throws Exception {
        return loanDAO.listPending();
    }

    public boolean approve(int loanId) throws Exception {
        return new LoanService().approveLoan(loanId);
    }

    public boolean reject(int loanId) throws Exception {
        return new LoanService().rejectLoan(loanId);
    }
}
