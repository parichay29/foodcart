package com.rndtechnosoft.foodcart.Item;

import java.io.Serializable;

public class HomeList implements Serializable {

    private String id, banner_image, hotel_name, hotel_address;

    public HomeList(String id, String banner_image, String hotel_name, String hotel_address) {
        this.id = id;
        this.banner_image = banner_image;
        this.hotel_name = hotel_name;
        this.hotel_address = hotel_address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBanner_image() {
        return banner_image;
    }

    public void setBanner_image(String banner_image) {
        this.banner_image = banner_image;
    }

    public String getHotel_name() {
        return hotel_name;
    }

    public void setHotel_name(String hotel_name) {
        this.hotel_name = hotel_name;
    }

    public String getHotel_address() {
        return hotel_address;
    }

    public void setHotel_address(String hotel_address) {
        this.hotel_address = hotel_address;
    }
}
