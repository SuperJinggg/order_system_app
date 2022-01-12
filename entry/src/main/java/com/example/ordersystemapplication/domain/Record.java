package com.example.ordersystemapplication.domain;


import java.io.Serializable;

public class Record implements Serializable {

    private Integer recordId;

    private String orderId;

    private Integer foodId;

    private Integer foodNum;

    public Record() {
    }

    public Record(Integer recordId, String orderId, Integer foodId, Integer foodNum) {
        this.recordId = recordId;
        this.orderId = orderId;
        this.foodId = foodId;
        this.foodNum = foodNum;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public Integer getFoodNum() {
        return foodNum;
    }

    public void setFoodNum(Integer foodNum) {
        this.foodNum = foodNum;
    }

    @Override
    public String toString() {
        return "Record{" +
                "recordId=" + recordId +
                ", orderId='" + orderId + '\'' +
                ", foodId=" + foodId +
                ", foodNum=" + foodNum +
                '}';
    }
}