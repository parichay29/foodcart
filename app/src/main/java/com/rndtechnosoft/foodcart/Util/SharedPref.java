package com.rndtechnosoft.foodcart.Util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPref {

    static SharedPreferences shPreferences;
	public static String strPrefName = "FoodCart";
	public static String HOTEL_ID = "hotelid";
	public static String USER_ID = "userid";
	public static String USER_MOBILE = "mobile";
	public static String USER_NAME = "username";
	Context myContext;

	public SharedPref(Context ctx) {
		myContext = ctx;
	}

	public static String getPreference(String key, String Default,
                                       Activity activity) {
		shPreferences = activity.getSharedPreferences(strPrefName,
				Context.MODE_PRIVATE);
		return shPreferences.getString(key, Default);
	}

	public static void clearAllPreferences(Activity activity) {
		shPreferences = activity.getSharedPreferences(strPrefName,
				Context.MODE_PRIVATE);

		Editor editor = shPreferences.edit();
		editor.clear();
		editor.apply(); // important! Don't forget!
	}


	
	public static Integer getPreference(String key,
                                        Context activity) {
		shPreferences = activity.getSharedPreferences(key,
				Context.MODE_PRIVATE);
		return shPreferences.getInt(key, 0);
	}
	
	public static boolean setPreference(String key, int value,
                                        Activity activity) {
		if (value != 0) {
			shPreferences = activity.getSharedPreferences(key,
					Context.MODE_PRIVATE);
			Editor editor = shPreferences.edit();
			editor.putInt(key, value);
			editor.apply();
			return true;
		}
		return false;
	}
	
	public static int increment(Activity act, String key){

		int t;
		shPreferences = act.getSharedPreferences(key,
				Context.MODE_PRIVATE);
		t = shPreferences.getInt(key, 0);
		t++;
		Editor editor = shPreferences.edit();
		editor.putInt(key, t);
		editor.apply();
		return t;
	}
	public static boolean setPreference(String key, String value,
                                        Activity activity) {
		if (value != null) {
			shPreferences = activity.getSharedPreferences(strPrefName,
					Context.MODE_PRIVATE);
			Editor editor = shPreferences.edit();
			editor.putString(key, value);
			editor.apply();
			return true;
		}
		return false;
	}
	public static boolean setPreference(String key, boolean value,
                                        Activity activity) {
		shPreferences = activity.getSharedPreferences(strPrefName,
				Context.MODE_PRIVATE);
		Editor editor = shPreferences.edit();
		editor.putBoolean(key, value);
		editor.apply();
		return true;
	}

	public static boolean setPreference(String key, String value,
                                        Context activity) {
		shPreferences = activity.getSharedPreferences(strPrefName,
				Context.MODE_PRIVATE);
		Editor editor = shPreferences.edit();
		editor.putString(key, value);
		editor.apply();
		return true;
	}
	public static String getHotelId(Context activity) {
		try {
			shPreferences = activity.getSharedPreferences(strPrefName,
					Context.MODE_PRIVATE);

		} catch (Exception e) {
			// TODO: handle exception
		}
		return shPreferences.getString(HOTEL_ID, "");
	}

	public static String getUserName(Context activity) {

		shPreferences = activity.getSharedPreferences(strPrefName,
				Context.MODE_PRIVATE);
		return shPreferences.getString(USER_NAME, "");
	}

	public static String getUserId(Context activity) {

		shPreferences = activity.getSharedPreferences(strPrefName,
				Context.MODE_PRIVATE);
		return shPreferences.getString(USER_ID, "");
	}


	public static String getMobileNumber(Context activity) {

		shPreferences = activity.getSharedPreferences(strPrefName,
				Context.MODE_PRIVATE);
		return shPreferences.getString(USER_MOBILE, "");
	}


}
