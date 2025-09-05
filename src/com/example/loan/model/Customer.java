package com.example.loan.model;

public class Customer {
    private int id;
    private String fullName;
    private String email;
    private String phone;
    private String aadhar;

    public Customer() {}

    public Customer(int id, String fullName, String email, String phone, String aadhar) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.aadhar = aadhar;
    }

    // getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAadhar() { return aadhar; }
    public void setAadhar(String aadhar) { this.aadhar = aadhar; }

    @Override
    public String toString() {
        return String.format("[%d] %s | %s | %s", id, fullName, email == null ? "-" : email, phone == null ? "-" : phone);
    }
}
