package com.example.ordersystemapplication.domain;

import java.io.Serializable;

public class OrderFood implements Serializable {
    private Integer foodId;

    private String foodName;

    private Double foodPrice;

    private String foodDesc;

    private String foodPhoto;

    private Integer foodRepertory;

    private Integer categoryId;

    private Integer foodNum;

    @Override
    public String toString() {
        return "OrderFood{" +
                "foodId=" + foodId +
                ", foodName='" + foodName + '\'' +
                ", foodPrice=" + foodPrice +
                ", foodDesc='" + foodDesc + '\'' +
                ", foodPhoto='" + foodPhoto + '\'' +
                ", foodRepertory=" + foodRepertory +
                ", categoryId=" + categoryId +
                ", foodNum=" + foodNum +
                '}';
    }

    public OrderFood() {
    }


    public OrderFood(Integer foodId, String foodName, Double foodPrice, String foodDesc, Integer foodNum) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodDesc = foodDesc;
        this.foodNum = foodNum;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Double getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(Double foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getFoodDesc() {
        return foodDesc;
    }

    public void setFoodDesc(String foodDesc) {
        this.foodDesc = foodDesc;
    }

    public String getFoodPhoto() {
        return foodPhoto;
    }

    public void setFoodPhoto(String foodPhoto) {
        this.foodPhoto = foodPhoto;
    }

    public Integer getFoodRepertory() {
        return foodRepertory;
    }

    public void setFoodRepertory(Integer foodRepertory) {
        this.foodRepertory = foodRepertory;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getFoodNum() {
        return foodNum;
    }

    public void setFoodNum(Integer foodNum) {
        this.foodNum = foodNum;
    }
}