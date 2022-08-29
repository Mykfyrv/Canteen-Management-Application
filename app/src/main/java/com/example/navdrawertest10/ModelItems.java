package com.example.navdrawertest10;

public class ModelItems {
    String id, itemname, itemquantity, date;
    public ModelItems(){}

    public ModelItems(String id, String itemname, String itemquantity, String date) {
        this.id=id;
        this.itemname=itemname;
        this.itemquantity=itemquantity;
        this.date=date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String username) {
        this.itemname = itemname;
    }

    public String getItemquantity() {
        return itemquantity;
    }

    public void setItemquantity(String password) {
        this.itemquantity = itemquantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String password) {
        this.date = date;
    }
}
