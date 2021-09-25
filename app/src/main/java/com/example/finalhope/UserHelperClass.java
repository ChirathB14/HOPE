package com.example.finalhope;

public class UserHelperClass {

    String type, fullName, username, email, phoneNo, nic_reg, password;

    public UserHelperClass() {
    }

    public UserHelperClass(String type, String fullName, String username, String email, String phoneNo, String nic_reg, String password) {
        this.type = type;
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.phoneNo = phoneNo;
        this.nic_reg = nic_reg;
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getNic_reg() {
        return nic_reg;
    }

    public void setNic_reg(String nic_reg) {
        this.nic_reg = nic_reg;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
