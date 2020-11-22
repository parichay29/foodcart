package com.rndtechnosoft.foodcart.Item;

public class SubCityList {
    String id, cid, city_name;
    public SubCityList(String id, String cid, String city_name) {
        this.id=id;
        this.cid=cid;
        this.city_name=city_name;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
