package com.rndtechnosoft.foodcart.Util;

import android.app.Application;

import com.rndtechnosoft.foodcart.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class CalligraphyApplication extends Application {



    private static CalligraphyApplication mInstance;

    public static synchronized CalligraphyApplication getInstance() {
        return mInstance;
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
