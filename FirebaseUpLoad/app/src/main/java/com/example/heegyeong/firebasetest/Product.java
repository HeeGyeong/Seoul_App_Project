package com.example.heegyeong.firebasetest;

import android.graphics.Bitmap;

import java.net.URL;

/**
 * Created by Heegyeong on 2016-11-17.
 */
public class Product {

    public String proName;
    public String proPrice;
    public String proEvent;
    public String proUrl;
    public String proCategory;

    public Product() {

    }
    public Product(String proName, String proPrice, String proEvent, String proUrl, String proCategory){
        this.proName = proName;
        this.proPrice = proPrice;
        this.proEvent = proEvent;
        this.proUrl = proUrl;
        this.proCategory = proCategory;

    }

    public String getproName() {
        return proName;
    }
    public String getproPrice() {
        return proPrice;
    }
    public String getproEvent() { return proEvent;}
    public String getproUrl() { return proUrl;}
    public String getproCategory () { return proCategory;}


}