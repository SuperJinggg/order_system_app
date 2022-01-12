package com.example.ordersystemapplication.domain;


import java.io.Serializable;

public class Food implements Serializable {
    private Integer foodId;

    private String foodName;

    private Double foodPrice;

    private String foodDesc;

    private String foodPhoto;

    private Integer foodRepertory;

    private Integer categoryId;

    public Food() {
    }


    public Food(Integer foodId, String foodName, Double foodPrice, String foodDesc, String foodPhoto, Integer foodRepertory, Integer categoryId) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodDesc = foodDesc;
        this.foodPhoto = foodPhoto;
        this.foodRepertory = foodRepertory;
        this.categoryId = categoryId;
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

    @Override
    public String toString() {
        return "Food{" +
                "foodId=" + foodId +
                ", foodName='" + foodName + '\'' +
                ", foodPrice=" + foodPrice +
                ", foodDesc='" + foodDesc + '\'' +
                ", foodPhoto='" + foodPhoto + '\'' +
                ", foodRepertory=" + foodRepertory +
                ", categoryId=" + categoryId +
                '}';
    }
}