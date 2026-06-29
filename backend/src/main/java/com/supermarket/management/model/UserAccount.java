package com.supermarket.management.model;

public class UserAccount {
    private String username;
    private String password;
    private String businessName;
    private String role;

    public UserAccount() {
    }

    public UserAccount(String username, String password, String businessName, String role) {
        this.username = username;
        this.password = password;
        this.businessName = businessName;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
