package com.rndtechnosoft.foodcart.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.rndtechnosoft.foodcart.Util.Constant_Api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    String DB_PATH;
    private final static String DB_NAME = "dbcart.sqli";
    public final static int DB_VERSION = 1;
    public static SQLiteDatabase db;
    private final Context context;
    private final String TABLE_NAME = "tbl_order";
    private final String ID = "id";
    private final String MENU_NAME = "Menu_name";
    private final String QUANTITY = "Quantity";
    private final String TOTAL_PRICE = "Total_price";
    private final String CAT = "cat";
    private final String KG = "kg";
    private final String MIN = "minkg";
    private final String MAX = "maxkg";
    private final String FOOD_CAT = "food_cat";
    private final String F_ID = "f_id";
    private final String F_NAME = "f_name";
    private final String SUB_PRICE = "sub_price";


    public DBHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;

        DB_PATH = Constant_Api.DB_PATH;
    }

    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();
        SQLiteDatabase db_Read = null;

        if (dbExist) {

        } else {
            db_Read = this.getReadableDatabase();
            db_Read.close();

            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }

    }

    private boolean checkDataBase() {

        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();

    }

    private void copyDataBase() throws IOException {

        InputStream myInput = context.getAssets().open(DB_NAME);

        String outFileName = DB_PATH + DB_NAME;

        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public void close() {
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<ArrayList<Object>> getAllData() {
        ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();

        Cursor cursor = null;

        try {
            cursor = db.query(
                    TABLE_NAME,
                    new String[]{ID, MENU_NAME, QUANTITY, TOTAL_PRICE,CAT,KG,MIN,MAX,FOOD_CAT,F_ID,F_NAME,SUB_PRICE},
                    null, null, null, null, null);
            cursor.moveToFirst();

            if (!cursor.isAfterLast()) {
                do {
                    ArrayList<Object> dataList = new ArrayList<Object>();

                    dataList.add(cursor.getLong(0));
                    dataList.add(cursor.getString(1));
                    dataList.add(cursor.getString(2));
                    dataList.add(cursor.getString(3));
                    dataList.add(cursor.getString(4));
                    dataList.add(cursor.getString(5));
                    dataList.add(cursor.getInt(6));
                    dataList.add(cursor.getInt(7));
                    dataList.add(cursor.getString(8));
                    dataList.add(cursor.getString(9));
                    dataList.add(cursor.getString(10));
                    dataList.add(cursor.getString(11));

                    dataArrays.add(dataList);
                }

                while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLException e) {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }

        return dataArrays;
    }

    public boolean isDataExist(long id) {
        boolean exist = false;

        Cursor cursor = null;

        try {
            cursor = db.query(
                    TABLE_NAME,
                    new String[]{ID},
                    ID + "=" + id,
                    null, null, null, null);
            if (cursor.getCount() > 0) {
                exist = true;
            }

            cursor.close();
        } catch (SQLException e) {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }

        return exist;
    }

    public boolean isPreviousDataExist() {
        boolean exist = false;

        Cursor cursor = null;

        try {
            cursor = db.query(
                    TABLE_NAME,
                    new String[]{ID},
                    null, null, null, null, null);
            if (cursor.getCount() > 0) {
                exist = true;
            }

            cursor.close();
        } catch (SQLException e) {
            Log.e("DB Error", e.toString());
            e.printStackTrace();
        }

        return exist;
    }

    public void addData(long id, String menu_name, int quantity, double total_price,String cay_type,String food_cat,int sub_price) {

        ContentValues values = new ContentValues();
        values.put(ID, id);
        values.put(MENU_NAME, menu_name);
        values.put(QUANTITY, quantity);
        values.put(TOTAL_PRICE, total_price);
        values.put(CAT, cay_type);
        values.put(FOOD_CAT,food_cat);
        values.put(SUB_PRICE,sub_price);

        try {
            db.insert(TABLE_NAME, null, values);
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

    public void addDataAdv(long id, String menu_name, int quantity, double total_price,String cay_type, String kg, int min, int max, String f_id, String f_name,int sub_price) {

        ContentValues values = new ContentValues();
        values.put(ID, id);
        values.put(MENU_NAME, menu_name);
        values.put(QUANTITY, quantity);
        values.put(TOTAL_PRICE, total_price);
        values.put(CAT, cay_type);
        values.put(KG, kg);
        values.put(MIN,min);
        values.put(MAX,max);
        values.put(F_ID,f_id);
        values.put(F_NAME,f_name);
        values.put(SUB_PRICE,sub_price);

        try {
            db.insert(TABLE_NAME, null, values);
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

    public void deleteData(long id) {

        try {
            db.delete(TABLE_NAME, ID + "=" + id, null);
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

    public void deleteAllData() {

        try {
            db.delete(TABLE_NAME, null, null);
        } catch (Exception e) {
            Log.e("DB ERROR", e.toString());
            e.printStackTrace();
        }
    }

    public void updateDataAdv(long id, int quantity, double total_price,String kg) {


        if(quantity==0){
            deleteData(id);
        }else {
            ContentValues values = new ContentValues();
            values.put(QUANTITY, quantity);
            values.put(KG, kg);
            values.put(TOTAL_PRICE, total_price);

            try {
                db.update(TABLE_NAME, values, ID + "=" + id, null);
            } catch (Exception e) {
                Log.e("DB Error", e.toString());
                e.printStackTrace();
            }
        }
    }

    public void updateData(long id, int quantity, double total_price) {


        if(quantity==0){
            deleteData(id);
        }else {
            ContentValues values = new ContentValues();
            values.put(QUANTITY, quantity);
            values.put(TOTAL_PRICE, total_price);

            try {
                db.update(TABLE_NAME, values, ID + "=" + id, null);
            } catch (Exception e) {
                Log.e("DB Error", e.toString());
                e.printStackTrace();
            }
        }
    }



    public Integer getQuantity(long id) {

        Integer quantity=0;
        SQLiteDatabase db = this.getReadableDatabase();
        //specify the columns to be fetched
        String[] columns = {QUANTITY};
        //Select condition
        String selection = ID + " = ?";
        //Arguments for selection
        String[] selectionArgs = {String.valueOf(id)};


        try {
            Cursor cursor = db.query(TABLE_NAME, columns, selection,
                    selectionArgs, null, null, null);
            if (null != cursor &&  cursor.moveToFirst()) {
                quantity=cursor.getInt(0);
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return quantity;
        }
        db.close();
        return quantity;

    }


    public String getCat(long id) {

        String cat_type = null;
        SQLiteDatabase db = this.getReadableDatabase();
        //specify the columns to be fetched
        String[] columns = {CAT};
        //Select condition
        String selection = ID + " = ?";
        //Arguments for selection
        String[] selectionArgs = {String.valueOf(id)};


        try {
            Cursor cursor = db.query(TABLE_NAME, columns, null,
                    null, null, null, null, "1");
            if (null != cursor &&  cursor.moveToFirst()) {
                cat_type=cursor.getString(0);
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return cat_type;
        }
        db.close();
        return cat_type;

    }


}