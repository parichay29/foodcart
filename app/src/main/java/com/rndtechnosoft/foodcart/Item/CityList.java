package com.rndtechnosoft.foodcart.Item;

public class CityList {
    String cid, city_name, delivery_amount;
    public CityList(String cid, String city_name, String delivery_amount) {
        this.cid=cid;
        this.city_name=city_name;
        this.delivery_amount=delivery_amount;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getDelivery_amount() {
        return delivery_amount;
    }

    public void setDelivery_amount(String delivery_amount) {
        this.delivery_amount = delivery_amount;
    }
}
