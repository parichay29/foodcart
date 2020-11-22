package com.rndtechnosoft.foodcart.Item;

import java.io.Serializable;

public class MenuList implements Serializable {

    private String cid,category_name,category_image,cat_type,category_image_thumb;

    public MenuList(String cid, String category_name, String category_image, String cat_type, String category_image_thumb) {
        this.cid = cid;
        this.category_name = category_name;
        this.category_image = category_image;
        this.cat_type = cat_type;
        this.category_image_thumb = category_image_thumb;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }

    public String getCategory_image_thumb() {
        return category_image_thumb;
    }

    public void setCategory_image_thumb(String category_image_thumb) {
        this.category_image_thumb = category_image_thumb;
    }

    public String getCat_type() {
        return cat_type;
    }

    public void setCat_type(String cat_type) {
        this.cat_type = cat_type;
    }
}
