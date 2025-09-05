package com.example.loan;

import com.example.loan.dao.CustomerDAO;
import com.example.loan.dao.LoanDAO;
import com.example.loan.model.Customer;
import com.example.loan.model.Loan;
import com.example.loan.service.AdminService;
import com.example.loan.service.LoanService;
import com.example.loan.service.ReportService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final Scanner sc = new Scanner(System.in);
    private static final CustomerDAO customerDAO = new CustomerDAO();
    private static final LoanDAO loanDAO = new LoanDAO();
    private static final LoanService loanService = new LoanService();
    private static final AdminService adminService = new AdminService();
    private static final ReportService reportService = new ReportService();

    public static void main(String[] args) {
        System.out.println("=== Loan Management System ===");

        while (true) {
            try {
                showMenu();
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                int choice = Integer.parseInt(line);
                switch (choice) {
                    case 1:
                        registerCustomer();
                        break;
                    case 2:
                        applyLoan();
                        break;
                    case 3:
                        adminApproveReject();
                        break;
                    case 4:
                        makeRepayment();
                        break;
                    case 5:
                        viewLoanStatus();
                        break;
                    case 6:
                        showReports();
                        break;
                    case 7:
                        deleteCustomer();
                        break;
                    case 8:
                        deleteLoan();
                        break;
                    case 0:
                        System.out.println("Exiting...");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                // e.printStackTrace();
            }
        }
    }

    private static void showMenu() {
        System.out.println("\n1) Register Customer");
        System.out.println("2) Apply Loan");
        System.out.println("3) Admin: Approve / Reject Loans");
        System.out.println("4) Make Repayment");
        System.out.println("5) View Loan Status / Repayments");
        System.out.println("6) Reports (summary)");
        System.out.println("7) Delete Customer");
        System.out.println("8) Delete Loan");
        System.out.println("0) Exit");
        System.out.print("Choose: ");
    }

    private static void registerCustomer() throws Exception {
        System.out.print("Full name: "); String name = sc.nextLine().trim();
        System.out.print("Email: "); String email = sc.nextLine().trim();
        System.out.print("Phone: "); String phone = sc.nextLine().trim();
        System.out.print("Aadhar: "); String aadhar = sc.nextLine().trim();

        Customer c = new Customer();
        c.setFullName(name);
        c.setEmail(email);
        c.setPhone(phone);
        c.setAadhar(aadhar);

        Customer saved = customerDAO.create(c);
        if (saved != null) {
            System.out.println("Customer registered: ID = " + saved.getId());
        } else {
            System.out.println("Registration failed");
        }
    }

    private static void applyLoan() throws Exception {
        System.out.print("Customer ID: "); int cid = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Principal amount: "); double principal = Double.parseDouble(sc.nextLine().trim());
        System.out.print("Annual rate (percent): "); double rate = Double.parseDouble(sc.nextLine().trim());
        System.out.print("Term (months): "); int months = Integer.parseInt(sc.nextLine().trim());

        Loan loan = new Loan();
        loan.setCustomerId(cid);
        loan.setPrincipal(principal);
        loan.setAnnualRate(rate);
        loan.setTermMonths(months);
        loan.setDisburseDate(LocalDate.now());
        loan.setStatus("PENDING");

        Loan saved = loanService.applyLoan(loan);
        if (saved != null) System.out.println("Loan applied. Loan ID = " + saved.getId());
        else System.out.println("Loan application failed");
    }

    private static void adminApproveReject() throws Exception {
        List<Loan> pend = adminService.listPendingLoans();
        if (pend.isEmpty()) {
            System.out.println("No pending loans.");
            return;
        }
        System.out.println("Pending loans:");
        for (Loan l : pend) System.out.println(l);

        System.out.print("Enter loan id to process: "); int id = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Approve (A) or Reject (R)? "); String op = sc.nextLine().trim().toUpperCase();
        if ("A".equals(op)) {
            boolean ok = adminService.approve(id);
            System.out.println(ok ? "Loan approved" : "Approve failed");
        } else if ("R".equals(op)) {
            boolean ok = adminService.reject(id);
            System.out.println(ok ? "Loan rejected" : "Reject failed");
        } else {
            System.out.println("Unknown op");
        }
    }

    private static void makeRepayment() throws Exception {
        System.out.print("Loan ID: "); int loanId = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Amount paid: "); double amount = Double.parseDouble(sc.nextLine().trim());
        System.out.print("Payment mode (CASH/UPI/NETBANKING): "); String mode = sc.nextLine().trim();
        boolean ok = loanService.makeRepayment(loanId, amount, mode);
        System.out.println(ok ? "Payment recorded" : "Payment failed");
    }

    private static void viewLoanStatus() throws Exception {
        System.out.print("Loan ID: "); int id = Integer.parseInt(sc.nextLine().trim());
        Loan l = loanDAO.findById(id);
        if (l == null) {
            System.out.println("Loan not found");
            return;
        }
        System.out.println(l);

        // show repayments
        com.example.loan.dao.RepaymentDAO repDao = new com.example.loan.dao.RepaymentDAO();
        java.util.List<com.example.loan.model.Repayment> reps = repDao.listByLoan(id);
        if (reps.isEmpty()) System.out.println("No repayments yet");
        else {
            System.out.println("Repayments:");
            for (com.example.loan.model.Repayment r : reps) System.out.println(r);
        }
    }

    private static void showReports() throws Exception {
        System.out.println(reportService.totalApprovedLoansSummary());
        System.out.println(reportService.defaultRate());
        System.out.println("Monthly collections (last 12 months):");
        reportService.printMonthlyCollections();
    }

    private static void deleteCustomer() {
        try {
            System.out.print("Enter Customer ID to delete: ");
            int id = Integer.parseInt(sc.nextLine().trim());
            boolean ok = customerDAO.deleteById(id);
            System.out.println(ok ? "Customer deleted." : "Customer not found or not deleted.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void deleteLoan() {
        try {
            System.out.print("Enter Loan ID to delete: ");
            int id = Integer.parseInt(sc.nextLine().trim());
            boolean ok = loanDAO.deleteById(id);
            System.out.println(ok ? "Loan deleted." : "Loan not found or not deleted.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
