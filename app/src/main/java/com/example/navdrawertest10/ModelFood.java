package com.example.navdrawertest10;

public class ModelFood {
    private String imageUrl;
    private String foodName;
    private String foodPrice;
    private String foodQuantity;
    private String foodId;
    private String currentTime;

    public ModelFood(){
    }

    public ModelFood(String foodId, String foodName, String foodPrice, String foodQuantity, String imageUrl){
        this.imageUrl = imageUrl;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodQuantity = foodQuantity;
        this.foodId = foodId;
        this.currentTime = currentTime;
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
}
