package com.rndtechnosoft.foodcart.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rndtechnosoft.foodcart.R;
import com.rndtechnosoft.foodcart.Util.Constant_Api;
import com.rndtechnosoft.foodcart.Util.Method;

public class AboutUs extends AppCompatActivity {

    public Toolbar toolbar;
    private TextView textView_app_name, textView_app_version, textView_app_author, textView_app_contact, textView_app_email, textView_app_website, textView_app_description,
            textView_titleVerson, textView_titleAuthore, textView_titleCompany, textView_titleEmail, textView_titleWebside, textView_titleAbout;

    private ImageView app_logo;
    private Method method;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        Method.forceRTLIfSupported(getWindow(), AboutUs.this);

        method = new Method(AboutUs.this);

        toolbar = (Toolbar) findViewById(R.id.toolbar_about_us);
        toolbar.setTitleTextColor(getResources().getColor(R.color.finestBlack));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.left_arrow));
        toolbar.setTitle(getResources().getString(R.string.about_us));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textView_app_name = (TextView) findViewById(R.id.textView_app_name_about_us);
        textView_app_version = (TextView) findViewById(R.id.textView_app_version_about_us);
        textView_app_author = (TextView) findViewById(R.id.textView_app_author_about_us);
        textView_app_contact = (TextView) findViewById(R.id.textView_app_contact_about_us);
        textView_app_email = (TextView) findViewById(R.id.textView_app_email_about_us);
        textView_app_website = (TextView) findViewById(R.id.textView_app_website_about_us);
        textView_app_description = (TextView) findViewById(R.id.textView_app_description_about_us);


        textView_titleVerson = (TextView) findViewById(R.id.textView_TitleVersion_about_us);
        textView_titleAuthore = (TextView) findViewById(R.id.textView_TitleAuthore_about_us);
        textView_titleAbout = (TextView) findViewById(R.id.textView_TitleAbout_about_us);
        textView_titleCompany = (TextView) findViewById(R.id.textView_TitleCompany_about_us);
        textView_titleEmail = (TextView) findViewById(R.id.textView_TitleEmail_about_us);
        textView_titleWebside = (TextView) findViewById(R.id.textView_TitleWebsite_about_us);
        textView_titleAbout = (TextView) findViewById(R.id.textView_TitleContact_about_us);
        
        app_logo = (ImageView) findViewById(R.id.app_logo_about_us);

        textView_app_name.setText(Constant_Api.aboutUs.getApp_name());
        Glide.with(getApplication()).load(Constant_Api.image + Constant_Api.aboutUs.getApp_logo())
                .into(app_logo);

        textView_app_version.setText(Constant_Api.aboutUs.getApp_version());
        textView_app_author.setText(Constant_Api.aboutUs.getApp_author());
        textView_app_contact.setText(Constant_Api.aboutUs.getApp_contact());
        textView_app_email.setText(Constant_Api.aboutUs.getApp_email());
        textView_app_website.setText(Constant_Api.aboutUs.getApp_website());
        textView_app_description.setText(Html.fromHtml(Constant_Api.aboutUs.getApp_description()));

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
