package com.rndtechnosoft.foodcart.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rndtechnosoft.foodcart.BuildConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rndtechnosoft.foodcart.Adapter.ViewPagerAdapter;
import com.rndtechnosoft.foodcart.Fragment.HomeFragment;
import com.rndtechnosoft.foodcart.Fragment.OrderFragment;
import com.rndtechnosoft.foodcart.Fragment.ProfileFragment;
import com.rndtechnosoft.foodcart.Helper.DBHelper;
import com.rndtechnosoft.foodcart.Item.AboutUsList;
import com.rndtechnosoft.foodcart.R;
import com.rndtechnosoft.foodcart.Util.Constant_Api;
import com.rndtechnosoft.foodcart.Util.Constants;
import com.rndtechnosoft.foodcart.Util.Method;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.rndtechnosoft.foodcart.Util.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    public static Toolbar toolbar;
    private TextView textView_appDevlopBy;
    boolean doubleBackToExitPressedOnce = false;
    final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;
    static DBHelper dbhelper;
    public ViewPager viewPager;
    public ViewPagerAdapter adapter;
    public static BottomNavigationView bottomNavigationMenu;
    MenuItem prevMenuItem;
    ArrayList<String> permissions = new ArrayList<>();
    ArrayList<String> permissionsToRequest;
    public final static int ALL_PERMISSIONS_RESULT = 102;
    ArrayList<String> permissionsRejected = new ArrayList<>();
    boolean isGPS = false;
    boolean isNetwork = false;
    boolean canGetLocation = true;
    //private GPSTracker gps;
    private String location;
    SharedPref sharedPref;
    String type="";

    private FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    private HashMap<String, Object> firebaseDefaultMap;
    public static final String VERSION_CODE_KEY = "latest_app_version";
    public static final String TITLE = "title";
    public static final String FORCE = "force";
    public static final String MESSAGE = "msg";
    public static final String UPDATE = "update";
    private static final String TAG = "MainActivity";
    private boolean isforced;
    String version;

    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu
        getMenuInflater().inflate(R.menu.menu_addcart, menu);

        // Get the notifications MenuItem and LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.cart);
        LayerDrawable icon = (LayerDrawable) item.getIcon();

        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                //code to do the HTTP request
                Method.sendRegistrationToServer(MainActivity.this,FirebaseInstanceId.getInstance().getToken(),Method.getAndroidID(MainActivity.this));
            }
        });
        thread.start();

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
                startActivity(new Intent(MainActivity.this, ActivityCart.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Method.forceRTLIfSupported(getWindow(), MainActivity.this);

        textView_appDevlopBy = (TextView) findViewById(R.id.textView_app_developed_by);

        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setTitleTextColor(getResources().getColor(R.color.finestBlack));
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.home));

        dbhelper = new DBHelper(this);

        bottomNavigationMenu = findViewById(R.id.navigation);
        bottomNavigationMenu .setItemIconTintList(null);
        bottomNavigationMenu.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPager=findViewById(R.id.framlayout_main);

        if (getIntent().hasExtra(Constants.type)) {
            if (getIntent().getStringExtra(Constants.type).equals(Constants.order))
                type = Constants.order;
            if (getIntent().getStringExtra(Constants.type).equals(Constants.normal))
                type = Constants.normal;
        }

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
            Log.e("versioncode",version);
            new updatepro().execute();
            //setVersionCode(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        // create database
        try {
            dbhelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        // then, the database will be open to use
        try {
            dbhelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }


        // if user has already ordered food previously then show activity_confirm dialog
        /*if (dbhelper.isPreviousDataExist()) {
            showAlertDialog();
        }*/
        setupViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            int currentPosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationMenu.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                bottomNavigationMenu.getMenu().getItem(position).setChecked(true);

                /*FragmentLifecycle fragmentToShow = (FragmentLifecycle)adapter.getItem(position);
                fragmentToShow.onResumeFragment();

                FragmentLifecycle fragmentToHide = (FragmentLifecycle)adapter.getItem(currentPosition);
                fragmentToHide.onPauseFragment();*/

                currentPosition = position;



                prevMenuItem = bottomNavigationMenu.getMenu().getItem(position);

                switch (position) {
                    case 0:
                        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.home));
                        break;
                    case 1:
                        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.myorders));
                        break;
                    case 2:
                        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.profile));
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationIcon(R.drawable.ic_side_nav);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (Method.isNetworkAvailable(MainActivity.this)) {
            loadAppDetail();
        } else {
            Toast.makeText(MainActivity.this, getResources().getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();
        }

        if (type.equals("order")) {
            viewPager.setCurrentItem(1);
            getIntent().removeExtra(Constants.type);
        }
        else if(type.equals("Normal")) {
            viewPager.setCurrentItem(0);
            getIntent().removeExtra(Constants.type);
        }
        else {
            HomeFragment homeFragment = new HomeFragment();
            FragmentTransaction homeFragment_ft = getSupportFragmentManager().beginTransaction();
            homeFragment_ft.replace(R.id.framlayout_main, homeFragment);
            homeFragment_ft.commit();
            Method.onBackPress = true;
        }

        checkPer();

        firebaseDefaultMap = new HashMap<>();
        firebaseDefaultMap.put(VERSION_CODE_KEY, getCurrentVersionCode());
        mFirebaseRemoteConfig.setDefaults(firebaseDefaultMap);

        //Setting that default Map to Firebase Remote Config

        //Setting Developer Mode enabled to fast retrieve the values
        mFirebaseRemoteConfig.setConfigSettings(
                new FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(BuildConfig.DEBUG)
                        .build());

        //Fetching the values here
        mFirebaseRemoteConfig.fetch().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mFirebaseRemoteConfig.activateFetched();
                    Log.d(TAG, "Fetched value: " + mFirebaseRemoteConfig.getString(VERSION_CODE_KEY));
                    Log.d(TAG, "Fetched value: " + mFirebaseRemoteConfig.getString(TITLE));
                    Log.d(TAG, "Fetched value: " + mFirebaseRemoteConfig.getString(MESSAGE));
                    Log.d(TAG, "Fetched value: " + mFirebaseRemoteConfig.getString(FORCE));
                    //calling function to check if new version is available or not
                    checkForUpdate();
                } else {
                    Toast.makeText(MainActivity.this, "Something went wrong please try again",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.d(TAG, "Default value: " + mFirebaseRemoteConfig.getString(VERSION_CODE_KEY));


        /*OneSignal.startInit(this)
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
                .init();*/

    }

    public class updatepro extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            //dialog = ProgressDialog.show(MainActivity.this, "", getString(R.string.sending_alert), true);

        }

        @Override
        protected Void doInBackground(Void... params) {
            if (version!=null && !version.equals(""))
                setVersionCode(version);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //dialog.dismiss();
            //resultAlert();
            //onBackPressed();
        }
    }

    private void setVersionCode(String version) {
        HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(Constant_Api.update_version);
        try {
            List<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>(15);
            nameValuePairs1.add(new BasicNameValuePair("versioncode", version));
            nameValuePairs1.add(new BasicNameValuePair("user_id", SharedPref.getUserId(this)));
            request.setEntity(new UrlEncodedFormEntity(nameValuePairs1, HTTP.UTF_8));
            HttpResponse response = client.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            Log.e("versionupdate",responseBody);
            //result = responseBody;
        } catch (Exception ex) {
            Log.d("versionex",ex.toString());
            //result = "Unable to connect.";
        }
    }


    private void checkForUpdate() {
        int latestAppVersion = (int) mFirebaseRemoteConfig.getDouble(VERSION_CODE_KEY);

        if (latestAppVersion > getCurrentVersionCode()) {
            if(mFirebaseRemoteConfig.getString(FORCE).equalsIgnoreCase("true")){
                new AlertDialog.Builder(this).setTitle(mFirebaseRemoteConfig.getString(TITLE))
                        .setMessage(mFirebaseRemoteConfig.getString(MESSAGE)).setPositiveButton(
                        mFirebaseRemoteConfig.getString(UPDATE), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.rndtechnosoft.foodcart"));
                                startActivity(intent);
                            }
                        }).setCancelable(false).show();
            }else{
                new AlertDialog.Builder(this).setTitle(mFirebaseRemoteConfig.getString(TITLE))
                        .setMessage(mFirebaseRemoteConfig.getString(MESSAGE)).setPositiveButton(
                        mFirebaseRemoteConfig.getString(UPDATE), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.rndtechnosoft.foodcart"));
                                startActivity(intent);
                            }
                        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            }



        }
    }

    private int getCurrentVersionCode() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_shop:
                    viewPager.setCurrentItem(0);
                    try {
                        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.home));
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    }
                    return true;
                case R.id.action_orders:
                    viewPager.setCurrentItem(1);
                    try {
                        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.myorders));
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    }
                    return true;
                case R.id.action_profile:
                    viewPager.setCurrentItem(2);
                    try {
                        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.profile));
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    }
                    return true;
                default:
                    viewPager.setCurrentItem(0);
                    try {
                        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.home));
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    }
            }
            return false;
        }
    };


    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        HomeFragment fragmentHome = new HomeFragment();
        ProfileFragment fragmentProfile = new ProfileFragment();
        OrderFragment fragmentGallery = new OrderFragment();
        adapter.addFragment(fragmentHome);
        adapter.addFragment(fragmentGallery);
        adapter.addFragment(fragmentProfile);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
    }


    // show activity_confirm dialog to ask user to delete previous order or not
    public void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm);
        builder.setMessage(getString(R.string.db_exist_alert));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.option_yes), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dbhelper.deleteAllData();
                invalidateOptionsMenu();
                dbhelper.close();
            }
        });

        builder.setNegativeButton(getString(R.string.option_no), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dbhelper.close();
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
            }
            if (Method.onBackPress) {
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, getResources().getString(R.string.Please_click_BACK_again_to_exit), Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            } else {
                HomeFragment homeFragment = new HomeFragment();
                FragmentTransaction homeFragment_ft = getSupportFragmentManager().beginTransaction();
                homeFragment_ft.replace(R.id.framlayout_main, homeFragment);
                homeFragment_ft.commit();
                toolbar.setTitle(getResources().getString(R.string.home));
                Method.onBackPress = true;
            }

        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //Checking if the item is in checked state or not, if not make it in checked state
        if (item.isChecked())
            item.setChecked(false);
        else
            item.setChecked(true);

        //Closing drawer on item click
        drawer.closeDrawers();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {

            case R.id.home:
                viewPager.setCurrentItem(0);
                Method.onBackPress = true;
                return true;

            case R.id.profile:
                viewPager.setCurrentItem(2);
                Method.onBackPress = true;
                return true;

            case R.id.myorder:
                viewPager.setCurrentItem(1);
                return true;

            case R.id.contact_us:
                startActivity(new Intent(MainActivity.this, ContactActivity.class));
                return true;


//            case R.id.rate_app:
//                Uri uri = Uri.parse("market://details?id=" + getApplication().getPackageName());
//                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
//                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
//                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//                try {
//                    startActivity(goToMarket);
//                } catch (ActivityNotFoundException e) {
//                    startActivity(new Intent(Intent.ACTION_VIEW,
//                            Uri.parse("http://play.google.com/store/apps/details?id=" + getApplication().getPackageName())));
//                }
//                return true;

            case R.id.share_app:
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String sAux = "\n" + getResources().getString(R.string.Let_me_recommend_you_this_application) + "\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=" + getApplication().getPackageName();
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                }
                return true;

            case R.id.about:
                startActivity(new Intent(MainActivity.this, AboutUs.class));
                return true;

            case R.id.privacy_policy:
                startActivity(new Intent(MainActivity.this, PrivacyPolice.class));
                return true;
            case R.id.logout:

                SharedPreferences.Editor editor = getSharedPreferences(Constant_Api.MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString(Constants.MOBILE_NO, "");
                editor.putString(Constants.NAME, "");
                editor.apply();
                SharedPref.clearAllPreferences(MainActivity.this);
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();


                return true;
            default:
                return true;
        }

    }

    public void checkPer() {
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.CAMERA);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);

            // check permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionsToRequest.size() > 0) {
                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                            ALL_PERMISSIONS_RESULT);
                    //Log.d(TAG, "Permission requests");
                    canGetLocation = false;
                }
            }
    }

    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForUpdate();
        invalidateOptionsMenu();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        boolean canUseExternalStorage = false;

        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                try {
                    //Log.d(TAG, "onRequestPermissionsResult");
                    for (String perms : permissionsToRequest) {
                        if (!hasPermission(perms)) {
                            permissionsRejected.add(perms);
                        }
                    }

                    if (permissionsRejected.size() > 0) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                                showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(permissionsRejected.toArray(
                                                            new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    } else {
                        //Log.d(TAG, "No rejected permissions.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                    //openPage();
                }
                break;
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    canUseExternalStorage = true;
                }

                if (!canUseExternalStorage) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.cannot_use_save_permission), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("cancel", null)
                .create()
                .show();
    }

    public void loadAppDetail() {

        AsyncHttpClient client = new AsyncHttpClient(true,80,443);client.addHeader("App-Id",Constant_Api.HOTEL_ID);

        client.get(Constant_Api.app_detail, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray("SINGLE_HOTEL_APP");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String app_name = object.getString("app_name");
                        String app_logo = object.getString("app_logo");
                        String app_version = object.getString("app_version");
                        String app_author = object.getString("app_author");
                        String app_contact = object.getString("app_contact");
                        String app_email = object.getString("app_email");
                        String app_website = object.getString("app_website");
                        String app_description = object.getString("app_description");
                        String app_developed_by = object.getString("app_developed_by");
                        String app_privacy_policy = object.getString("app_privacy_policy");
                        Constant_Api.aboutUs = new AboutUsList(app_name, app_logo, app_version, app_author, app_contact, app_email, app_website, app_description, app_developed_by, app_privacy_policy);
                        textView_appDevlopBy.setText(Constant_Api.aboutUs.getApp_developed_by());
                        textView_appDevlopBy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Uri uri = Uri.parse("https://www.rndtechnosoft.com");
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Toast.makeText(MainActivity.this,"error",Toast.LENGTH_LONG).show();
            }
        });

    }

}
