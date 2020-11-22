package com.rndtechnosoft.foodcart.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.rndtechnosoft.foodcart.Adapter.MenuAdapter;
import com.rndtechnosoft.foodcart.Item.MenuList;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class StockFragment extends Fragment {

    private AVLoadingIndicatorView progressBar;
    private List<MenuList> menuLists;
    private MenuAdapter menuAdapter;
    RecyclerView recyclerView;
    private GridLayoutManager layoutManager;

    public StockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_stock, container, false);
        progressBar = (AVLoadingIndicatorView) view.findViewById(R.id.progresbar_stock);
        recyclerView = view.findViewById(R.id.recyclerView_stock);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);
        menuLists = new ArrayList<>();
        if (Method.isNetworkAvailable(getActivity())) {
            progressBar.show();
            Category();
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            progressBar.hide();
        }
        return view;
    }

    public void Category() {
        //progressBar.setVisibility(View.VISIBLE);
        progressBar.show();

        AsyncHttpClient client = new AsyncHttpClient(true,80,443);client.addHeader("App-Id", Constant_Api.HOTEL_ID);
        client.get(Constant_Api.menu, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray("INSTOCK_CATEGORY_LIST");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String cid = object.getString("cid");
                        String category_name = object.getString("category_name");
                        String category_image = object.getString("category_image");
                        String cat_type = object.getString("cat_type");
                        String category_image_thumb = object.getString("category_image_thumb");

                        menuLists.add(new MenuList(cid, category_name, category_image, cat_type, category_image_thumb));
                    }

                    menuAdapter = new MenuAdapter(getActivity(), menuLists);
                    recyclerView.setAdapter(menuAdapter);
                    progressBar.hide();
                    //progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBar.hide();
                //progressBar.setVisibility(View.GONE);
            }
        });
    }

}
