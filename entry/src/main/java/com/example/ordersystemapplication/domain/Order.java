package com.example.ordersystemapplication.domain;


import java.io.Serializable;

public class Order implements Serializable {

    private String orderId;

    private String custPhone;

    private String tableId;

    private Integer orderState;

    private Double orderPrice;

    private String createTime;

    private String endTime;

    public Order() {
    }

    public Order(String orderId, String custPhone, String tableId, Integer orderState, Double orderPrice, String createTime, String endTime) {
        this.orderId = orderId;
        this.custPhone = custPhone;
        this.tableId = tableId;
        this.orderState = orderState;
        this.orderPrice = orderPrice;
        this.createTime = createTime;
        this.endTime = endTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    public Double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", custPhone='" + custPhone + '\'' +
                ", tableId=" + tableId +
                ", orderState=" + orderState +
                ", orderPrice=" + orderPrice +
                ", createTime='" + createTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}