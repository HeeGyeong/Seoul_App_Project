package com.example.heegyeong.project_second;

import android.graphics.Bitmap;

import java.net.URL;

/**
 * Created by Heegyeong on 2016-11-05.
 */
public class Product {

    public Bitmap img;
    public URL img_url;

    ////////////////////////////////////////////////////// DB에 저장된 값들.
    public String proName;
    public String proPrice;
    public String proEvent;
    public String proUrl;
    public String proCategory;


    public Product(String proName, String proPrice, String proEvent, String proUrl,  String proCategory){
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
    ////////////////////////////////////////////////////////

    public Product() {

    }
    public Product(String proName, String proPrice, String proEvent, Bitmap img, URL img_url, String proCategory){
        this.proName = proName;
        this.proPrice = proPrice;
        this.img = img;
        this.img_url = img_url;
        this.proEvent = proEvent;

        this.proCategory = proCategory;
    }


    public String getText1() {
        return proName;
    }
    public String getText2() {
        return proPrice;
    }
    public Bitmap getImg() {
        return img;
    }
    public URL getUrl() {
        return img_url;
    }

    public String getText3(){return proEvent;}

}