package com.example.navdrawertest10;

public class ModelNotification {
    private String imageUrl;
    private String orderStatus;
    private String dateTime;
    private String notificationSen;
    private String currentTime;


    public ModelNotification() {
    }

    public ModelNotification(String dateTime, String imageUrl, String notificationSen, String orderStatus, String currentTime) {
        this.imageUrl = imageUrl;
        this.orderStatus = orderStatus;
        this.dateTime = dateTime;
        this.notificationSen = notificationSen;
        this.currentTime = currentTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getNotificationSen() {
        return notificationSen;
    }

    public void setNotificationSen(String notificationSen) {
        this.notificationSen = notificationSen;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }
}
