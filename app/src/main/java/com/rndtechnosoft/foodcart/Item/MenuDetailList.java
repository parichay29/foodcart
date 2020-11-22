package com.rndtechnosoft.foodcart.Item;

import java.io.Serializable;
import java.util.ArrayList;

public class MenuDetailList implements Serializable {

    private String mId,cid, name, image,desc,price,min,max,food_type, cat_type, image_thumb,cat_food_type,kg,start_time,end_time,food_time_msg;

    public ArrayList<String> getFlavour_id() {
        return flavour_id;
    }

    public void setFlavour_id(ArrayList<String> flavour_id) {
        this.flavour_id = flavour_id;
    }

    ArrayList<String> flavour_id,flavour_name;

    public MenuDetailList(String mId, String cid, String name, String desc, String price, String min, String max, String food_type, String cat_type, String image, String image_thumb, String start_time, String end_time, String cat_food_type, ArrayList<String> flavour_id, ArrayList<String> flavour_name, String food_time_msg) {
        this.mId = mId;
        this.cid = cid;
        this.desc = desc;
        this.price = price;
        this.min = min;
        this.max = max;
        this.food_type = food_type;
        this.cat_type = cat_type;
        this.name = name;
        this.image = image;
        this.image_thumb = image_thumb;
        this.start_time = start_time;
        this.end_time = end_time;
        this.cat_food_type = cat_food_type;
        this.flavour_id = flavour_id;
        this.flavour_name = flavour_name;
        this.food_time_msg = food_time_msg;
    }

    public String getKg() {
        return kg;
    }

    public void setKg(String kg) {
        this.kg = kg;
    }

    public String getCat_food_type() {
        return cat_food_type;
    }

    public void setCat_food_type(String cat_food_type) {
        this.cat_food_type = cat_food_type;
    }

    public String getFood_type() {
        return food_type;
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
    }

    public String getmId() {
        return mId;
    }

    public String getCat_type() {
        return cat_type;
    }

    public void setCat_type(String cat_type) {
        this.cat_type = cat_type;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_thumb() {
        return image_thumb;
    }

    public void setImage_thumb(String image_thumb) {
        this.image_thumb = image_thumb;
    }

    public ArrayList<String> getFlavour_name() {
        return flavour_name;
    }

    public void setFlavour_name(ArrayList<String> flavour_name) {
        this.flavour_name = flavour_name;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getFood_time_msg() {
        return food_time_msg;
    }

    public void setFood_time_msg(String food_time_msg) {
        this.food_time_msg = food_time_msg;
    }
}
