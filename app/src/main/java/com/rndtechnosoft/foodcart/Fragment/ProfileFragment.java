package com.rndtechnosoft.foodcart.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rndtechnosoft.foodcart.Activity.ScratchCardActivity;
import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.rndtechnosoft.foodcart.Activity.ProfileEditActivity;
import com.rndtechnosoft.foodcart.R;
import com.rndtechnosoft.foodcart.Util.Constant_Api;
import com.rndtechnosoft.foodcart.Util.Method;
import com.rndtechnosoft.foodcart.Util.SharedPref;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    CircleImageView profile_image;
    TextView username,u_name,amount,email,mobile,location,address,gender,dob,doa;
    AVLoadingIndicatorView progressBar;
    String pid,name,email_id,plocation,pzipcode,pdoa,pdob,pgender,pmobile,pimage,pwallet,paddress;
    //Button edit_profile;
    int user_id=1;
    SwipeRefreshLayout pullToRefresh;
    LinearLayout scratchCard;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_edit,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile_edit:
                startActivity(new Intent(getActivity(), ProfileEditActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        setHasOptionsMenu(true);
        pullToRefresh = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh);
        profile_image=view.findViewById(R.id.profile_image);
        username=view.findViewById(R.id.user_name);
        u_name=view.findViewById(R.id.u_name);
        amount=view.findViewById(R.id.amount);
        email=view.findViewById(R.id.email);
        mobile=view.findViewById(R.id.mobile);
        location=view.findViewById(R.id.location);
        address=view.findViewById(R.id.address);
        gender=view.findViewById(R.id.gender);
        dob=view.findViewById(R.id.dob);
        doa=view.findViewById(R.id.doa);
        progressBar=view.findViewById(R.id.progresbar_profile);
        scratchCard=view.findViewById(R.id.scratchCard);
        //edit_profile=view.findViewById(R.id.edit_profile);
        username.setText(SharedPref.getUserName(getActivity()));
        u_name.setText("@"+SharedPref.getMobileNumber(getActivity()));
        mobile.setText(SharedPref.getMobileNumber(getActivity()));

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onResume();
            }
        });

       /* edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ProfileEditActivity.class));
            }
        });*/

       scratchCard.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(getActivity(), ScratchCardActivity.class));
           }
       });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Method.isNetworkAvailable(getActivity())) {
            getData();
        } else {
            pullToRefresh.setRefreshing(false);
            Toast.makeText(getActivity(), getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
            progressBar.hide();
        }
    }

    public void getData() {

        progressBar.show();

        AsyncHttpClient client = new AsyncHttpClient();
        /*client.addHeader("App-Id",Constant_Api.HOTEL_ID);*/
        String userid=SharedPref.getUserId(getActivity());
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
                        pid = object.getString("id");
                        name = object.getString("name");
                        email_id = object.getString("email");
                        pmobile = object.getString("mobile");
                        pgender = object.getString("gender");
                        pimage = object.getString("image");
                        pwallet = object.getString("wallet");
                        pdob = object.getString("dob");
                        pdoa = object.getString("doa");
                        paddress = object.getString("address");
                        plocation = object.getString("location");
                        pzipcode = object.getString("zipcode");

                    }

                    if (!name.equals(""))
                        username.setText(name);
                    if (!email_id.equals(""))
                        email.setText(email_id);
                    if (!pmobile.equals("")) {
                        mobile.setText(pmobile);
                        u_name.setText("@"+pmobile);
                    }
                    if (!pgender.equals(""))
                        gender.setText(pgender);
                    if (!pimage.equals(""))
                        Glide.with(getActivity()).load(pimage).into(profile_image);
                    if (!pwallet.equals(""))
                        amount.setText(pwallet);
                    if (!pdob.equals(""))
                        dob.setText(pdob);
                    if (!pdoa.equals(""))
                        doa.setText(pdoa);
                    if (!paddress.equals(""))
                        address.setText(paddress);
                    if (!plocation.equals(""))
                        location.setText(plocation);

                    pullToRefresh.setRefreshing(false);
                    progressBar.hide();

                } catch (JSONException e) {
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
