package com.rndtechnosoft.foodcart.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;

import com.rndtechnosoft.foodcart.R;
import com.rndtechnosoft.foodcart.Util.Constant_Api;
import com.rndtechnosoft.foodcart.Util.Method;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PrivacyPolice extends AppCompatActivity {

    public Toolbar toolbar;
    private TextView privacy_policy_textview;
    private Method method;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        Method.forceRTLIfSupported(getWindow(),PrivacyPolice.this);

        method=new Method(PrivacyPolice.this);

        toolbar = (Toolbar) findViewById(R.id.toolbar_privacy_policy);
        toolbar.setTitleTextColor(getResources().getColor(R.color.finestBlack));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.left_arrow));
        toolbar.setTitle(getResources().getString(R.string.privacy_policy));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        privacy_policy_textview=(TextView)findViewById(R.id.textview_privacy_policy);
        privacy_policy_textview.setText(Html.fromHtml(Constant_Api.aboutUs.getApp_privacy_policy()));

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
