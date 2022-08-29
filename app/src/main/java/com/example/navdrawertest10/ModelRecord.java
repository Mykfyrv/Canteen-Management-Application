package com.example.navdrawertest10;

public class ModelRecord {
    String foodName;
    String todayTotal;
    String todayTotalOrder;
    String imageUrl;

    public ModelRecord() {
    }

    public ModelRecord(String imageUrl, String foodName, String todayTotal, String todayTotalOrder){
        this.imageUrl = imageUrl;
        this.foodName = foodName;
        this.todayTotal = todayTotal;
        this.todayTotalOrder = todayTotalOrder;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getTodayTotal() {
        return todayTotal;
    }

    public void setTodayTotal(String todayTotal) {
        this.todayTotal = todayTotal;
    }

    public String getTodayTotalOrder() {
        return todayTotalOrder;
    }

    public void setTodayTotalOrder(String todayTotalOrder) {
        this.todayTotalOrder = todayTotalOrder;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}


