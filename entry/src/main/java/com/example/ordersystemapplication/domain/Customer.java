package com.example.ordersystemapplication.domain;


import java.io.Serializable;

public class Customer implements Serializable {
    private String custPhone;

    private String password;

    private String custName;

    private Double custBalance;

    public Customer() {
    }

    public Customer(String custPhone, String password, String custName, Double custBalance) {
        this.custPhone = custPhone;
        this.password = password;
        this.custName = custName;
        this.custBalance = custBalance;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public Double getCustBalance() {
        return custBalance;
    }

    public void setCustBalance(Double custBalance) {
        this.custBalance = custBalance;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "custPhone='" + custPhone + '\'' +
                ", password='" + password + '\'' +
                ", custName='" + custName + '\'' +
                ", custBalance=" + custBalance +
                '}';
    }
}