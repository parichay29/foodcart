package com.rndtechnosoft.foodcart.Item;

public class ScratchList {

    String id,title,message,sorrymsg,congratsmsg,amount,type,coupon_text,image,sc_image,status;

    public ScratchList(String id, String title, String message, String sorrymsg, String congratsmsg, String amount, String type, String coupon_text, String image, String sc_image, String status) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.sorrymsg = sorrymsg;
        this.congratsmsg = congratsmsg;
        this.amount = amount;
        this.type = type;
        this.coupon_text = coupon_text;
        this.image = image;
        this.sc_image = sc_image;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCoupon_text() {
        return coupon_text;
    }

    public void setCoupon_text(String coupon_text) {
        this.coupon_text = coupon_text;
    }

    public String getSc_image() {
        return sc_image;
    }

    public void setSc_image(String sc_image) {
        this.sc_image = sc_image;
    }

    public String getSorrymsg() {
        return sorrymsg;
    }

    public void setSorrymsg(String sorrymsg) {
        this.sorrymsg = sorrymsg;
    }

    public String getCongratsmsg() {
        return congratsmsg;
    }

    public void setCongratsmsg(String congratsmsg) {
        this.congratsmsg = congratsmsg;
    }
}
