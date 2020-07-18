package com.home.khalil.smartshop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Product {

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("_rev")
    @Expose
    private String rev;

    @SerializedName("price")
    @Expose
    private int price;

    @SerializedName("image")
    @Expose
    private String image;

    private  int quantity;

    private int totalQuantity;

    public Product(String id, String rev,String title, int price, String image, int totalQuantity){
        this.id = id;
        this.rev = rev;
        this.title = title;
        this.price = price;
        this.image =image;
        this.totalQuantity= totalQuantity;
        quantity=1;


    }





    public String getTitle(){
        return title;
    }

    public int getPrice(){
        return price;
    }

    public String getImage(){
        return image;
    }

    public int getQuantity(){
        return quantity;
    }

    public int getTotalQuantity(){
        return totalQuantity;
    }

    public void setQuantity(int q){
        this.quantity=q;
    }

    public void setTotalQuantity(int q){
        this.totalQuantity=q;
    }
}
