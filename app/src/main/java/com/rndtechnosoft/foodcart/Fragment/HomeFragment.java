package com.rndtechnosoft.foodcart.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.rndtechnosoft.foodcart.Activity.MenuDetailActivity;
import com.rndtechnosoft.foodcart.Adapter.MenuAdapter;
import com.rndtechnosoft.foodcart.Adapter.MyAdapter;
import com.rndtechnosoft.foodcart.Adapter.SingleMenuAdapter;
import com.rndtechnosoft.foodcart.Item.HomeList;
import com.rndtechnosoft.foodcart.Item.HomeListImage;
import com.rndtechnosoft.foodcart.Item.MenuDetailList;
import com.rndtechnosoft.foodcart.Item.MenuList;
import com.rndtechnosoft.foodcart.R;
import com.rndtechnosoft.foodcart.Util.Constant_Api;
import com.rndtechnosoft.foodcart.Util.Method;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class HomeFragment extends Fragment {

    private ProgressBar progressBar;
    AVLoadingIndicatorView avprogress;
    private SliderLayout mDemoSlider;
    private List<HomeList> homeLists;
    private List<HomeListImage> homeListImages;
    private Method method;
    //private RecyclerView recyclerView;
    private List<MenuList> galleryLists;
    private MenuAdapter galleryAdapter;
    private GridLayoutManager layoutManager;
    TabLayout tabLayout;
    ViewPager viewPager;
    TextView txttype,nodata;
    RecyclerView recyclerView;
    Button button_cat_type;
    RelativeLayout relativeLayout_detail_home_fragment;
    String cat_id="",cname="";
    private List<MenuDetailList> singleLists;
    private SingleMenuAdapter singleAdapter;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_on_demand, container, false);

        method = new Method(getActivity());

        homeLists = new ArrayList<>();
        homeListImages = new ArrayList<>();
        galleryLists = new ArrayList<>();

        tabLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.viewPager);
        //pullToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh);
        txttype=view.findViewById(R.id.txttype);
        nodata=view.findViewById(R.id.nodata);
        recyclerView=view.findViewById(R.id.rv_home_landscape);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);

        relativeLayout_detail_home_fragment=view.findViewById(R.id.relativeLayout_detail_home_fragment);
        button_cat_type=view.findViewById(R.id.button_cat_type);
        avprogress=view.findViewById(R.id.progresbar_single);
        //progressBar = (AVLoadingIndicatorView) view.findViewById(R.id.progresbar_home);
        mDemoSlider = (SliderLayout) view.findViewById(R.id.slider_home_fragment);

        /*pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onResume();
            }
        });*/

        progressBar = view.findViewById(R.id.progressBarHome);
        tabLayout.addTab(tabLayout.newTab().setText("In Stock"));
        tabLayout.addTab(tabLayout.newTab().setText("Advance Booking"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        button_cat_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MenuDetailActivity.class).putExtra("id",cat_id).putExtra("title",cname);
                startActivity(intent);
            }
        });

        final MyAdapter adapter = new MyAdapter(getActivity(),getChildFragmentManager(), 2);


        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        if (Method.isNetworkAvailable(getActivity())) {
            homeListImages.clear();
            homeLists.clear();
            home();

        } else {
            //pullToRefresh.setRefreshing(false);
            Toast.makeText(getActivity(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            //progressBar.setVisibility(View.GONE);
        }

        galleryAdapter = new MenuAdapter(getActivity(), galleryLists);

        return view;

    }

    @Override
    public void onResume() {
        getFastFood();
        super.onResume();

    }

    public void home() {

        progressBar.setVisibility(View.VISIBLE);

        AsyncHttpClient client = new AsyncHttpClient(true,80,443);client.addHeader("App-Id",Constant_Api.HOTEL_ID);
        client.get(Constant_Api.home, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);

                String id = null;
                String banner_image = null;
                String hotel_name = null;
                String hotel_address = null;

                try {
                    JSONObject jsonObject = new JSONObject(res);
                    homeListImages.clear();
                    homeLists.clear();
                    JSONObject Jobject = jsonObject.getJSONObject("SINGLE_HOTEL_APP");
                    {

                        JSONArray jsonArray = Jobject.getJSONArray("home_banner");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);
                            id = object.getString("id");
                            banner_image = object.getString("banner_image");

                            homeListImages.add(new HomeListImage(id,banner_image));

                        }

                        JSONArray jsonArray_hoteInfo = Jobject.getJSONArray("hotel_info");

                        for (int j = 0; j < jsonArray_hoteInfo.length(); j++) {

                            JSONObject object = jsonArray_hoteInfo.getJSONObject(j);
                            hotel_name = object.getString("hotel_name");
                            hotel_address = object.getString("hotel_address");

                        }

                        homeLists.add(new HomeList(id, banner_image, hotel_name, hotel_address));
                    }

                    progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Log.e("bannersize", String.valueOf(homeListImages.size()));
                for (int i = 0; i < homeListImages.size(); i++) {
                    DefaultSliderView defaultSliderView = new DefaultSliderView(getActivity());
                    // initialize a SliderLayout
                    defaultSliderView
                            .image(homeListImages.get(i).getBanner_image())
                            .setScaleType(BaseSliderView.ScaleType.Fit);
                    mDemoSlider.addSlider(defaultSliderView);
                }

              //  mDemoSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
                mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                mDemoSlider.setCustomAnimation(new DescriptionAnimation());
              //  mDemoSlider.setDuration(4000);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void getFastFood() {

        avprogress.show();
        String url = Constant_Api.fast_food;
        AsyncHttpClient client = new AsyncHttpClient(true,80,443);client.addHeader("App-Id",Constant_Api.HOTEL_ID);
        client.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                singleLists =new ArrayList<>();
                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray("MENU_CATEGORY");

                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        cname = object.getString("category_name");
                        cat_id= object.getString("cid");
                        txttype.setText(cname);

                    }

                    JSONArray jsonArray1 = jsonObject.getJSONArray("FOOD_LIST");

                    for (int i = 0; i < jsonArray1.length(); i++) {

                        JSONObject object = jsonArray1.getJSONObject(i);
                        String id = object.getString("id");
                        String cid = object.getString("cat_id");
                        String name = object.getString("name");
                        String desc = object.getString("des");
                        String price = object.getString("price");
                        String food_type = object.getString("food_type_icon");
                        String cat_type = object.getString("cat_type");
                        String image = object.getString("wallpaper_image");
                        String image_thumb = object.getString("wallpaper_image_thumb");
                        String start_time = object.getString("food_opening_time");
                        String end_time = object.getString("food_closing_time");
                        String food_cat_type = object.getString("cat_food_type");
                        String food_time_msg = object.getString("food_time_msg");

                        singleLists.add(new MenuDetailList(id,cid,name,desc,price,"","", food_type, cat_type, image, image_thumb,start_time,end_time, food_cat_type, new ArrayList<String>(), new ArrayList<String>(),food_time_msg));
                    }

                    singleAdapter = new SingleMenuAdapter(getActivity(), singleLists);
                    if (singleLists==null || singleLists.size()==0) {
                        nodata.setVisibility(View.VISIBLE);
                        relativeLayout_detail_home_fragment.setVisibility(View.GONE);
                        button_cat_type.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                    }else {
                        nodata.setVisibility(View.GONE);
                        relativeLayout_detail_home_fragment.setVisibility(View.VISIBLE);
                        button_cat_type.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.setAdapter(singleAdapter);
                        singleAdapter.notifyDataSetChanged();
                    }
                    //pullToRefresh.setRefreshing(false);
                    avprogress.hide();

                } catch (JSONException e) {
                    e.printStackTrace();
                    avprogress.hide();
                    nodata.setVisibility(View.VISIBLE);
                    relativeLayout_detail_home_fragment.setVisibility(View.GONE);
                    button_cat_type.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                avprogress.hide();
            }
        });
    }
}
