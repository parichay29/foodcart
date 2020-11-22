package com.rndtechnosoft.foodcart.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.rndtechnosoft.foodcart.BuildConfig;
import com.rndtechnosoft.foodcart.Helper.MultipartRequest;
import com.rndtechnosoft.foodcart.R;
import com.rndtechnosoft.foodcart.Util.App;
import com.rndtechnosoft.foodcart.Util.Constant_Api;
import com.rndtechnosoft.foodcart.Util.Method;
import com.rndtechnosoft.foodcart.Util.SharedPref;
import com.rndtechnosoft.foodcart.dialogs.PostImageChooseDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import de.hdodenhof.circleimageview.CircleImageView;

import static com.rndtechnosoft.foodcart.Util.Constant_Api.APP_TEMP_FOLDER;

public class ProfileEditActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, PostImageChooseDialog.AlertPositiveListener {

    CircleImageView profile_image,image_edit;
    EditText etName,etEmail,etPhone,etAddress,etLocation;
    String name,email,gender,phone,address,dob,doa,image,latitude,longitude,zipcode,location;
    EditText etDob,etDoa;
    Button btnUpdate;
    Spinner spinGender;
    private static int RESULT_LOAD_IMAGE = 1;
    String ba1;
    String Result;
    String picturePath;
    String userid;
    private String selectedPostImg = "";
    private Uri selectedImage;
    private Uri outputFileUri;
    public static final int SELECT_POST_IMG = 3;
    public static final int CREATE_POST_IMG = 5;
    Toolbar toolbar;
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        profile_image=findViewById(R.id.profile_image);
        image_edit=findViewById(R.id.image);
        etName=findViewById(R.id.edtName);
        etEmail=findViewById(R.id.edtEmail);
        etPhone=findViewById(R.id.edtPhone);
        etAddress=findViewById(R.id.edtAddress);
        etDob=findViewById(R.id.edtDOB);
        etDoa=findViewById(R.id.edtDOA);
        etLocation=findViewById(R.id.edtLocation);
        spinGender=findViewById(R.id.gender);
        btnUpdate=findViewById(R.id.btnUpdate);
        userid = SharedPref.getUserId(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar_profile_edit);
        toolbar.setTitleTextColor(getResources().getColor(R.color.finestBlack));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.left_arrow));
        toolbar.setTitle(getResources().getString(R.string.edit_profile));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        List<String> categories = new ArrayList<String>();
        categories.add("Please select Gender");
        categories.add("Male");
        categories.add("Female");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinGender.setOnItemSelectedListener(this);

        // attaching data adapter to spinner
        spinGender.setAdapter(dataAdapter);

        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker(etDob);
            }
        });

        etDoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker(etDoa);
            }
        });

        image_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choiceImage();
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedPostImg.length() == 0) {

                    choiceImage();

                }
                /*Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);*/
            }
        });

        if (selectedPostImg != null && selectedPostImg.length() > 0) {

            profile_image.setImageURI(FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", new File(selectedPostImg)));
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=etName.getText().toString();
                email=etEmail.getText().toString();
                address=etAddress.getText().toString();
                dob=etDob.getText().toString();
                doa=etDoa.getText().toString();
                location=etLocation.getText().toString();
                if (Method.isNetworkAvailable(ProfileEditActivity.this)) {
                    //setData(name,email,gender,image,address,dob,doa,latitude,longitude,location,zipcode);
                    //new sendData().execute();
                    File f = new File(Environment.getExternalStorageDirectory() + File.separator + APP_TEMP_FOLDER, "photo.jpg");
                    Result = setData(name,email,gender,image,address,dob,doa,latitude,longitude,location,zipcode,f);
                } else {
                    Toast.makeText(ProfileEditActivity.this, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
                    //progressBar.hide();
                }
            }
        });

        if (Method.isNetworkAvailable(this)) {
            getData();
        } else {
            Toast.makeText(this, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            //progressBar.hide();
        }
    }

    private void datePicker(final EditText date) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    public void choiceImage() {

        android.app.FragmentManager fm = this.getFragmentManager();

        PostImageChooseDialog alert = new PostImageChooseDialog();
        alert.show(fm, "alert_dialog_image_choose");
    }

    @Override
    public void onImageFromGallery() {
        imageFromGallery();
    }

    @Override
    public void onImageFromCamera() {
        imageFromCamera();
    }

    public void imageFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, getText(R.string.label_select_img)), SELECT_POST_IMG);
    }

    public void imageFromCamera() {

        try {

            File root = new File(Environment.getExternalStorageDirectory(), APP_TEMP_FOLDER);

            if (!root.exists()) {

                root.mkdirs();
            }

            File sdImageMainDirectory = new File(root, "photo.jpg");

            outputFileUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", sdImageMainDirectory);

            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(cameraIntent, CREATE_POST_IMG);


        } catch (Exception e) {

            Toast.makeText(this, "Error occured. Please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public class sendData extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(ProfileEditActivity.this, "", getString(R.string.sending_alert), true);

        }

        @Override
        protected Void doInBackground(Void... params) {
            /*Bitmap bm = BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 90, bao);
            byte[] ba = bao.toByteArray();
            ba1 = Base64.encodeToString(ba, Base64.NO_WRAP);

            Log.e("base64", "-----" + ba1);
            image=System.currentTimeMillis() + ".jpg";*/
            // Upload image to server
            /*ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            //nameValuePairs.add(new BasicNameValuePair("base64", ba1));
            nameValuePairs.add(new BasicNameValuePair("ImageName", System.currentTimeMillis() + ".jpg"));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(URL);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                String st = EntityUtils.toString(response.getEntity());
                Log.v("log_tag", "In the try Loop" + st);

            } catch (Exception e) {
                Log.v("log_tag", "Error in http connection " + e.toString());
            }*/
            /*File f = new File(Environment.getExternalStorageDirectory() + File.separator + APP_TEMP_FOLDER, "photo.jpg");
            Result = setData(name,email,gender,image,address,dob,doa,latitude,longitude,location,zipcode,f);*/
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            onBackPressed();
            //resultAlert(Result);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            *//*CircleImageView profile=findViewById(R.id.profile_image);
            profile.setImageBitmap(BitmapFactory.decodeFile(picturePath));*//*
            Picasso.with(ProfileEditActivity.this).load(data.getData()).centerCrop().fit()
                    .into((CircleImageView) findViewById(R.id.profile_image));
        }*/
        if (requestCode == SELECT_POST_IMG && resultCode == RESULT_OK && null != data) {

            selectedImage = data.getData();

            selectedPostImg = getImageUrlWithAuthority(this, selectedImage, "photo.jpg");

            try {

                selectedPostImg = Environment.getExternalStorageDirectory() + File.separator + APP_TEMP_FOLDER + File.separator + "photo.jpg";

                profile_image.setImageURI(Uri.fromFile(new File(selectedPostImg)));

            } catch (Exception e) {

                Log.e("OnSelectPostImage", e.getMessage());
            }

        } else if (requestCode == CREATE_POST_IMG && resultCode == this.RESULT_OK) {

            try {

                selectedPostImg = Environment.getExternalStorageDirectory() + File.separator + APP_TEMP_FOLDER + File.separator + "photo.jpg";

                profile_image.setImageURI(Uri.fromFile(new File(selectedPostImg)));

            } catch (Exception ex) {

                Log.v("OnCameraCallBack", ex.getMessage());
            }

        }
    }

    public static String getImageUrlWithAuthority(Context context, Uri uri, String fileName) {

        InputStream is = null;

        if (uri.getAuthority() != null) {

            try {

                is = context.getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);

                return writeToTempImageAndGetPathUri(context, bmp, fileName).toString();

            } catch (FileNotFoundException e) {

                e.printStackTrace();

            } finally {

                try {

                    if (is != null) {

                        is.close();
                    }

                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public static String writeToTempImageAndGetPathUri(Context inContext, Bitmap inImage, String fileName) {

        String file_path = Environment.getExternalStorageDirectory() + File.separator + APP_TEMP_FOLDER;
        File dir = new File(file_path);
        if (!dir.exists()) dir.mkdirs();

        File file = new File(dir, fileName);

        try {

            FileOutputStream fos = new FileOutputStream(file);

            inImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            fos.flush();
            fos.close();

        } catch (FileNotFoundException e) {

            Toast.makeText(inContext, "Error occured. Please try again later.", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {

            e.printStackTrace();
        }

        return Environment.getExternalStorageDirectory() + File.separator + APP_TEMP_FOLDER + File.separator + "photo.jpg";
    }

    public void RequestMultiPart(File file, String filename, String boundary, String url, String fileField, Map<String,String> params, final MultipartRequest.ApiResponse<String> completion ) {

        final String reqUrl = url;
        MultipartRequest imageUploadReq = new MultipartRequest(reqUrl,params,file,filename,fileField,
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Multipart Request Url: ", reqUrl);
                        Log.d("Multipart ERROR", "error => " + error.toString());
                        completion.onCompletion(error.toString());
                        //displayVolleyResponseError(error);
                    }
                },
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("MediaSent Response", response);
                        completion.onCompletion(response);

                    }
                }
        ) {

            /* The following method sets the cookies in the header, I needed it for my server
             but you might want to remove it if it is not useful in your case */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                CookieManager manager = App.getInstance().getCookieManager();
                List<HttpCookie> cookies = manager.getCookieStore().getCookies();
                String cookie = "";
                for (HttpCookie eachCookie : cookies) {
                    String cookieName = eachCookie.getName().toString();
                    String cookieValue = eachCookie.getValue().toString();
                    cookie += cookieName + "=" + cookieValue + "; ";
                }
                headers.put("Cookie", cookie);
                return headers;
            }

        };

        imageUploadReq.setRetryPolicy(new DefaultRetryPolicy(1000 * 60, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        App.getInstance().addToRequestQueue(imageUploadReq);
    }

    public String setData(String name, String email, String gender, String image, String address, String dob, String doa, String latitude, String longitude, String location, String zipcode,final File media) {

        String result = "";


        //progressBar.show();
        HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(Constant_Api.profile_edit);

        try {

            String uuid = UUID.randomUUID().toString();
            String boundary = "----------------------------"+uuid;
            String fileName = uuid+".jpg";

            Map<String,String> nameValuePairs = new HashMap<>();
            nameValuePairs.put("name", name);
            nameValuePairs.put("email", email);
            nameValuePairs.put("gender", gender);
            //nameValuePairs.put("image", image);
            nameValuePairs.put("address", address);
            nameValuePairs.put("dob", dob);
            nameValuePairs.put("doa", doa);
            nameValuePairs.put("latitude", "");
            nameValuePairs.put("longitude", "");
            nameValuePairs.put("location", location);
            nameValuePairs.put("zipcode", "");
            nameValuePairs.put("user_id", userid);

            if(media.exists())
                this.RequestMultiPart(media, fileName, boundary, Constant_Api.profile_edit, "image", nameValuePairs, new MultipartRequest.ApiResponse<String>() {
                    @Override
                    public void onCompletion(String result) {


                        try {

                            //hidepDialog();
                            JSONObject data = new JSONObject(result);
                            JSONObject obj = data.getJSONObject("ADD_FEEDS");
                            String errorFlag= obj.getString("error");
                            if(errorFlag.equals("false"))
                            {
                                if(media.exists())
                                    media.delete();

                                //Toast.makeText(this,getResources().getString(R.string.success_order),Toast.LENGTH_LONG).show();
                                finish();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        finally {


                        }
                        // completion.onCompletion(res);
                    }
                });
            else {
                new updatepro().execute();
            }

            /*request.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
            HttpResponse response = client.execute(request);
            result = request(response);*/
            onBackPressed();
        } catch (Exception ex) {
            result = "Unable to connect.";
        }
        return result;
    }

    public void UpdateData(){
        HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(Constant_Api.profile_edit);
        try {
            List<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>(15);
            nameValuePairs1.add(new BasicNameValuePair("name", name));
            nameValuePairs1.add(new BasicNameValuePair("email", email));
            nameValuePairs1.add(new BasicNameValuePair("gender", gender));
            //nameValuePairs.add(new BasicNameValuePair("image", image));
            nameValuePairs1.add(new BasicNameValuePair("address", address));
            nameValuePairs1.add(new BasicNameValuePair("dob", dob));
            nameValuePairs1.add(new BasicNameValuePair("doa", doa));
            nameValuePairs1.add(new BasicNameValuePair("latitude", ""));
            nameValuePairs1.add(new BasicNameValuePair("longitude", ""));
            nameValuePairs1.add(new BasicNameValuePair("location", location));
            nameValuePairs1.add(new BasicNameValuePair("zipcode", ""));
            nameValuePairs1.add(new BasicNameValuePair("user_id", userid));
            request.setEntity(new UrlEncodedFormEntity(nameValuePairs1, HTTP.UTF_8));
            HttpResponse response = client.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            Log.e("placeorder",responseBody);
            //result = responseBody;
        } catch (Exception ex) {
            //result = "Unable to connect.";
        }
    }

    public class updatepro extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(ProfileEditActivity.this, "", getString(R.string.sending_alert), true);

        }

        @Override
        protected Void doInBackground(Void... params) {
            UpdateData();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //dialog.dismiss();
            //resultAlert();
            //onBackPressed();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // On selecting a spinner item
        gender = adapterView.getItemAtPosition(i).toString();

        // Showing selected spinner item
        //Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void getData() {

        //progressBar.show();

        AsyncHttpClient client = new AsyncHttpClient();
        /*client.addHeader("CalligraphyApplication-Id",Constant_Api.HOTEL_ID);*/
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

                        name = object.getString("name");
                        email = object.getString("email");
                        phone = object.getString("mobile");
                        gender = object.getString("gender");
                        image = object.getString("image");
                        dob = object.getString("dob");
                        doa = object.getString("doa");
                        address = object.getString("address");
                        location = object.getString("location");

                    }

                    try {
                        Glide.with(ProfileEditActivity.this).load(image).into(profile_image);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    etName.setText(name);
                    etEmail.setText(email);
                    etPhone.setText(phone);
                    etLocation.setText(location);
                    etAddress.setText(address);
                    if (gender.equals("Male"))
                        spinGender.setSelection(1);
                    else if (gender.equals("Female"))
                        spinGender.setSelection(2);
                    else
                        spinGender.setSelection(0);
                    etDob.setText(dob);
                    etDoa.setText(doa);
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
}
