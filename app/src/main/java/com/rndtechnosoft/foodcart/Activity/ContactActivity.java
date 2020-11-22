package com.rndtechnosoft.foodcart.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.rndtechnosoft.foodcart.R;
import com.rndtechnosoft.foodcart.Util.Constant_Api;
import com.rndtechnosoft.foodcart.Util.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ContactActivity extends AppCompatActivity {

    private EditText editText_name, editText_email, editText_phoneNumber, editText_subject, editText_description;
    private Button button_submit;
    private String name, email, phoneNumber, subject, description;
    private Method method;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        toolbar = (Toolbar) findViewById(R.id.toolbar_contact);
        toolbar.setTitleTextColor(getResources().getColor(R.color.finestBlack));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.left_arrow));
        toolbar.setTitle(getResources().getString(R.string.contact_us));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        method = new Method(this);

        editText_name = (EditText) findViewById(R.id.textView_name_contactUS_fragment);
        editText_email = (EditText) findViewById(R.id.textView_email_contactUS_fragment);
        editText_phoneNumber = (EditText) findViewById(R.id.textView_phoneNo_contactUS_fragment);
        editText_subject = (EditText) findViewById(R.id.textView_subject_contactUS_fragment);
        editText_description = (EditText) findViewById(R.id.textView_description_contactUS_fragment);
        button_submit = (Button) findViewById(R.id.button_contactus_fragment);


        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Method.isNetworkAvailable(ContactActivity.this)) {

                    name = editText_name.getText().toString();
                    email = editText_email.getText().toString();
                    phoneNumber = editText_phoneNumber.getText().toString();
                    subject = editText_subject.getText().toString();
                    description = editText_description.getText().toString();

                    form();

                } else {
                    Toast.makeText(ContactActivity.this, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void contactUs() {

        String url = Constant_Api.contact_us + "name=" + name + "&email=" + email + "&phone=" + phoneNumber + "&subject=" + subject + "&message=" + description;

        AsyncHttpClient client = new AsyncHttpClient(true,80,443);client.addHeader("App-Id",Constant_Api.HOTEL_ID);
        client.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray("SINGLE_HOTEL_APP");

                    for (int i = 0; i <= jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String msg = object.getString("msg");
                        String success = object.getString("success");

                        if (success.equals("1")) {
                            Toast.makeText(ContactActivity.this, msg, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ContactActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void form() {

        editText_name.setError(null);
        editText_email.setError(null);
        editText_phoneNumber.setError(null);
        editText_subject.setError(null);
        editText_description.setError(null);

        if (name.isEmpty() || name.equals("")) {
            editText_name.requestFocus();
            editText_name.setError(getResources().getString(R.string.name_ContactUs));
        } else if (!isValidMail(email) || email.isEmpty()) {
            editText_email.requestFocus();
            editText_email.setError(getResources().getString(R.string.email_ContactUs));
        } else if (phoneNumber.isEmpty()) {
            editText_phoneNumber.requestFocus();
            editText_phoneNumber.setError(getResources().getString(R.string.phoneNumber_contactUs));
        } else if (subject.isEmpty() || subject.equals("")) {
            editText_subject.requestFocus();
            editText_subject.setError(getResources().getString(R.string.subject_ContactUs));
        } else if (description.isEmpty() || description.equals("")) {
            editText_description.requestFocus();
            editText_description.setError(getResources().getString(R.string.description_ContactUs));
        } else {
            editText_name.setText("");
            editText_email.setText("");
            editText_phoneNumber.setText("");
            editText_subject.setText("");
            editText_description.setText("");
            contactUs();
        }
    }
}
