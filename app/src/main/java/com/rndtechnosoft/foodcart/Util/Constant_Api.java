package com.rndtechnosoft.foodcart.Util;

import com.rndtechnosoft.foodcart.BuildConfig;
import com.rndtechnosoft.foodcart.Item.AboutUsList;

public class Constant_Api {

    public static final String TAKE_AWAY = "Take Away";
    public static final String TYPE_HOME = "Home Delivery";
    public static String HOTEL_ID = "1";
    public static boolean WALLET_FLAG = false;

    //database path configuration
    public static String DB_PATH_START = "/data/data/";
    public static String DB_PATH_END = "/databases/";
    public static String PACKAGE_NAME = BuildConfig.APPLICATION_ID;
    public static String DB_PATH = DB_PATH_START + PACKAGE_NAME + DB_PATH_END;

    //main server api url
    public static String url = "http://ravishah.xyz/foodcart/";

    public static String MY_PREFS_NAME="FoodCart";
    public static final String APP_TEMP_FOLDER = "FoodCart";

    //app image folder
    public static String image = url + "images/";

    public static String add_order = url + "place_order.php?";

    public static String profile_view=url + "view_profile.php?user_id=";

    public static String profile_edit=url + "update_profile.php";
    public static String update_version=url + "updateversion.php";

    //app detail
    public static String app_detail = url + "api.php";

    //Home url
    public static String home = url + "api.php?home";

    //menu url
    public static String menu = url + "menu_cat_list.php";

    public static String citylist = url + "api.php?city_list";

    public static String subcitylist = url + "api.php?sub_city_list_by_city_id=";

    public static String fast_food = url + "api.php?fast_food_list";

    //Menu Detail url
    public static String menu_detail = url + "api.php?menusubcat_id=";

    public static String bill = url +"order-receipt.php?order_id=";

    //My Orders url
    public static String my_orders = url + "api.php?mobileid=";

    //contactUS url
    public static String contact_us = url + "api_contact.php?";

    //AboutUs data
    public static AboutUsList aboutUs;

    //login
    public static final String login = url + "login.php";

    public static final String registerFCM = url +"registerFCM.php";

    public static final String scratchcoupon = url + "get_sc_coupon.php";

    public static final String addtowallet = url + "api.php?wallet_pay";

    public static final String coupon_list = url + "get_coupon.php?coupon_list";

    public static final String get_discount = url + "get_coupon.php?get_discount";

}
