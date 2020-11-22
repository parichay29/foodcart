package com.rndtechnosoft.foodcart.Util;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.rndtechnosoft.foodcart.R;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by needbuzz on 12-08-2017.
 */

public class App extends Application {

    private String last24Call,businessTitle,businessTag,businessPhone,locality,username,businessDesc,address,price,isServiceVerified,odCatId,odSubCatId,odCatName,odSubCatName,fullname, accessToken, gcmToken = "", fb_id = "", photoUrl = "", coverUrl = "", area = "", country = "", city = "", phone = "";
    private Double lat = 0.000000, lng = 0.000000;
    private RequestQueue mRequestQueue;
    private static App mInstance;
    private CookieManager cookieManager;
    public static App getmInstance() {
        return mInstance;
    }
    /*** VOLLEY REQUEST MANAGER ***/

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            //set the server cookies, essential if your server require authentication
            cookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
            CookieHandler.setDefault(cookieManager);
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
    public static void setmInstance(App mInstance) {
        App.mInstance = mInstance;
    }

    public static synchronized App getInstance() {
        return mInstance;
    }

    public CookieManager getCookieManager() {
        return cookieManager;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        /*CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/futura_medium_bt.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );*/
    }

}