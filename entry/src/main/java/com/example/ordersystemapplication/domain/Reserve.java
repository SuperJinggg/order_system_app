package com.example.ordersystemapplication.domain;


import java.io.Serializable;

public class Reserve implements Serializable {

    private Integer reserveId;

    private String custPhone;

    private String tableId;

    private String startTime;

    private String endTime;

    public Reserve() {
    }

    public Reserve(Integer reserveId, String custPhone, String tableId, String startTime, String endTime) {
        this.reserveId = reserveId;
        this.custPhone = custPhone;
        this.tableId = tableId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public Integer getReserveId() {
        return reserveId;
    }

    public void setReserveId(Integer reserveId) {
        this.reserveId = reserveId;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }



    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Reserve{" +
                "reserveId=" + reserveId +
                ", custPhone='" + custPhone + '\'' +
                ", tableId=" + tableId +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}