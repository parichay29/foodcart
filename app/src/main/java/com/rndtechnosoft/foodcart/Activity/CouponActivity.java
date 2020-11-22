package com.rndtechnosoft.foodcart.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rndtechnosoft.foodcart.Adapter.CouponCodeAdapter;
import com.rndtechnosoft.foodcart.Item.CouponList;
import com.rndtechnosoft.foodcart.R;
import com.rndtechnosoft.foodcart.Util.Constant_Api;
import com.rndtechnosoft.foodcart.Util.SharedPref;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class CouponActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView coupon_list;
    CouponCodeAdapter codeAdapter;
    ArrayList<CouponList> couponLists;
    private EditText coupon_code;
    private Button apply_button;
    String amount;
    String sub_total;
    boolean ischecked;
    int wamt;
    private String error;
    private String pay_amount;
    private String title;
    private String message;
    private String api_amount;
    private String discount;
    AVLoadingIndicatorView progress_coupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        toolbar = (Toolbar) findViewById(R.id.toolbar_about_us);
        toolbar.setTitleTextColor(getResources().getColor(R.color.finestBlack));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.left_arrow));
        toolbar.setTitle(getResources().getString(R.string.title_coupon));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        sub_total = extras.getString("amount");
        ischecked = extras.getBoolean("ischecked");
        wamt = extras.getInt("wamt");
        Log.e("subtotal->>>",sub_total);
        Log.e("ischecked->>>",""+ischecked);

        String[] separated = sub_total.split(getResources().getString(R.string.rupee));
        amount= separated[1].split("\\.")[0];
        Log.e("amt->>>",amount);

        coupon_code = findViewById(R.id.coupon_code);
        apply_button = findViewById(R.id.apply_button);
        coupon_list = findViewById(R.id.coupon_list);
        progress_coupon = findViewById(R.id.progresbar_coupon);
        coupon_list.setLayoutManager(new LinearLayoutManager(this));

        coupon_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                apply_button.setTextColor(getResources().getColor(R.color.colorAccent));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        apply_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(CouponActivity.this);
                getDiscount(coupon_code.getText().toString(),amount);
            }
        });

        getCouponList();
    }

    public void getDiscount(final String coupon_code, final String amount) {

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.addHeader("userid", SharedPref.getUserId(CouponActivity.this));
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
                        //showCustomDialog(title,message);
                    }
                    startActivity(new Intent(CouponActivity.this,ActivityCart.class)
                            .putExtra("title",title).putExtra("message",message)
                            .putExtra("amount",amount).putExtra("pay_amount",pay_amount)
                            .putExtra("discount",discount).putExtra("coupon_code",coupon_code)
                            .putExtra("ischecked",ischecked).putExtra("wamt",wamt));
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //progressBar.setVisibility(View.GONE);
            }
        });

    }

    public void showCustomDialog(String title,String message) {

        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.my_dialog, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
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
                startActivity(new Intent(CouponActivity.this,ActivityCart.class));
            }
        });

    }

    private void getCouponList() {

        progress_coupon.setVisibility(View.VISIBLE);

        AsyncHttpClient client = new AsyncHttpClient(true, 80, 443);
        client.get(Constant_Api.coupon_list, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                couponLists = new ArrayList<>();
                try {
                    couponLists.clear();
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonArray = jsonObject.getJSONArray("COUPON_LIST");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("id");
                        String title = object.getString("title");
                        String tandc = object.getString("tandc");
                        String min_order = object.getString("min_order");
                        String exp_date = object.getString("exp_date");
                        String coupon_code = object.getString("coupon_code");

                        couponLists.add(new CouponList(id, title, tandc, min_order, exp_date, coupon_code));
                    }

                    codeAdapter = new CouponCodeAdapter(CouponActivity.this, couponLists,amount);
                    if (couponLists == null || couponLists.size() == 0) {
//                        tvNoData.setVisibility(View.VISIBLE);
                        coupon_list.setVisibility(View.GONE);
                    } else {
//                        tvNoData.setVisibility(View.GONE);
                        coupon_list.setVisibility(View.VISIBLE);
                        coupon_list.setAdapter(codeAdapter);
                    }

                    progress_coupon.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    progress_coupon.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progress_coupon.setVisibility(View.GONE);
                //Toast.makeText(CouponActivity.this,"FAILURE",Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
