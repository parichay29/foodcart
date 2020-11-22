package com.rndtechnosoft.foodcart.Item;

import java.io.Serializable;

public class MyOrderList implements Serializable {

    private String id;
    private String mobileid;
    private String name;
    private String address;
    private String phone;
    private String order_list;
    private String datetime;
    private String status;
    private String comment;
    private String email;
    private String order_type;
    private String del_msg;
    private String del_time;
    private String del_done;
    private String cat_type;
    private String adtime;
    private String addate;
    private String admsg;


    public MyOrderList(String id, String mobileid, String name, String address,
                       String phone, String order_list, String datetime, String status,
                       String comment, String email, String order_type, String del_msg, String del_time,String del_done,String cat_type,
                        String adtime, String addate, String admsg)
    {
        this.id=id;
        this.mobileid=mobileid;
        this.name=name;
        this.address=address;
        this.phone=phone;
        this.order_list=order_list;
        this.datetime=datetime;
        this.status=status;
        this.comment=comment;
        this.email=email;
        this.order_type=order_type;
        this.del_msg=del_msg;
        this.del_time=del_time;
        this.del_done=del_done;
        this.cat_type=cat_type;
        this.adtime=adtime;
        this.addate=addate;
        this.admsg=admsg;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobileid() {
        return mobileid;
    }

    public void setMobileid(String mobileid) {
        this.mobileid = mobileid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrder_list() {
        return order_list;
    }

    public void setOrder_list(String order_list) {
        this.order_list = order_list;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public String getDel_msg() {
        return del_msg;
    }

    public void setDel_msg(String del_msg) {
        this.del_msg = del_msg;
    }

    public String getDel_time() {
        return del_time;
    }

    public void setDel_time(String del_time) {
        this.del_time = del_time;
    }

    public String getDel_done() {
        return del_done;
    }

    public void setDel_done(String del_done) {
        this.del_done = del_done;
    }

    public String getCat_type() {
        return cat_type;
    }

    public void setCat_type(String cat_type) {
        this.cat_type = cat_type;
    }

    public String getAdtime() {
        return adtime;
    }

    public void setAdtime(String adtime) {
        this.adtime = adtime;
    }

    public String getAddate() {
        return addate;
    }

    public void setAddate(String addate) {
        this.addate = addate;
    }

    public String getAdmsg() {
        return admsg;
    }

    public void setAdmsg(String admsg) {
        this.admsg = admsg;
    }
}
