package com.rndtechnosoft.foodcart.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.SQLException;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.rndtechnosoft.foodcart.Adapter.AdapterCart;
import com.rndtechnosoft.foodcart.Helper.AlertDialogRadio;
import com.rndtechnosoft.foodcart.Helper.DBHelper;
import com.rndtechnosoft.foodcart.Item.ItemCart;
import com.rndtechnosoft.foodcart.R;
import com.rndtechnosoft.foodcart.Util.Constant_Api;
import com.rndtechnosoft.foodcart.Util.Method;
import com.rndtechnosoft.foodcart.Util.SharedPref;
import com.rndtechnosoft.foodcart.interfaceee.CartListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;


public class ActivityCart extends AppCompatActivity implements AlertDialogRadio.AlertPositiveListener, CartListener {

    RecyclerView recyclerView;
    ProgressBar prgLoading;
    ProgressDialog dialog;
    ImageView img_cancel;
    TextView txtSubTotal,txtTotal, txtAlert,txtBalance, etDate, etTime, txtDiscAmt,txtWallet,txtcoupon;
    Button submit;
    EditText etComment;
    RelativeLayout lytOrder;
    DBHelper dbhelper;
    AdapterCart adapterCart;
    public static double Tax;
    public static String Currency;
    ArrayList<ArrayList<Object>> data;
    public static ArrayList<Integer> Menu_ID = new ArrayList<Integer>();
    public static ArrayList<String> Menu_name = new ArrayList<String>();
    public static ArrayList<Integer> Quantity = new ArrayList<Integer>();
    public static ArrayList<Double> Sub_total_price = new ArrayList<Double>();
   // public static ArrayList<String> CAT_TYPE = new ArrayList<String>();
    public static ArrayList<String> kg = new ArrayList<String>();
    public static ArrayList<String> min = new ArrayList<String>();
    public static ArrayList<String> max = new ArrayList<String>();
    public static ArrayList<String> food_cat = new ArrayList<String>();
    public static ArrayList<String> f_id = new ArrayList<String>();
    public static ArrayList<String> f_name = new ArrayList<String>();
    public static ArrayList<String> per_price = new ArrayList<String>();
    private List<ItemCart> arrayItemCart;
    double Total_price;
    final int CLEAR_ALL_ORDER = 0;
    final int CLEAR_ONE_ORDER = 1;
    int FLAG;
    int cwall=0;
    int ID;
    String TaxCurrencyAPI;
    int IOConnect = 0;
    Button btn_reservation;
    private int position=0;
    private Toolbar toolbar;
    CheckBox checkWallet;
    int wallet=0,minamt=0;
    String minmsg="";
    String OrderList = "";
    String Result;
    //double amount=0;
    double totalamount=0;
    public static String cat_type="";
    private int mYear, mMonth, mDay, mHour, mMinute;
    String date, time, comment;
    private String format = "";
    private String ORDER_LIST;
    private double payable=0;
    private LinearLayout linearcoupon,linearcode;
    private String pay_amount;
    private String title;
    private String message;
    private String api_amount;
    private String coupon_code;
    private String discount;
    LinearLayout discountlayout,linearWallet;
    private String error;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_cart);
            Log.d("Log", "Working in Normal Mode, RTL Mode is Disabled");

        btn_reservation = (Button) findViewById(R.id.btn_reservation);

        checkWallet= findViewById(R.id.checkWallet);
        linearcoupon = findViewById(R.id.linearcoupon);
        linearcode = findViewById(R.id.linearcode);
        discountlayout = findViewById(R.id.discount);
        linearWallet = findViewById(R.id.linearWallet);
        prgLoading = (ProgressBar) findViewById(R.id.prgLoading);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //txtTotalLabel = (TextView) findViewById(R.id.txtTotalLabel);
        txtSubTotal = (TextView) findViewById(R.id.txtSubTotal);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtAlert = (TextView) findViewById(R.id.txtAlert);
        txtBalance = (TextView) findViewById(R.id.txtBalance);
        txtDiscAmt = (TextView) findViewById(R.id.txtDiscAmt);
        txtWallet = (TextView) findViewById(R.id.txtWallet);
        txtcoupon = (TextView) findViewById(R.id.txtcoupon);
        img_cancel = (ImageView) findViewById(R.id.img_cancel);
        toolbar = (Toolbar) findViewById(R.id.toolbar_about_us);
        toolbar.setTitleTextColor(getResources().getColor(R.color.finestBlack));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.left_arrow));
        toolbar.setTitle(getResources().getString(R.string.title_cart));
        setSupportActionBar(toolbar);
        linearcode.setVisibility(View.GONE);


        getWallet();



        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        //recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(3), true));
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());

        lytOrder = (RelativeLayout) findViewById(R.id.lytOrder);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            clearCouponData();
        } else {
            title= extras.getString("title");
            message = extras.getString("message");
            api_amount = extras.getString("amount");
            pay_amount = extras.getString("pay_amount");
            coupon_code = extras.getString("coupon_code");
            discount = extras.getString("discount");
            Constant_Api.WALLET_FLAG = extras.getBoolean("ischecked");
            cwall = extras.getInt("wamt");
            showCustomDialog(title,message);
        }



        if (discount!=null && !discount.equals("")) {
            txtDiscAmt.setText("- " + getResources().getString(R.string.rupee) + " " + discount);
            txtDiscAmt.setTextColor(getResources().getColor(R.color.new_green));
            discountlayout.setVisibility(View.VISIBLE);
        }else {
            discountlayout.setVisibility(View.GONE);
        }

        if (coupon_code!=null && !coupon_code.equals("") && discount!=null && !discount.equals("")){
            linearcoupon.setVisibility(View.GONE);
            linearcode.setVisibility(View.VISIBLE);
            txtcoupon.setText(coupon_code);
        }

        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearcoupon.setVisibility(View.VISIBLE);
                linearcode.setVisibility(View.GONE);
                discountlayout.setVisibility(View.GONE);
                clearCouponData();
//                String amt = String.valueOf(Float.parseFloat((pay_amount))+Float.parseFloat((discount)));
                if (checkWallet.isChecked())
                    txtTotal.setText(getApplicationContext().getResources().getString(R.string.rupee) + (Total_price-cwall));
                else
                    txtTotal.setText(getApplicationContext().getResources().getString(R.string.rupee) + Total_price);
            }
        });

        linearcoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ActivityCart.this,CouponActivity.class)
                        .putExtra("amount",txtSubTotal.getText().toString())
                        .putExtra("ischecked",Constant_Api.WALLET_FLAG)
                        .putExtra("wamt",cwall));
            }
        });

        checkWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkWallet.isChecked()) {
                   checkwallet();
                }else if (!checkWallet.isChecked()){
                    uncheckwallet();
                }
            }
        });


        adapterCart = new AdapterCart(this, arrayItemCart);
        dbhelper = new DBHelper(this);

        try {
            dbhelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }


        refreshdata();
       // new getTaxCurrency().execute();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("catdb"," : "+cat_type);
                if (Total_price < minamt){
                    showAlert();
                }else {
                    if (cat_type.equals("advance")) {
                        dateTime();
                    } else {
                        typealert();
                    }
                }
                new getJson().execute();
            }
        };
        btn_reservation.setOnClickListener(listener);

    }

    private void clearCouponData() {
        title= null;
        message = null;
        api_amount = null;
        pay_amount = null;
        discount=null;
        coupon_code=null;
    }

    private void uncheckwallet() {
        Constant_Api.WALLET_FLAG=false;
        cwall=0;
        if (pay_amount!=null)
            txtTotal.setText(getApplicationContext().getResources().getString(R.string.rupee)+pay_amount);
        else
            txtTotal.setText(getApplicationContext().getResources().getString(R.string.rupee)+Total_price);
        linearWallet.setVisibility(View.GONE);
        txtWallet.setText("-"+getResources().getString(R.string.rupee)+String.valueOf(wallet));
        txtWallet.setTextColor(getResources().getColor(R.color.new_green));
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityCart.this);
        builder.setTitle(R.string.app_name);
        builder.setMessage(minmsg);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                checkWallet.setChecked(false);
                cwall=0;
            }
        });
        builder.show();
    }

    private void dateTime() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityCart.this);
        builder.setTitle("Select Date & Time : ");
        LayoutInflater inflater = getLayoutInflater();
        final View dialogLayout = inflater.inflate(R.layout.alert_layout, null);
        etDate = dialogLayout.findViewById(R.id.txtDate);
        etTime = dialogLayout.findViewById(R.id.txtTime);
        etComment = dialogLayout.findViewById(R.id.txtComment);
        submit = dialogLayout.findViewById(R.id.btnSubmit);
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });
        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etDate.getText().toString().equalsIgnoreCase(""))
                    return;
                else if(etTime.getText().toString().equalsIgnoreCase(""))
                    return;
                else if (!etDate.getText().toString().equalsIgnoreCase("") && !etTime.getText().toString().equalsIgnoreCase("")) {
                    date = etDate.getText().toString();
                    time = etTime.getText().toString();
                }
                comment = etComment.getText().toString();
                typealert();
            }
        });
        builder.setView(dialogLayout);
        builder.show();
    }

    private void timePicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        showTime(hourOfDay,minute);
                        //etTime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();

    }

    public void showTime(int hour, int min) {
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }

       if (hour<10 && min<10)
            etTime.setText(0+""+hour+":"+0+""+min+" "+format);
       else if (hour<10)
           etTime.setText(0+""+hour+":"+min+" "+format);
       else if (min<10)
           etTime.setText(hour+":"+0+""+min+" "+format);
       else
           etTime.setText(hour+":"+min+" "+format);
    }

    private void datePicker() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        etDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    private void typealert() {
        /** Getting the fragment manager */
        FragmentManager manager = getFragmentManager();

        /** Instantiating the DialogFragment class */
        AlertDialogRadio alert = new AlertDialogRadio();

        /** Creating a bundle object to store the selected item's index */
        Bundle b = new Bundle();

        /** Storing the selected item's index in the bundle object */
        b.putInt("position", position);

        /** Setting the bundle object to the dialog fragment object */
        alert.setArguments(b);

        /** Creating the dialog fragment object, which will in turn open the alert dialog window */
        alert.show(manager, "alert_dialog_radio");
    }

    private void getWallet() {
        AsyncHttpClient client = new AsyncHttpClient();
        /*client.addHeader("App-Id",Constant_Api.HOTEL_ID);*/
        String userid= SharedPref.getUserId(this);
        Log.d("userid",userid);
        client.get(Constant_Api.profile_view+userid, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray("USER_PROFILE");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        wallet = object.getInt("wallet");
                        minamt = object.getInt("min_amount_rs");
                        minmsg = object.getString("min_amount_msg");

                    }

                    txtBalance.setText(getApplicationContext().getResources().getString(R.string.rupee)+wallet);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //progressBar.hide();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                this.finish();
                return true;

            case R.id.clear:
                showClearDialog(CLEAR_ALL_ORDER, 1111);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showClearDialog(int flag, int id) {
        FLAG = flag;
        ID = id;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm);
        switch (FLAG) {
            case 0:
                builder.setMessage(getString(R.string.clear_all_order));
                break;
            case 1:
                builder.setMessage(getString(R.string.clear_one_order));
                break;
        }
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (FLAG) {
                    case 0:
                        dbhelper.deleteAllData();
                        clearData();
                        new getDataTask().execute();
                        break;
                    case 1:
                        dbhelper.deleteData(ID);
                        clearData();
                        new getDataTask().execute();
                        break;
                }
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
 TextView textView=alert.findViewById(android.R.id.message);
        Typeface face= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            face = getResources()
                    .getFont(R.font.roboto_medium);
        }
        textView.setTypeface(face);
    }

    public void refreshdata(){
        new getDataTask().execute();
    }

    @Override
    public void onPositiveClick(int position) {
        String wallamt= String.valueOf(cwall);
        if (AlertDialogRadio.Ordertype.code[position].equals(Constant_Api.TYPE_HOME)) {
            if (cat_type.equals("advance")) {
                Intent i = new Intent(ActivityCart.this, ActivityReservation.class).putExtra("type", Constant_Api.TYPE_HOME).putExtra("ORDER_LIST",ORDER_LIST)
                        .putExtra("cwall",wallamt).putExtra("order", OrderList).putExtra("coupon_code",coupon_code).putExtra("payable",payable).putExtra("date",date).putExtra("time",time).putExtra("comment",comment).putExtra("cat_type",cat_type);
                startActivity(i);
            }else{int max=0;
                Intent i = new Intent(ActivityCart.this, ActivityReservation.class).putExtra("type", Constant_Api.TYPE_HOME)
                        .putExtra("cwall",wallamt).putExtra("order", OrderList).putExtra("coupon_code",coupon_code).putExtra("payable",payable).putExtra("cat_type",cat_type).putExtra("ORDER_LIST",ORDER_LIST);
                startActivity(i);
            }
        }else {
            if (AlertDialogRadio.Ordertype.code[position].equals(Constant_Api.TAKE_AWAY))
                confirmTakeAway();
        }
    }

    private void confirmTakeAway() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm);
        builder.setMessage("Are you sure you want to place order?");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                new sendData().execute();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void updateOrderJson() {
        String response= null;

        String[] separated = txtTotal.getText().toString().split(getResources().getString(R.string.rupee));

        response = separated[1];
        OrderList="";
        for (int i=0; i<Quantity.size();i++)
            OrderList += (Quantity.get(i) + " - " + Menu_name.get(i) + " - " + Sub_total_price.get(i) + " INR <br>");
        OrderList += "Total Order : " + Total_price + " INR<br>" ;
        if (discount!=null && !discount.equals("")) {
            OrderList += "Discount : " + discount + " INR<br>";
        }else {
            OrderList += "Discount : " + 0 + " INR<br>";
        }
        OrderList += "Wallet Amount Used : " + cwall + " INR<br>" ;
        //OrderList += "Total Payable : " ;// + (Total_price-cwall) + " INR<br>"
        payable= Double.parseDouble(response);

        if (cat_type.equals("advance")) {
            ORDER_LIST = "{ \"order_array\" : [";
            for (int i = 0; i < Quantity.size(); i++) {
                ORDER_LIST += "{\"qty\" : " + "\"" + Quantity.get(i) + "\"";
                if (!kg.get(i).equals(""))
                    ORDER_LIST += ",\"menu_name\" : " + "\"" + Menu_name.get(i)  + "\"";
                else
                    ORDER_LIST += ",\"menu_name\" : " + "\"" + Menu_name.get(i)  + "\"";
                ORDER_LIST += ",\"sub_total\" : " + "\"" + Sub_total_price.get(i) + "\"";
                if(!f_id.get(i).equals("") && !f_name.get(i).equals(""))
                    ORDER_LIST+= ",\"flavour\":{\"flavour_id\":\""+f_id.get(i)+"\",\"flavour_name\":\""+f_name.get(i)+"\"}";
                if (i == Quantity.size() - 1)
                    ORDER_LIST += "}";
                else
                    ORDER_LIST += "},";

            }
        }else{
            ORDER_LIST = "{ \"order_array\" : [";
            for (int i = 0; i < Quantity.size(); i++) {
                ORDER_LIST += "{\"qty\" : " + "\"" + Quantity.get(i) + "\"";
                if (!food_cat.get(i).equals(""))
                    ORDER_LIST += ",\"menu_name\" : " + "\"" + Menu_name.get(i)  + "\"";//+ " (" + food_cat.get(i) + ")"
                else
                    ORDER_LIST += ",\"menu_name\" : " + "\"" + Menu_name.get(i)  + "\"";
                ORDER_LIST += ",\"sub_total\" : " + "\"" + Sub_total_price.get(i) + "\"";
                if (i == Quantity.size() - 1)
                    ORDER_LIST += "}";
                else
                    ORDER_LIST += "},";
            }
        }


        if (discount!=null && !discount.equals("")) {
            ORDER_LIST += "],\"amount\" : {\"sub_total\" : \"" + Total_price + "\",\"discount\" : \"" + discount + "\",\"wallet\" : \"" + cwall + "\",\"total_price\" : \"" + response + "\"}}";
        }else {
            ORDER_LIST += "],\"amount\" : {\"sub_total\" : \"" + Total_price + "\",\"discount\" : \"" + 0 + "\",\"wallet\" : \"" + cwall + "\",\"total_price\" : \"" + response + "\"}}";
        }
        Log.e("orderlistjson",ORDER_LIST);
    }

    @Override
    public void onRefreshCallback() {
        adapterCart.notifyDataSetChanged();
    }

    public class sendData extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(ActivityCart.this, "", getString(R.string.sending_alert), true);

        }

        @Override
        protected Void doInBackground(Void... params) {
            updateOrderJson();
            Result= placeOrder();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            resultAlert(Result);
        }
    }

    public void resultAlert(String HasilProses) {
        Log.e("HasilProses", HasilProses);
        String[] separated = HasilProses.split("end");

        String response= separated[1];
        try {
            JSONObject jsonObject = new JSONObject(response);

            JSONArray jsonArray = jsonObject.getJSONArray("ORDER_PLACED");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object = jsonArray.getJSONObject(i);
                String error = object.getString("error");
                int order_id = Integer.parseInt(object.getString("order_id"));
                String message = object.getString("message");
                Intent intent = new Intent(ActivityCart.this, ActivityConfirmMessage.class).putExtra("order_id",order_id);
                dbhelper.deleteAllData();
                startActivity(intent);
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String placeOrder() {
        //dialog = ProgressDialog.show(ActivityCart.this, "", getString(R.string.placing_order), true);

        OrderList +="Total Payable : "+payable+" INR";

        String result = "";
        HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(Constant_Api.add_order);
        String date_time= java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());;
        String amount= String.valueOf(cwall);
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(15);
            nameValuePairs.add(new BasicNameValuePair("user_id", SharedPref.getUserId(this)));
            nameValuePairs.add(new BasicNameValuePair("name", SharedPref.getUserName(this)));
            nameValuePairs.add(new BasicNameValuePair("number_of_people", ""));
            nameValuePairs.add(new BasicNameValuePair("date_time", ""));
            nameValuePairs.add(new BasicNameValuePair("phone", SharedPref.getMobileNumber(this)));
            nameValuePairs.add(new BasicNameValuePair("amount", amount));
            nameValuePairs.add(new BasicNameValuePair("order_list", OrderList));
            nameValuePairs.add(new BasicNameValuePair("json_order_list", ORDER_LIST));
            nameValuePairs.add(new BasicNameValuePair("comment", comment));
            nameValuePairs.add(new BasicNameValuePair("city", "Bhilad"));
            nameValuePairs.add(new BasicNameValuePair("email", ""));
            nameValuePairs.add(new BasicNameValuePair("app_id", Constant_Api.HOTEL_ID));
            nameValuePairs.add(new BasicNameValuePair("mobileid", Method.getAndroidID(ActivityCart.this)));
            nameValuePairs.add(new BasicNameValuePair("address", ""));
            nameValuePairs.add(new BasicNameValuePair("cat_type", cat_type));
            nameValuePairs.add(new BasicNameValuePair("coupon_code", coupon_code));

            if (cat_type.equals("advance")) {
                nameValuePairs.add(new BasicNameValuePair("date", date));
                nameValuePairs.add(new BasicNameValuePair("time", time));
            }

            nameValuePairs.add(new BasicNameValuePair("order_type", Constant_Api.TAKE_AWAY));
            request.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            HttpResponse response = client.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            Log.e("placeorder",responseBody);
            result = responseBody;
            //dialog.dismiss();
        } catch (Exception ex) {
            result = "Unable to connect.";
            //dialog.dismiss();
        }
        return result;
    }

    public static String request(HttpResponse response) {
        String result = "";
        try {
            InputStream in = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder str = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                str.append(line + "\n");
            }
            in.close();
            result = str.toString();
        } catch (Exception ex) {
            result = "Error";
        }
        return result;
    }

    public class getTaxCurrency extends AsyncTask<Void, Void, Void> {

        getTaxCurrency() {
            if (!prgLoading.isShown()) {
                prgLoading.setVisibility(View.VISIBLE);
                txtAlert.setVisibility(View.GONE);
            }
        }

        @Override
        protected Void doInBackground(Void... arg0) {
//            parseJSONDataTax();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            prgLoading.setVisibility(View.GONE);
            if (IOConnect == 0) {
                new getDataTask().execute();

            } else {
                txtAlert.setVisibility(View.VISIBLE);
                txtAlert.setText(R.string.failed_connect_network);
            }

        }
    }

    public void clearData() {
        Menu_ID.clear();
        Menu_name.clear();
        Quantity.clear();
        Sub_total_price.clear();
        //CAT_TYPE.clear();
        kg.clear();
        min.clear();
        max.clear();
        food_cat.clear();
        f_id.clear();
        f_name.clear();
        per_price.clear();
    }

    public class getDataTask extends AsyncTask<Void, Void, Void> {

        getDataTask() {
            if (!prgLoading.isShown()) {
                prgLoading.setVisibility(View.VISIBLE);
                lytOrder.setVisibility(View.GONE);
                txtAlert.setVisibility(View.GONE);
            }
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            getDataFromDatabase();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            txtSubTotal.setText(getApplicationContext().getResources().getString(R.string.rupee)+Total_price);
            txtTotal.setText(getApplicationContext().getResources().getString(R.string.rupee)+Total_price);
            //txtTotalLabel.setText(getString(R.string.total_order) + " Tax included");
            prgLoading.setVisibility(View.GONE);
            if (Menu_ID.size() > 0) {
                lytOrder.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(adapterCart);
            } else {
                txtAlert.setVisibility(View.VISIBLE);
            }

            if (coupon_code!=null && !coupon_code.equals("")){
                getDiscount(coupon_code, String.valueOf(Total_price));
            }
        }
    }

    private void getDiscount(final String coupon_code, final String amount) {
        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.addHeader("userid", SharedPref.getUserId(ActivityCart.this));
        client.addHeader("coupon_code", coupon_code);
        client.addHeader("min_value", amount);
        client.get(Constant_Api.get_discount, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                //couponLists = new ArrayList<>();
                try {
                    //couponLists.clear();
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonArray = jsonObject.getJSONArray("GET_DISCOUNT");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        error = object.getString("error");
                        title = object.getString("title");
                        message = object.getString("message");
                        api_amount = object.getString("amount");
                        pay_amount = object.getString("pay_amount");
                        discount = object.getString("discount");
                        Log.d("discountamount",discount+"->>>"+pay_amount+"->>>"+api_amount);
                        //showCustomDialog(title,message);
                    }

                    txtDiscAmt.setText("- " + getResources().getString(R.string.rupee) + " " + discount);
                    txtTotal.setText(getResources().getString(R.string.rupee) + pay_amount);


//
//                    startActivity(new Intent(ActivityCart.this,ActivityCart.class)
//                            .putExtra("title",title).putExtra("message",message)
//                            .putExtra("amount",amount).putExtra("pay_amount",pay_amount)
//                            .putExtra("discount",discount).putExtra("coupon_code",coupon_code));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(Constant_Api.WALLET_FLAG){
                    checkWallet.setChecked(true);
                    checkwallet();
                }else {
                    uncheckwallet();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void checkwallet() {
        Constant_Api.WALLET_FLAG = true;
        cwall = wallet;
        String[] separated = txtTotal.getText().toString().split(getResources().getString(R.string.rupee));
        String payamt= separated[1];//.split("\\.")[0];
        if (Double.valueOf(payamt) >= cwall && cwall!=0){
            totalamount = Double.valueOf(payamt) - cwall;
            txtTotal.setText(getApplicationContext().getResources().getString(R.string.rupee) + totalamount);
            linearWallet.setVisibility(View.VISIBLE);
            txtWallet.setText("-"+getResources().getString(R.string.rupee)+String.valueOf(cwall));
            txtWallet.setTextColor(getResources().getColor(R.color.new_green));
        }else if (Double.valueOf(payamt)<=cwall && cwall!=0) {
            txtTotal.setText(getApplicationContext().getResources().getString(R.string.rupee) + "0");
            linearWallet.setVisibility(View.VISIBLE);
            txtWallet.setText("-"+getResources().getString(R.string.rupee)+String.valueOf(cwall));
            txtWallet.setTextColor(getResources().getColor(R.color.new_green));
        }
    }

    public void getDataFromDatabase() {

        Total_price = 0;
        clearData();
        data = dbhelper.getAllData();

        for (int i = 0; i < data.size(); i++) {
            ArrayList<Object> row = data.get(i);

            Menu_ID.add(Integer.parseInt(row.get(0).toString()));
            Menu_name.add(row.get(1).toString());
            Quantity.add(Integer.parseInt(row.get(2).toString()));
            cat_type = dbhelper.getCat(0);

            if (cat_type.equals("advance")) {
                if (row.get(5)!=null && !row.get(5).equals(""))
                    kg.add(row.get(5).toString());
                else
                    kg.add("");
                if (row.get(6)!=null && !row.get(6).equals(""))
                    min.add(row.get(6).toString());
                else
                    min.add("");
                if (row.get(7)!=null && !row.get(7).equals(""))
                    max.add(row.get(7).toString());
                else
                    max.add("");
                if (row.get(9)!=null && !row.get(9).equals(""))
                    f_id.add(row.get(9).toString());
                else
                    f_id.add("");
                if (row.get(10)!=null && !row.get(10).equals(""))
                    f_name.add(row.get(10).toString());
                else
                    f_name.add("");
            }
            if (row.get(8)!=null && !row.get(8).equals(""))
                food_cat.add(row.get(8).toString());
            else
                food_cat.add("");

            per_price.add(row.get(11).toString());

            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
            DecimalFormat formatData = (DecimalFormat)numberFormat;
            formatData.applyPattern("#.##");
            Sub_total_price.add(Double.parseDouble(formatData.format(Double.parseDouble(row.get(3).toString()))));

            Total_price += Sub_total_price.get(i);
            OrderList = (Quantity + " " + Menu_name + " " + Sub_total_price + " INR,\n");
        }
        Log.e("catdb"," : "+dbhelper.getCat(0));

        Total_price += (Total_price * (Tax / 100));

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat formatData = (DecimalFormat)numberFormat;
        formatData.applyPattern("#.##");
        Total_price = Double.parseDouble(formatData.format(Total_price));

        if (OrderList.equalsIgnoreCase("")) {
            OrderList += getString(R.string.no_order_menu);
        }

        OrderList += "\nTotal Order : " + Total_price + " INR" ;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dbhelper.close();
        finish();
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {

            this.clickListener = clickListener;

            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    public void showCustomDialog(String title,String message) {
        hideKeyboard(ActivityCart.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.my_dialog, viewGroup, false);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setView(dialogView);

        final android.support.v7.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView title_dialog = dialogView.findViewById(R.id.title_dialog);
        TextView msg_dialog = dialogView.findViewById(R.id.message_dialog);
        Button buttonOk = dialogView.findViewById(R.id.buttonOk);
        title_dialog.setText(title);
        msg_dialog.setText(message);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public class getJson extends AsyncTask<Void, Void, Void> {

        getJson() {

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            updateOrderJson();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }
}