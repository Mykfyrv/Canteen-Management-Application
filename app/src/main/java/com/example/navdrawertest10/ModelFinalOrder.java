package com.example.navdrawertest10;

public class ModelFinalOrder {
    private String imageUrl;
    private String foodName;
    private String foodPrice;
    private String foodQuantity;
    private String foodId;
    private String orderId;
    private String customerName;
    private String tableNo;
    private String dateOrder;
    private String status;
    private String currentTime;
    private String totalPrice;

    public ModelFinalOrder(){


    }

    public ModelFinalOrder(String orderId, String customerName, String foodId, String foodName, String foodPrice, String foodQuantity, String imageUrl, String tableNo, String dateOrder, String status, String currentTime, String totalPrice){
        this.imageUrl = imageUrl;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodQuantity = foodQuantity;
        this.foodId = foodId;
        this.orderId = orderId;
        this.customerName = customerName;
        this.tableNo = tableNo;
        this.dateOrder = dateOrder;
        this.status = status;
        this.currentTime = currentTime;
        this.totalPrice = totalPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getFoodQuantity() {
        return foodQuantity;
    }

    public void setFoodQuantity(String foodQuantity) {
        this.foodQuantity = foodQuantity;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public String getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(String dateOrder) {
        this.dateOrder = dateOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
