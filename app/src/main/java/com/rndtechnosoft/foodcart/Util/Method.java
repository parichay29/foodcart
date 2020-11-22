package com.rndtechnosoft.foodcart.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.rndtechnosoft.foodcart.R;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;

import static com.rndtechnosoft.foodcart.Activity.ActivityReservation.request;

public class Method {

    private Activity activity;

    public static Typeface futura_medium_bt;
    public static Typeface OpenSans_Regular;
    public static Typeface OpenSans_Semibold;

    public static boolean onBackPress = false;

    public Method(Activity activity) {
        this.activity = activity;
        /*try {
            futura_medium_bt = Typeface.createFromAsset(activity.getAssets(), "fonts/futura_medium_bt.ttf");
            OpenSans_Regular = Typeface.createFromAsset(activity.getAssets(), "fonts/OpenSans_Regular.ttf");
            OpenSans_Semibold = Typeface.createFromAsset(activity.getAssets(), "fonts/OpenSans_Semibold.ttf");
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public static void forceRTLIfSupported(Window window, Activity activity) {
        if (activity.getResources().getString(R.string.isRTL).equals("true")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                window.getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }
    }

    public static String getAndroidID(Context context){
         String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
         return android_id;
    }

    public static class sendData extends AsyncTask<Void, Void, String> {
        Context context;
        String token,aid;
        public sendData(Context context, String token, String aid) {
            this.context=context;
            this.token=token;
            this.aid=aid;
        }

        //ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            //dialog = ProgressDialog.show(activity, "", getString(R.string.sending_alert), true);

        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "";
            HttpClient client = new DefaultHttpClient();
            HttpPost request = new HttpPost(Constant_Api.registerFCM);

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(9);
                nameValuePairs.add(new BasicNameValuePair("user_id", SharedPref.getUserId(context)));
                nameValuePairs.add(new BasicNameValuePair("token", token));
                nameValuePairs.add(new BasicNameValuePair("mobileid", aid));
                request.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                HttpResponse response = client.execute(request);
                result = request(response);
                //Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                result = "Unable to connect.";
                //Toast.makeText(context, "Catch", Toast.LENGTH_SHORT).show();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            //dialog.dismiss();
            //resultAlert(Result);
        }
    }


    public static void sendRegistrationToServer(Context context, String token, String aid) {
        /*OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url(Constant_Api.registerFCM+"?user_id="+SharedPref.getUserId(context)+"&token="+token+"&mobileid="+aid)
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            Log.d("RegsiterTAG", String.valueOf(response));
            Log.d("SuccessRegister","Regsiter Success");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        new sendData(context,token,aid).execute();
    }

    public static void setBadgeCount(Context context, LayerDrawable icon, int count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }




    //network check
    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //youtube application installation or not check
    public static boolean isAppInstalled(Activity activity) {
        String packageName = "com.google.android.youtube";
        Intent mIntent = activity.getPackageManager().getLaunchIntentForPackage(packageName);
        if (mIntent != null) {
            return true;
        } else {
            return false;
        }
    }

    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) activity
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();

        point.x = display.getWidth();
        point.y = display.getHeight();

        columnWidth = point.x;
        return columnWidth;
    }

}
