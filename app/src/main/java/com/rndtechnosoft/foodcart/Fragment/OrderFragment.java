package com.rndtechnosoft.foodcart.Fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.rndtechnosoft.foodcart.Adapter.MyOrdersAdapter;
import com.rndtechnosoft.foodcart.Item.MyOrderList;
import com.rndtechnosoft.foodcart.R;
import com.rndtechnosoft.foodcart.Util.Constant_Api;
import com.rndtechnosoft.foodcart.Util.Method;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {

    private Activity activity;
    private MyOrdersAdapter galleryAdapter;
    private RecyclerView recyclerView;
    private AVLoadingIndicatorView progressBar;
    private List<MyOrderList> galleryLists;
    private String selectedId,title;
    private int selectPosition;
    private TextView textView;
    public Toolbar toolbar;
    SwipeRefreshLayout pullToRefresh;

    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_order, container, false);
        galleryLists = new ArrayList<>();
        activity=getActivity();

        pullToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh);
        //toolbar = view.findViewById(R.id.toolbar_gallery_detail);
        progressBar =  view.findViewById(R.id.progresbar_gallery_fragment);
        /*toolbar.setTitle("My Orders");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        Log.e("ordercreate","onCreate()");
        textView = view.findViewById(R.id.txtNoData);
        recyclerView =  view.findViewById(R.id.recyclerView_gallery_fragment);
        progressBar =  view.findViewById(R.id.progresbar_gallery_fragment);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onResume();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        galleryLists.clear();
        Log.e("orderresume","onResume()");
        if (Method.isNetworkAvailable(getActivity())) {
            Gallery();
        } else {
            pullToRefresh.setRefreshing(false);
            Toast.makeText(getActivity(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            progressBar.hide();
        }
    }

    public void Gallery() {


        if (galleryAdapter!=null)
            galleryAdapter.notifyDataSetChanged();
        progressBar.show();
        textView.setVisibility(View.GONE);
        String url = Constant_Api.my_orders + Method.getAndroidID(activity);

        AsyncHttpClient client = new AsyncHttpClient(true,80,443);client.addHeader("App-Id",Constant_Api.HOTEL_ID);

        client.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);

                try {
                    //if (galleryLists.size()==0 || galleryAdapter==null || (galleryAdapter.menuLists==null || galleryAdapter.menuLists.size()==0)) {
                        JSONObject jsonObject = new JSONObject(res);

                        JSONArray jsonArray = jsonObject.getJSONArray("SINGLE_HOTEL_APP");
                    galleryLists.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);
                            String id = object.getString("ID");
                            String mobileid = object.getString("mobileid");
                            String name = object.getString("name");
                            String address = object.getString("address");
                            String phone = object.getString("phone");
                            String order_list = object.getString("order_list");
                            String datetime = object.getString("date_time");
                            String status = object.getString("status");
                            String comment = object.getString("comment");
                            String email = object.getString("email");
                            String order_type = object.getString("order_type");
                            String del_msg = object.getString("delmsg");
                            String del_time = object.getString("deltime");
                            String del_done = object.getString("deldone");
                            String cat_type = object.getString("cat_type");
                            String adtime = object.getString("adtime");
                            String addate = object.getString("addate");
                            String admsg = object.getString("admsg");
                            galleryLists.add(new MyOrderList(id, mobileid, name, address, phone, order_list, datetime, status, comment, email, order_type, del_msg, del_time,del_done,cat_type,adtime,addate,admsg));
                        }

                        Log.e("Orderlistsize", String.valueOf(galleryLists.size()) + res);

                        galleryAdapter = new MyOrdersAdapter(activity, galleryLists);
                        if (galleryLists == null || galleryLists.size() == 0) {
                            textView.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            textView.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(galleryAdapter);
                            galleryAdapter.notifyDataSetChanged();
                        }
                        pullToRefresh.setRefreshing(false);
                        progressBar.hide();
                    /*}else{*/
                        Log.e("size", String.valueOf(galleryAdapter.menuLists.size()));
                    //}

                } catch (Exception e) {
                    Log.e("orderexception", String.valueOf(e));
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBar.hide();
            }
        });
    }

}
