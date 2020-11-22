package com.rndtechnosoft.foodcart.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rndtechnosoft.foodcart.Adapter.ScratchCardTypeAdapter;
import com.rndtechnosoft.foodcart.Item.ScratchList;
import com.rndtechnosoft.foodcart.Item.ScratchModel;
import com.rndtechnosoft.foodcart.R;
import com.rndtechnosoft.foodcart.Util.Constant_Api;
import com.rndtechnosoft.foodcart.Util.SharedPref;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ScratchCardActivity extends AppCompatActivity {

    private AVLoadingIndicatorView scratchProgress;
    ScratchCardTypeAdapter adapter;
    private ArrayList<ScratchList> scratchLists;
    ArrayList<ScratchModel> randomModelList;
    private RecyclerView mRecyclerView;
    ImageView back,rewardimage;
    TextView amount,rewardText;
    private String masterimage;
    private String rewardamount;
    private String rewardtext;
    RelativeLayout rewardlist,recycle_relative;
    TextView nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch_card);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorAccentLight));
        }*/

        back = findViewById(R.id.back);
        rewardimage = findViewById(R.id.rewardImage);
        amount = findViewById(R.id.amount);
        rewardText = findViewById(R.id.rewardText);
        rewardlist = findViewById(R.id.rewardlist);
        recycle_relative = findViewById(R.id.recycle_relative);
        nodata = findViewById(R.id.nodata);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        scratchProgress = findViewById(R.id.scratchProgress);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_scratch);
        mRecyclerView.setLayoutManager(new GridLayoutManager(ScratchCardActivity.this,2));
    }

    @Override
    protected void onResume() {
        getCouponList();
        super.onResume();
    }

    public void getCouponList() {
        if (adapter!=null)
            adapter=null;
        scratchProgress.show();
        nodata.setVisibility(View.GONE);

        AsyncHttpClient client = new AsyncHttpClient(true,80,443);
        client.addHeader("userid", SharedPref.getUserId(ScratchCardActivity.this));

        client.get(Constant_Api.scratchcoupon, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);
                randomModelList = new ArrayList<>();
                scratchLists = new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray1 = jsonObject.getJSONArray("MASTER_SCRATCH");
                    for (int j=0;j<jsonArray1.length();j++){
                        JSONObject object = jsonArray1.getJSONObject(j);
                        masterimage = object.getString("master_image");
                        rewardamount = object.getString("master_amount");
                        rewardtext = object.getString("master_subtitle");
                    }

                    JSONArray jsonArray = jsonObject.getJSONArray("SCRATCH_LIST");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("id");
                        String title = object.getString("title");
                        String message = object.getString("message");
                        String sorrymsg = object.getString("sorry_msg");
                        String congratsmsg = object.getString("congrats_msg");
                        String amount = object.getString("amount");
                        String type = object.getString("type");
                        String coupon_text = object.getString("coupon_text");
                        String image = object.getString("image");
                        String scimage = object.getString("scimage");
                        String status = object.getString("status");


                        scratchLists.add(new ScratchList(id,title,message,sorrymsg,congratsmsg,amount,type,coupon_text,image,scimage,status));
                        int type_model;
                        if (scratchLists.get(i).getType().equalsIgnoreCase("text"))
                            type_model=ScratchModel.TEXT_TYPE;
                        else
                            type_model=ScratchModel.IMAGE_TYPE;
                        ScratchModel singleVideo = new ScratchModel(type_model, scratchLists.get(i).getImage(), 0);
                        singleVideo.setScratch(scratchLists.get(i));
                        randomModelList.add(singleVideo);
                    }

                    try {
                        Picasso.with(ScratchCardActivity.this).load(masterimage).into(rewardimage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    amount.setText(rewardamount);
                    rewardText.setText(rewardtext);
                    rewardlist.setVisibility(View.VISIBLE);

                    if (scratchLists != null && scratchLists.size() != 0) {
//                        ScratchModel randomData = new ScratchModel();
//                        randomData.setScratchLists(scratchLists);
//                        for (int i=0; i<scratchLists.size();i++) {
//                            if (scratchLists.get(i).getType().equalsIgnoreCase("text"))
//                                randomData.setType(ScratchModel.TEXT_TYPE);
//                            else
//                                randomData.setType(ScratchModel.IMAGE_TYPE);
//                        }
//                        randomModelList.add(randomData);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        recycle_relative.setVisibility(View.VISIBLE);
                        nodata.setVisibility(View.GONE);

                        if (adapter == null)
                            adapter = new ScratchCardTypeAdapter(randomModelList, ScratchCardActivity.this);

                        mRecyclerView.setAdapter(adapter);
                    }else {
                        mRecyclerView.setVisibility(View.GONE);
                        recycle_relative.setVisibility(View.GONE);
                        rewardlist.setVisibility(View.VISIBLE);
                        nodata.setVisibility(View.VISIBLE);
                    }



                    scratchProgress.hide();
                } catch (Exception e) {
                    e.printStackTrace();
                    scratchProgress.hide();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                scratchProgress.hide();
            }
        });
    }
}
