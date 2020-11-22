package com.rndtechnosoft.foodcart.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.rndtechnosoft.foodcart.Adapter.MenuDetailAdapter;
import com.rndtechnosoft.foodcart.Helper.DBHelper;
import com.rndtechnosoft.foodcart.Item.MenuDetailList;
import com.rndtechnosoft.foodcart.R;
import com.rndtechnosoft.foodcart.Util.Constant_Api;
import com.rndtechnosoft.foodcart.Util.Method;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MenuDetailActivity extends AppCompatActivity {

    private Activity activity;
    private MenuDetailAdapter galleryAdapter;
    private RecyclerView recyclerView;
    private AVLoadingIndicatorView progressBar;
    private List<MenuDetailList> galleryLists;
    private String selectedId,title;
    private int selectPosition;
    private TextView textView;
    public Toolbar toolbar;
    private DBHelper dbhelper;
    TextView tvNoData;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
        galleryLists.clear();

        if (Method.isNetworkAvailable(this)) {
            progressBar.show();
            progressBar.setVisibility(View.VISIBLE);
            Gallery();
        } else {
            Toast.makeText(this, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            progressBar.hide();
        }
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu
        getMenuInflater().inflate(R.menu.menu_addcart, menu);

        // Get the notifications MenuItem and LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.cart);
        LayerDrawable icon = (LayerDrawable) item.getIcon();

        try {
            dbhelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        // Update LayerDrawable's BadgeDrawable
        Method.setBadgeCount(this, icon, dbhelper.getAllData().size());


        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                this.finish();
                return true;

            case R.id.cart:
                startActivity(new Intent(MenuDetailActivity.this, ActivityCart.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);

        galleryLists = new ArrayList<>();
        activity=MenuDetailActivity.this;

        dbhelper = new DBHelper(this);


        Intent in = getIntent();
        selectedId = in.getStringExtra("id");
        title = in.getStringExtra("title");
        selectPosition = in.getIntExtra("position", 0);

        toolbar = findViewById(R.id.toolbar_gallery_detail);
        progressBar =  findViewById(R.id.progresbar_gallery_fragment);
        toolbar.setTitleTextColor(getResources().getColor(R.color.finestBlack));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.left_arrow));
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvNoData = findViewById(R.id.tvNoData);

        recyclerView =  findViewById(R.id.recyclerView_gallery_fragment);
        progressBar =  findViewById(R.id.progresbar_gallery_fragment);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
    }


    public void Gallery() {

        progressBar.show();
        String url = Constant_Api.menu_detail + selectedId;
        AsyncHttpClient client = new AsyncHttpClient(true,80,443);client.addHeader("App-Id",Constant_Api.HOTEL_ID);
        client.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray("SINGLE_HOTEL_APP");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("id");
                        String cid = object.getString("cat_id");
                        String name = object.getString("name");
                        String desc = object.getString("des");
                        String price = object.getString("price");
                        String min = object.getString("min_kg");
                        String max = object.getString("max_kg");
                        String food_type = object.getString("food_type_icon");
                        String cat_type = object.getString("cat_type");
                        String image = object.getString("wallpaper_image");
                        String image_thumb = object.getString("wallpaper_image_thumb");
                        String start_time = object.getString("food_opening_time");
                        String end_time = object.getString("food_closing_time");
                        String food_cat_type = object.getString("cat_food_type");
                        String food_time_msg = object.getString("food_time_msg");
                        ArrayList<String> flavour_id = new ArrayList<>();
                        ArrayList<String> flavour_name = new ArrayList<>();
                        if(!cat_type.equalsIgnoreCase("instock")) {
                            JSONArray jsonArray1 = object.getJSONArray("flavour_list");


                            for (int j = 0; j < jsonArray1.length(); j++) {
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                String fid = jsonObject1.getString("f_id");
                                String fname = jsonObject1.getString("flavour_name");
                                flavour_id.add(fid);
                                flavour_name.add(fname);
                            }
                        }
                        galleryLists.add(new MenuDetailList(id,cid,name,desc,price,min,max, food_type, cat_type, image, image_thumb,start_time,end_time, food_cat_type,flavour_id,flavour_name,food_time_msg));
                    }

                    galleryAdapter = new MenuDetailAdapter(activity, galleryLists);
                    if (galleryLists==null || galleryLists.size()==0) {
                        tvNoData.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }else {
                        tvNoData.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(galleryAdapter);
                    }
                    progressBar.hide();

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.hide();
                    tvNoData.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBar.hide();
            }
        });
    }
}
