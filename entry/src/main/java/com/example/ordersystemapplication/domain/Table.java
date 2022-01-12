package com.example.ordersystemapplication.domain;


import java.io.Serializable;

public class Table implements Serializable {
    private String tableId;

    private Integer tableState;

    private Integer fullPeople;

    private Double tablePrice;

    public Table() {
    }

    public Table(String tableId, Integer tableState, Integer fullPeople, Double tablePrice) {
        this.tableId = tableId;
        this.tableState = tableState;
        this.fullPeople = fullPeople;
        this.tablePrice = tablePrice;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public Integer getTableState() {
        return tableState;
    }

    public void setTableState(Integer tableState) {
        this.tableState = tableState;
    }

    public Integer getFullPeople() {
        return fullPeople;
    }

    public void setFullPeople(Integer fullPeople) {
        this.fullPeople = fullPeople;
    }

    public Double getTablePrice() {
        return tablePrice;
    }

    public void setTablePrice(Double tablePrice) {
        this.tablePrice = tablePrice;
    }

    @Override
    public String toString() {
        return "Table{" +
                "tableId=" + tableId +
                ", tableState=" + tableState +
                ", fullPeople=" + fullPeople +
                ", tablePrice=" + tablePrice +
                '}';
    }
}