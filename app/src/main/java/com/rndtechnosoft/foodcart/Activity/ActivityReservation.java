package com.rndtechnosoft.foodcart.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.rndtechnosoft.foodcart.Helper.DBHelper;
import com.rndtechnosoft.foodcart.Item.CityList;
import com.rndtechnosoft.foodcart.Item.SubCityList;
import com.rndtechnosoft.foodcart.R;
import com.rndtechnosoft.foodcart.Util.Constant_Api;
import com.rndtechnosoft.foodcart.Util.Method;
import com.rndtechnosoft.foodcart.Util.SharedPref;

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

public class ActivityReservation extends AppCompatActivity {

    Button btnSend;

    EditText edtName;
    EditText edtNumberOfPeople;
    EditText edtPhone;
    EditText edtEmail;
    EditText edtOrderList;
    EditText edtComment;
    EditText edtAddress;
    EditText edtRoomNo;
    static EditText dateText, timeText;

    static Button btnDate;
    static Button btnTime;

    ScrollView sclDetail;
    ProgressBar progressBar;
    TextView txtAlert;

    public static DBHelper dbhelper;
    ArrayList<ArrayList<Object>> data;

    String Name, NumberOfPeople, Date, Time, Phone, Date_Time, Address,Email,roomNo;
    String OrderList = "";
    String Comment = "";

    private static int mYear;
    private static int mMonth;
    private static int mDay;
    private static int mHour;
    private static int mMinute;
    Spinner spinCity, spinSubCity;

    // declare static variables to store tax and currency data
    public static double Tax;
    public static String Currency="  INR";

    public static final String TIME_DIALOG_ID = "timePicker";
    public static final String DATE_DIALOG_ID = "datePicker";

    String Result;
    String TaxCurrencyAPI;
    int IOConnect = 0;
    private String type;
        double amount,total;
    String cwall;
    private String order, cat_type, date, time,ORDER_LIST=null;
    double payable;

    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter adapter;
    private String selectedRoomNo="Please select Room No";
    private RelativeLayout rel_room;
    List<CityList> cities=new ArrayList<>();
    List<String> cityList=new ArrayList<>();
    List<SubCityList> subcities=new ArrayList<>();
    List<String> subcityList=new ArrayList<>();
    private String city,subcity;
    Double delivery;
    private ArrayAdapter<String> cityAdapter;
    private ProgressDialog dialog;

    JSONObject jsonObject;
    private String appendlist="";
    private ArrayAdapter<String> subcityAdapter;
    private int spinpos=0;
    private String coupon_code;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        Log.d("Log", "Working in Normal Mode, RTL Mode is Disabled");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_about_us);
        toolbar.setTitleTextColor(getResources().getColor(R.color.finestBlack));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.left_arrow));
        toolbar.setTitle(getResources().getString(R.string.title_checkout));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                type= null;
                order = null;
                ORDER_LIST = null;
                cat_type = null;
                payable=0;
                if (cat_type.equals("advance")){
                    date = null;
                    time = null;
                    Comment = null;
                }
                coupon_code=null;
            } else {
                type= extras.getString("type");
                order = extras.getString("order");
                payable = extras.getDouble("payable");
                ORDER_LIST = extras.getString("ORDER_LIST");
                coupon_code = extras.getString("coupon_code");
                cat_type = extras.getString("cat_type");
                cwall = extras.getString("cwall");
                if (cat_type.equals("advance")){
                    date = extras.getString("date");
                    time = extras.getString("time");
                    Comment = extras.getString("comment");
                }
            }
        } else {
            type= (String) savedInstanceState.getSerializable("type");
            order= (String) savedInstanceState.getSerializable("order");
            payable= (Double) savedInstanceState.getSerializable("payable");
            ORDER_LIST= (String) savedInstanceState.getSerializable("ORDER_LIST");
            cat_type= (String) savedInstanceState.getSerializable("cat_type");
            cwall= (String) savedInstanceState.getSerializable("cwall");
            if (cat_type.equals("advance")){
                date = (String) savedInstanceState.getSerializable("date");
                time = (String) savedInstanceState.getSerializable("time");
                Comment = (String) savedInstanceState.getSerializable("comment");
            }
        }
        order=order.replace("\n","<br>");

        try {
            jsonObject=new JSONObject(ORDER_LIST);
            Log.e("jsonObj",jsonObject+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        edtName = (EditText) findViewById(R.id.edtName);
        edtNumberOfPeople = (EditText) findViewById(R.id.edtNumberOfPeople);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        edtRoomNo = (EditText) findViewById(R.id.edtRoomNo);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        btnDate = (Button) findViewById(R.id.btnDate);
        btnTime = (Button) findViewById(R.id.btnTime);
        spinCity = findViewById(R.id.city);
        spinSubCity = findViewById(R.id.subcity);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtOrderList = (EditText) findViewById(R.id.edtOrderList);
        edtComment = (EditText) findViewById(R.id.edtComment);
        btnSend = (Button) findViewById(R.id.btnSend);
        sclDetail = (ScrollView) findViewById(R.id.sclDetail);
        progressBar = (ProgressBar) findViewById(R.id.prgLoading);
        txtAlert = (TextView) findViewById(R.id.txtAlert);
        autoCompleteTextView =  findViewById(R.id.auto_text_bg);
        rel_room =  findViewById(R.id.rel_room);

        edtName.setText(SharedPref.getUserName(this));
        edtPhone.setText(SharedPref.getMobileNumber(this));

        if (cat_type.equals("advance"))
            edtComment.setText(Comment);

        if(type.equalsIgnoreCase(Constant_Api.TAKE_AWAY)){
            edtAddress.setVisibility(View.GONE);
            edtEmail.setVisibility(View.VISIBLE);
            edtRoomNo.setVisibility(View.GONE);
            rel_room.setVisibility(View.VISIBLE);
        }else{
            edtAddress.setVisibility(View.VISIBLE);
            edtEmail.setVisibility(View.VISIBLE);
            edtRoomNo.setVisibility(View.GONE);
            rel_room.setVisibility(View.GONE);
        }
        dateText = (EditText) findViewById(R.id.dateText);
        timeText = (EditText) findViewById(R.id.timeText);

        getCityList();
        /*ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cities);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/
        spinCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                city=null;
                if (i!=0) {
                    city = cities.get(i).getCity_name();
                    delivery = Double.valueOf(cities.get(i).getDelivery_amount());
                    spinpos = Integer.parseInt(cities.get(i).getCid());
                    spinSubCity.setVisibility(View.VISIBLE);
                    getSubCityList(spinpos);
                    try {
                        jsonObject = new JSONObject(ORDER_LIST);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("amount");
                        Double total_price = Double.valueOf(jsonObject1.getString("total_price"));
                        jsonObject1.put("total_price", (total_price + delivery)); //updated total
                        jsonObject1.put("delivery_charges", delivery); //new added

                        jsonObject.put("amount", jsonObject1); //updated json

                        Log.e("jsonObject", String.valueOf(jsonObject));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    appendlist = "";
                    appendlist += "Delivery Charges : " + delivery + " INR<br>";
                    appendlist += "Total Payable : " + (payable + delivery) + " INR<br>";
                    Log.e("reservation",order+appendlist);
                    edtOrderList.setText(Html.fromHtml("Your order is:<p>" + order + appendlist + "</p>"));
                }
                else
                    spinSubCity.setVisibility(View.GONE);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                /*city = cities.get(1).getCity_name();
                delivery = Double.valueOf(cities.get(1).getDelivery_amount());
                try {
                    jsonObject=new JSONObject(ORDER_LIST);
                    JSONObject jsonObject1=jsonObject.getJSONObject("amount");
                    String total_price= jsonObject1.getString("total_price");
                    jsonObject1.put("total_price",(total_price+delivery)); //updated total
                    jsonObject1.put("delivery_charges",delivery); //new added

                    jsonObject.put("amount",jsonObject1); //updated json

                    Log.e("jsonObject", String.valueOf(jsonObject));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                appendlist="";
                appendlist += "Delivery Charges : "+delivery+" INR<br>";
                appendlist += "Total Payable : "+(payable+delivery)+" INR<br>";
                edtOrderList.setText(Html.fromHtml("Your order is:<p>"+order+appendlist+"</p>"));*/
            }
        });

        spinSubCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position!=0)
                    subcity = subcities.get(position).getCity_name();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        // attaching data adapter to spinner

        /*try {
            adapter = new ArrayAdapter(ActivityReservation.this, android.R.layout.simple_dropdown_item_1line, roomNoArray);
            autoCompleteTextView.setAdapter(adapter);
            autoCompleteTextView.setThreshold(1);

            autoCompleteTextView.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    autoCompleteTextView.showDropDown();
                    autoCompleteTextView.requestFocus();
                    return false;
                }
            });


            autoCompleteTextView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                    selectedRoomNo =parent.getItemAtPosition(i).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    selectedRoomNo ="Please select Room No";
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }*/


        dbhelper = new DBHelper(this);
        try {
            dbhelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        new getTaxCurrency().execute();

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), DATE_DIALOG_ID);
            }
        });


        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), DATE_DIALOG_ID);
            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), TIME_DIALOG_ID);
            }
        });



        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dbhelper.deleteAllData();
                dbhelper.close();
                Date_Time = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                Name = edtName.getText().toString();
                NumberOfPeople = edtNumberOfPeople.getText().toString();
                Email = edtEmail.getText().toString();
                Address = edtAddress.getText().toString();
                Date = dateText.getText().toString();
                Time = timeText.getText().toString();
                Phone = edtPhone.getText().toString();
                Comment = edtComment.getText().toString();
                roomNo = autoCompleteTextView.getText().toString();
//                Date_Time = Date + " " + Time;


                //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

               if(type.equalsIgnoreCase(Constant_Api.TAKE_AWAY)){
                   if (Name.equalsIgnoreCase("")) {
                       Toast.makeText(ActivityReservation.this, R.string.entername, Toast.LENGTH_SHORT).show();
                       edtName.requestFocus();
                   }else if (Phone.equalsIgnoreCase("")) {
                       Toast.makeText(ActivityReservation.this, R.string.enterphone, Toast.LENGTH_SHORT).show();
                   }else if (Address.equalsIgnoreCase("")) {
                       Toast.makeText(ActivityReservation.this, R.string.enteremail, Toast.LENGTH_SHORT).show();
                       edtAddress.requestFocus();
                   } else if ((data.size() == 0)) {
                       Toast.makeText(ActivityReservation.this, R.string.order_alert, Toast.LENGTH_SHORT).show();
                   } else if(roomNo.equalsIgnoreCase("")){
                       Toast.makeText(ActivityReservation.this, R.string.select_room, Toast.LENGTH_SHORT).show();
                       autoCompleteTextView.requestFocus();
                   }else if (Phone.length()<10) {
                       Toast.makeText(ActivityReservation.this, R.string.tenphone, Toast.LENGTH_SHORT).show();
                       edtPhone.requestFocus();
                   }/*else if (!Email.matches(emailPattern)) {
                       Toast.makeText(ActivityReservation.this, R.string.wrongemail, Toast.LENGTH_SHORT).show();
                       edtEmail.requestFocus();
                   }*/else {
//                       galleryDetail();
                       //placeOrder(Name, "1", Phone, Comment, Email);
                       new sendData().execute();
                   }
               }else {
                   if (Name.equalsIgnoreCase("")) {
                       Toast.makeText(ActivityReservation.this, R.string.entername, Toast.LENGTH_SHORT).show();
                       edtName.requestFocus();
                   }else if (Phone.equalsIgnoreCase("")) {
                       Toast.makeText(ActivityReservation.this, R.string.enterphone, Toast.LENGTH_SHORT).show();
                   }else if (Address.equalsIgnoreCase("")) {
                       Toast.makeText(ActivityReservation.this, R.string.enteradd, Toast.LENGTH_SHORT).show();
                       edtAddress.requestFocus();
                   }/*else if (Email.equalsIgnoreCase("")) {
                       Toast.makeText(ActivityReservation.this, R.string.enteremail, Toast.LENGTH_SHORT).show();
                       edtEmail.requestFocus();
                   }*/ /*else if ((data.size() == 0)) {
                       Toast.makeText(ActivityReservation.this, R.string.order_alert, Toast.LENGTH_SHORT).show();
                   }*/ else if (Phone.length()<10) {
                       Toast.makeText(ActivityReservation.this, R.string.tenphone, Toast.LENGTH_SHORT).show();
                       edtPhone.requestFocus();
                   }else if (city==null || cities.equals("")) {
                       Toast.makeText(ActivityReservation.this, "Please Select City", Toast.LENGTH_SHORT).show();
                       edtEmail.requestFocus();
                   }else {
                       new sendData().execute();
                       //placeOrder(Name, "1", Phone, Comment, Email);
                   }
               }
            }
        });
    }

    private void getCityList() {
        AsyncHttpClient client = new AsyncHttpClient(true,80,443);client.addHeader("App-Id",Constant_Api.HOTEL_ID);
        client.get(Constant_Api.citylist, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                cityList.clear();
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray("CITY_LIST");

                    cities.add(null);
                    cityList.add("Select City");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String cid = object.getString("id");
                        String city_name = object.getString("city_name");
                        String devamt = object.getString("delivery_amount");

                        cities.add(new CityList(cid, city_name,devamt));
                        cityList.add(city_name+" ( Delivery Charges : "+devamt+" )");
                    }

                    cityAdapter = new ArrayAdapter<>(ActivityReservation.this, android.R.layout.simple_spinner_dropdown_item,cityList);
                    spinCity.setAdapter(cityAdapter);
                    //progressBar.hide();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //progressBar.hide();
                Log.e("cityerror",error+"");
            }
        });
    }

    private void getSubCityList(final int spinpos){
        AsyncHttpClient client = new AsyncHttpClient(true,80,443);client.addHeader("App-Id",Constant_Api.HOTEL_ID);
        client.get(Constant_Api.subcitylist+spinpos, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                subcityList.clear();
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray("SUB_CITY_LIST");
                    subcities.add(null);
                    subcityList.add("Select Sub City");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("id");
                        String cid = object.getString("city_id");
                        String city_name = object.getString("sub_city_name");

                        subcities.add(new SubCityList(id,cid, city_name));
                        subcityList.add(city_name);
                    }

                    subcityAdapter = new ArrayAdapter<>(ActivityReservation.this, android.R.layout.simple_spinner_dropdown_item,subcityList);
                    spinSubCity.setAdapter(subcityAdapter);
                    //progressBar.hide();

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
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            mYear = year;
            mMonth = month;
            mDay = day;

//            btnDate.setText(new StringBuilder()
//                    .append(mYear).append("/")
//                    .append(mMonth + 1).append("/")
//                    .append(mDay).append(" "));

            dateText.setText(new StringBuilder()
                    .append(mYear).append("-")
                    .append(mMonth + 1).append("-")
                    .append(mDay).append(" "));

        }
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mHour = hourOfDay;
            mMinute = minute;

//            btnTime.setText(new StringBuilder()
//                    .append(pad(mHour)).append(":")
//                    .append(pad(mMinute)).append(":")
//                    .append("00"));

            timeText.setText(new StringBuilder()
                    .append(pad(mHour)).append(":")
                    .append(pad(mMinute)).append(":")
                    .append("00"));
        }
    }

    public class getTaxCurrency extends AsyncTask<Void, Void, Void> {

        getTaxCurrency() {
            if (!progressBar.isShown()) {
                progressBar.setVisibility(View.VISIBLE);
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
            progressBar.setVisibility(View.GONE);
            if (IOConnect == 0) {
                new getDataTask().execute();
            } else {
                txtAlert.setVisibility(View.VISIBLE);
            }
        }
    }

    public class getDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            //getDataFromDatabase();
            //order += "Total Payable : "+payable +" INR";
            edtOrderList.setText(Html.fromHtml("Your order is:<p>"+order+"</p>"));
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
            sclDetail.setVisibility(View.VISIBLE);

        }
    }

    public class sendData extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(ActivityReservation.this, "", getString(R.string.placing_order), true);

        }

        @Override
        protected Void doInBackground(Void... params) {
            //Toast.makeText(ActivityReservation.this, "City :"+city, Toast.LENGTH_SHORT).show();
            Result = placeOrder(Name, "1", Phone, Comment, Email);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            resultAlert(Result);
        }
    }

    public void resultAlert(String HasilProses) {

        try {
            String[] separated = HasilProses.split("end");

            String response= separated[1];
            JSONObject jsonObject = new JSONObject(response);

            JSONArray jsonArray = jsonObject.getJSONArray("ORDER_PLACED");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object = jsonArray.getJSONObject(i);
                String error = object.getString("error");
                int order_id = Integer.parseInt(object.getString("order_id"));
                String message = object.getString("message");
                Intent intent = new Intent(ActivityReservation.this, ActivityConfirmMessage.class).putExtra("order_id",order_id);
                dbhelper.deleteAllData();
                startActivity(intent);
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private String placeOrder(String name, String number_of_people, String phone, String comment, String email) {
        //dialog = ProgressDialog.show(ActivityReservation.this, "", getString(R.string.placing_order), true);
        String amount = String.valueOf(cwall);
        if (!cat_type.equals("advance")) {
            date = "";
            time = "";
        }
        ORDER_LIST= String.valueOf(jsonObject);
        if (comment==null)
            comment="No Comments";
        String result = "";
        HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(Constant_Api.add_order);
        String date_time= java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());;
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(15);
            nameValuePairs.add(new BasicNameValuePair("user_id", SharedPref.getUserId(this)));
            nameValuePairs.add(new BasicNameValuePair("name", name));
            nameValuePairs.add(new BasicNameValuePair("number_of_people", number_of_people));
            nameValuePairs.add(new BasicNameValuePair("date_time", ""));
            nameValuePairs.add(new BasicNameValuePair("phone", phone));
            nameValuePairs.add(new BasicNameValuePair("amount", amount));
            Log.e("appendlist",order+appendlist);
            Log.e("jsonORDERLIST",ORDER_LIST);
            nameValuePairs.add(new BasicNameValuePair("order_list", order+appendlist));
            nameValuePairs.add(new BasicNameValuePair("json_order_list", ORDER_LIST));
            nameValuePairs.add(new BasicNameValuePair("comment", comment));
            nameValuePairs.add(new BasicNameValuePair("city", city));
            nameValuePairs.add(new BasicNameValuePair("sub_city", subcity));
            nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("app_id", Constant_Api.HOTEL_ID));
            nameValuePairs.add(new BasicNameValuePair("mobileid", Method.getAndroidID(ActivityReservation.this)));
            nameValuePairs.add(new BasicNameValuePair("address", Address));
            nameValuePairs.add(new BasicNameValuePair("cat_type", cat_type));
            nameValuePairs.add(new BasicNameValuePair("coupon_code", coupon_code));

            if (cat_type.equals("advance")) {
                nameValuePairs.add(new BasicNameValuePair("date", date));
                nameValuePairs.add(new BasicNameValuePair("time", time));
            }

            nameValuePairs.add(new BasicNameValuePair("order_type", Constant_Api.TYPE_HOME));
            request.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            HttpResponse response = client.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            Log.e("placeorder",responseBody);
            result = responseBody;
            //dialog.dismiss();
        } catch (Exception ex) {
            result = "Unable to connect.";
        }
        return result;



        /*String amount = String.valueOf(cwall);
        if (!cat_type.equals("advance")) {
            date = "";
            time = "";
        }
        if (comment==null)
            comment="No Comments";
        String url = Constant_Api.add_order + "user_id=" + SharedPref.getUserId(this) + "&name=" + name
                + "&number_of_people=" + number_of_people + "&date_time=" + "" + "&phone=" + phone
                + "&amount=" + amount + "&order_list=" + order + "&json_order_list=" + ORDER_LIST
                + "&comment=" + comment + "&city=" + city + "&email=" + email
                + "&app_id=" + Constant_Api.HOTEL_ID + "&mobileid=" + Method.getAndroidID(ActivityReservation.this) + "&address=" + ""
                + "&cat_type=" + cat_type + "&date=" + date + "&time=" + time + "&order_type=" + Constant_Api.TYPE_HOME;

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("App-Id", Constant_Api.HOTEL_ID);
        client.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.e("OrderResponse", new String(responseBody));
                String res = new String(responseBody);
                String[] separated = res.split("end");

                String response= separated[1];

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("ORDER_PLACED");

                    for (int i = 0; i <= jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String error = object.getString("error");
                        int order_id = object.getInt("order_id");
                        String success = object.getString("message");

                        if (error.equals("false")) {
                            Intent intent = new Intent(ActivityReservation.this, ActivityConfirmMessage.class).putExtra("order_id", order_id);
                            dbhelper.deleteAllData();
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();
                        }

                    }
                    dialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("catchres", e.toString());
                    Toast.makeText(ActivityReservation.this, "Error Response", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("failure", error.toString());
                Toast.makeText(ActivityReservation.this, "Error Response", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    public String getRequest(
            String name,
            String number_of_people,
            String date_time,
            String phone,
            String order_list,
            String comment,
            String email) {
        String result = "";

        HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(Constant_Api.add_order);

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(15);
            nameValuePairs.add(new BasicNameValuePair("user_id", SharedPref.getUserId(this)));
            nameValuePairs.add(new BasicNameValuePair("name", name));
            nameValuePairs.add(new BasicNameValuePair("number_of_people", number_of_people));
            nameValuePairs.add(new BasicNameValuePair("date_time", ""));
            nameValuePairs.add(new BasicNameValuePair("amount", String.valueOf(cwall)));
            nameValuePairs.add(new BasicNameValuePair("phone", phone));
            nameValuePairs.add(new BasicNameValuePair("order_list", order));
            nameValuePairs.add(new BasicNameValuePair("comment", comment));
            nameValuePairs.add(new BasicNameValuePair("city", city));
            nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("app_id", Constant_Api.HOTEL_ID));
            nameValuePairs.add(new BasicNameValuePair("mobileid", Method.getAndroidID(ActivityReservation.this)));
            nameValuePairs.add(new BasicNameValuePair("address", Address));
            nameValuePairs.add(new BasicNameValuePair("cat_type", cat_type));
            if (cat_type.equals("advance")) {
                nameValuePairs.add(new BasicNameValuePair("date", date));
                nameValuePairs.add(new BasicNameValuePair("time", time));
            }

            nameValuePairs.add(new BasicNameValuePair("order_type", type));
            request.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            HttpResponse response = client.execute(request);
            result = request(response);
        } catch (Exception ex) {
            result = "Unable to connect.";
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

    public void getDataFromDatabase() {

        data = dbhelper.getAllData();

        double Order_price = 0;
        double Total_price = 0;
        double tax = 0;

        for (int i = 0; i < data.size(); i++) {
            ArrayList<Object> row = data.get(i);

            String Menu_name = row.get(1).toString();
            String Quantity = row.get(2).toString();

            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
            DecimalFormat formatData = (DecimalFormat)numberFormat;
            formatData.applyPattern("#.##");
            double Sub_total_price = Double.parseDouble(formatData.format(Double.parseDouble(row.get(3).toString())));

            Order_price += Sub_total_price;

            OrderList += (Quantity + " " + Menu_name + " " + Sub_total_price + " INR,\n");
        }

        if (OrderList.equalsIgnoreCase("")) {
            OrderList += getString(R.string.no_order_menu);
        }

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
        DecimalFormat formatData = (DecimalFormat)numberFormat;
        formatData.applyPattern("#.##");
        //tax = Double.parseDouble(formatData.format(Order_price * (Tax / 100)));
        //Total_price = Double.parseDouble(formatData.format(Order_price - tax));
        OrderList += "\nTotal Order : " + total + " " + Currency ;
        OrderList += "\nWallet Amount Used : " + cwall + " " + Currency ;
        OrderList += "\nTotal Payable : " + (total-Integer.parseInt(cwall)) + " " + Currency ;
        edtOrderList.setText(OrderList);
    }

    private static String pad(int c) {
        if (c >= 10) {
            return String.valueOf(c);
        } else {
            return "0" + String.valueOf(c);
        }
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
}