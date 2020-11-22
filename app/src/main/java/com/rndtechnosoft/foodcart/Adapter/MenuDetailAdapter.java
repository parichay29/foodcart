package com.rndtechnosoft.foodcart.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.SQLException;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rndtechnosoft.foodcart.Helper.DBHelper;
import com.rndtechnosoft.foodcart.Item.MenuDetailList;
import com.rndtechnosoft.foodcart.R;
import com.rndtechnosoft.foodcart.Util.Method;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static cz.msebera.android.httpclient.client.utils.DateUtils.parseDate;

public class MenuDetailAdapter extends RecyclerView.Adapter<MenuDetailAdapter.ViewHolder> {

    private Activity activity;
    private List<MenuDetailList> menuLists;
    private int columnWidth;
    private Method method;
    private int minteger=0;
    DBHelper dbhelper;
    String cat_type="",kg="";
    private int kilo=0;
    int min=0;
    int max=0;
    private String food_cat_type;
    String f_name,f_id= "";
    private RadioGroup rg;
    String endtime ="";
    String starttime ="";


    final String inputFormat = "HH:mm";

    Date date;
    Date dateCompareOne;
    Date dateCompareTwo;

    String compareStringOne = "9:45";
    String compareStringTwo = "11:45";

    SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.US);


    public MenuDetailAdapter(Activity activity, List<MenuDetailList> menuLists) {
        this.activity = activity;
        this.menuLists = menuLists;
        method = new Method(activity);
        Resources r = activity.getResources();
        dbhelper = new DBHelper(activity);
    }

    @Override
    public MenuDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.row_product_list, parent, false);
        return new MenuDetailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MenuDetailAdapter.ViewHolder holder, final int position) {

        starttime="";
        endtime="";
        if (!menuLists.get(position).getStart_time().equalsIgnoreCase("0") && !menuLists.get(position).getEnd_time().equalsIgnoreCase("0")) {

            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            try {
                Date _24HourStart = _24HourSDF.parse(menuLists.get(position).getStart_time());
                Date _24HourEnd = _24HourSDF.parse(menuLists.get(position).getEnd_time());
                starttime=_12HourSDF.format(_24HourStart);
                endtime=_12HourSDF.format(_24HourEnd);
                holder.tvtime.setVisibility(View.VISIBLE);
                holder.tvtime.setText(starttime+" - "+endtime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            /* try {
                starttime = LocalTime.parse(menuLists.get(position).getStart_time(), DateTimeFormatter.ofPattern("HH:mm")).format(DateTimeFormatter.ofPattern("hh:mm a"));
                endtime = LocalTime.parse(menuLists.get(position).getEnd_time(), DateTimeFormatter.ofPattern("HH:mm")).format(DateTimeFormatter.ofPattern("hh:mm a"));
                holder.tvtime.setVisibility(View.VISIBLE);
                holder.tvtime.setText(starttime+" - "+endtime);
            } catch (Exception e) {
                e.printStackTrace();
            }*/

        }else{
            holder.tvtime.setVisibility(View.GONE);
        }
        cat_type=menuLists.get(position).getCat_type();
        if (cat_type.equals("advance")){
            if ((menuLists.get(position).getMin()!=null && !menuLists.get(position).getMin().equals(""))&&(menuLists.get(position).getMax()!=null && !menuLists.get(position).getMax().equals(""))){
                min= Integer.parseInt(menuLists.get(position).getMin());
                max= Integer.parseInt(menuLists.get(position).getMax());
                holder.llkg.setVisibility(View.VISIBLE);
                Log.e("minkg", String.valueOf(min));
                Log.e("maxkg", String.valueOf(max));
                List<String> categories = new ArrayList<String>();
                for (int i=min;i<=max;i++)
                    categories.add(i+" kg");

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity, R.layout.spinnerkg_text, categories);
                dataAdapter.setDropDownViewResource(R.layout.spinner_dd);
                holder.spinkg.setAdapter(dataAdapter);
            }
        }else if (cat_type.equals("instock")){
            if (!menuLists.get(position).getCat_food_type().equals("") || menuLists.get(position).getCat_food_type()!=null){
                food_cat_type= menuLists.get(position).getCat_food_type();
                if (food_cat_type.equalsIgnoreCase("Jain")) {
                    holder.llkg.setVisibility(View.VISIBLE);
                    Log.e("food_cat_type", food_cat_type);
                    List<String> food_cat = new ArrayList<String>();
                    food_cat.add("Regular");
                    food_cat.add(food_cat_type);
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity, R.layout.spinnerkg_text, food_cat);
                    dataAdapter.setDropDownViewResource(R.layout.spinner_dd);
                    holder.spinkg.setAdapter(dataAdapter);
                }
            }
        }

        holder.tvAddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String start_time = menuLists.get(position).getStart_time();
                String end_time = menuLists.get(position).getEnd_time();
                if (!start_time.equalsIgnoreCase("") && !start_time.equalsIgnoreCase("0") && start_time!=null) {
                    try {
                        Date time1 = new SimpleDateFormat("HH:mm").parse(start_time);
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.setTime(time1);
                        calendar1.add(Calendar.DATE, 1);

                        Date time2 = new SimpleDateFormat("HH:mm").parse(end_time);
                        Calendar calendar2 = Calendar.getInstance();
                        calendar2.setTime(time2);
                        calendar2.add(Calendar.DATE, 1);

                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        String str = sdf.format(new Date());
                        Date d = new SimpleDateFormat("HH:mm").parse(str);
                        Calendar calendar3 = Calendar.getInstance();
                        calendar3.setTime(d);
                        calendar3.add(Calendar.DATE, 1);

                        Log.d("string1->>", time1 + "");
                        Log.d("string2->>", time2 + "");
                        Log.d("date->>", d + "");
                        Date x = calendar3.getTime();
                        if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                            //checkes whether the current time is between 14:49:00 and 20:11:13.
                            //Toast.makeText(activity, "Delivery available", Toast.LENGTH_SHORT).show();
                            holder.r_addcart.setVisibility(View.VISIBLE);
                            holder.tvAddtoCart.setVisibility(View.GONE);
                            holder.incre.performClick();
                        } else {
                            //Toast.makeText(activity, "Delivery not available", Toast.LENGTH_SHORT).show();
                            String msg =  String.format(menuLists.get(position).getFood_time_msg(), menuLists.get(position).getName(), starttime+" - "+endtime);
                            /*AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle(R.string.app_name);
                            builder.setMessage(msg);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                            builder.show();*/
                            AlertDialog dialog=new AlertDialog.Builder(activity).setTitle(R.string.app_name).setMessage(msg)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    }).show();
                            TextView textView=dialog.findViewById(android.R.id.message);
                            Typeface face= null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                face = activity.getResources()
                                        .getFont(R.font.roboto_medium);
                            }
                            textView.setTypeface(face);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                    holder.r_addcart.setVisibility(View.VISIBLE);
                    holder.tvAddtoCart.setVisibility(View.GONE);
                    holder.incre.performClick();
                }
            }
        });

        try {
            dbhelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        Glide.with(activity).load(menuLists.get(position).getImage()).centerCrop().into(holder.imageView_Gallery);
        Glide.with(activity).load(menuLists.get(position).getFood_type()).centerCrop().into(holder.food_type);
        holder.name.setText(menuLists.get(position).getName());
        if(menuLists.get(position).getDesc().equalsIgnoreCase("") || menuLists.get(position).getDesc()==null)
            holder.desc.setVisibility(View.GONE);

        holder.desc.setText(Html.fromHtml(menuLists.get(position).getDesc()));

        if (cat_type.equalsIgnoreCase("advance"))
            holder.price.setText("₹ "+ menuLists.get(position).getPrice()+" per kg");
        else
            holder.price.setText("₹ "+ menuLists.get(position).getPrice());
        int count=dbhelper.getQuantity(Long.parseLong(menuLists.get(position).getmId()));
        if (count!=0) {
            holder.count.setText("" + count);
            holder.r_addcart.setVisibility(View.VISIBLE);
            holder.tvAddtoCart.setVisibility(View.GONE);
        }else{
            holder.r_addcart.setVisibility(View.GONE);
            holder.tvAddtoCart.setVisibility(View.VISIBLE);
        }

        if (cat_type.equalsIgnoreCase("advance")) {
            holder.spinkg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    menuLists.get(position).setKg(adapterView.getItemAtPosition(i).toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }else if (cat_type.equalsIgnoreCase("instock")){
            holder.spinkg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    menuLists.get(position).setCat_food_type(adapterView.getItemAtPosition(i).toString());// = adapterView.getItemAtPosition(i).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

        dbhelper.close();
        holder.incre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    dbhelper.openDataBase();
                } catch (SQLException sqle) {
                    throw sqle;
                }
                final int quantity;
                int cnt=dbhelper.getQuantity(Long.parseLong(menuLists.get(position).getmId()));
                if(cnt!=0)
                    quantity= cnt+1;
                else
                    quantity=1;
                final Long price=Long.parseLong(menuLists.get(position).getPrice());
                final Long id=Long.parseLong(menuLists.get(position).getmId());
                final String name=menuLists.get(position).getName();
                final String cat_type=menuLists.get(position).getCat_type();

                String dbcat = dbhelper.getCat(Long.parseLong(menuLists.get(position).getmId()));
                if (dbcat==null || cat_type.equals(dbcat)){
                    if (cat_type.equals("advance")) {

                        String[] separated = new String[0];
//                        try {
                        if (menuLists.get(position).getKg()!=null && !menuLists.get(position).getKg().equals("")) {
                            separated = menuLists.get(position).getKg().split(" ");
                            kilo = Integer.parseInt(separated[0]);

                        }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }

                        final String food_cat="";

                        if (dbhelper.isDataExist(id)) {
                            if ((menuLists.get(position).getMin()==null || menuLists.get(position).getMin().equals("")) && (menuLists.get(position).getMax()==null || menuLists.get(position).getMax().equals(""))){
                                dbhelper.updateData(id,quantity,((price * quantity)));
                            }else {
                                dbhelper.updateDataAdv(id, quantity, ((price * quantity) * kilo), menuLists.get(position).getKg());
                            }
                        } else {
                            if (menuLists.get(position).getFlavour_name().size()>0 && !menuLists.get(position).getFlavour_name().equals("")){
                                // custom dialog
                                ArrayList<String> flavour_name = menuLists.get(position).getFlavour_name();
                                ArrayList<String> flavour_id = menuLists.get(position).getFlavour_id();
                                final Dialog dialog = new Dialog(activity);
                                dialog.setTitle("Select Flavour : ");
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.radiobutton_dialog);
                                dialog.setCancelable(false);
                                List<String> stringList=new ArrayList<>();  // here is list
                                final List<String> stringListId=new ArrayList<>();  // here is list
                                for(int i=0;i<flavour_name.size();i++) {
                                    stringList.add(flavour_name.get(i));
                                    stringListId.add(flavour_id.get(i));
                                }
                                 rg = (RadioGroup) dialog.findViewById(R.id.radio_group);
                                final TextView submit = dialog.findViewById(R.id.submit);
                                for(int i=0;i<stringList.size();i++){
                                    RadioButton rb=new RadioButton(activity); // dynamically creating RadioButton and adding to RadioGroup.
                                    rb.setTextSize(14);
                                    rb.setText(stringList.get(i).replace(",",""));
                                    rg.addView(rb);
                                    if (i==0) {
                                        rb.setChecked(true);
                                        f_name=rb.getText().toString();
                                    }
                                }

                                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                                    @Override
                                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                                        int childCount = group.getChildCount();
                                        for (int x = 0; x < childCount; x++) {
                                            RadioButton btn = (RadioButton) group.getChildAt(x);
                                            if (btn.getId() == checkedId) {
                                                Log.e("selected RadioButton->",btn.getText().toString());
                                                Log.e("selected RadioBnID->",stringListId.get(x));
                                                f_name=btn.getText().toString();
                                                f_id=stringListId.get(x);
                                            }
                                        }
                                    }
                                });
                                submit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if ((menuLists.get(position).getMin()==null || menuLists.get(position).getMin().equals("")) && (menuLists.get(position).getMax()==null || menuLists.get(position).getMax().equals(""))){
                                            dbhelper.addData(id,name,quantity,((price * quantity)),cat_type,food_cat, Integer.parseInt(menuLists.get(position).getPrice()));
                                        }else {
                                            if (f_name!=null && !f_name.equals(""))
                                                dbhelper.addDataAdv(id, name+" ("+menuLists.get(position).getKg()+", "+f_name+")", quantity, ((price * quantity) * kilo), cat_type, menuLists.get(position).getKg(), min, max,f_id,f_name, Integer.parseInt(menuLists.get(position).getPrice()));
                                        }
                                        dialog.dismiss();
                                        activity.invalidateOptionsMenu();
                                    }
                                });
                                dialog.show();
                            }else {
                                if ((menuLists.get(position).getMin()==null || menuLists.get(position).getMin().equals("")) && (menuLists.get(position).getMax()==null || menuLists.get(position).getMax().equals(""))){
                                    dbhelper.addData(id,name,quantity,((price * quantity)),cat_type,food_cat, Integer.parseInt(menuLists.get(position).getPrice()));
                                }else {
                                    dbhelper.addDataAdv(id, name+" ("+menuLists.get(position).getKg()+")", quantity, ((price * quantity) * kilo), cat_type, menuLists.get(position).getKg(), min, max,"","", Integer.parseInt(menuLists.get(position).getPrice()));
                                }
                            }

                        }
                    }else{
                        if (dbhelper.isDataExist(id)) {
                            dbhelper.updateData(id, quantity, (price * quantity));
                        } else {
                            Log.e("food_cat",menuLists.get(position).getCat_food_type());
                            if (!menuLists.get(position).getCat_food_type().equals("") && menuLists.get(position)!=null)
                                dbhelper.addData(id, name+" ("+menuLists.get(position).getCat_food_type()+")", quantity, (price * quantity), cat_type,menuLists.get(position).getCat_food_type(), Integer.parseInt(menuLists.get(position).getPrice()));
                            else
                                dbhelper.addData(id, name, quantity, (price * quantity), cat_type,menuLists.get(position).getCat_food_type(), Integer.parseInt(menuLists.get(position).getPrice()));

                        }
                    }
                    holder.count.setText(""+quantity);
                    dbhelper.close();
                    activity.invalidateOptionsMenu();
                }else {
                    showAlertDialog();
                }

                Log.e("rvdb :"," "+dbhelper.getCat(Long.parseLong(menuLists.get(position).getmId())));


            }
        });

        holder.decre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dbhelper.openDataBase();
                } catch (SQLException sqle) {
                    throw sqle;
                }
                Integer quantity=0;
                if(Integer.parseInt(holder.count.getText().toString())!=0) {
                    quantity = Integer.parseInt(holder.count.getText().toString()) - 1;
                    Long price = Long.parseLong(menuLists.get(position).getPrice());
                    Long id = Long.parseLong(menuLists.get(position).getmId());
                    String name = menuLists.get(position).getName();
                    String cat_type=menuLists.get(position).getCat_type();
                    String food_cat = menuLists.get(position).getCat_food_type();

                    if (dbhelper.isDataExist(id)) {
                        dbhelper.updateData(id, quantity, (price * quantity));
                    } else {
                        if (food_cat.equals("") && food_cat==null)
                            dbhelper.addData(id, name, quantity, (price * quantity),cat_type,food_cat, Integer.parseInt(menuLists.get(position).getPrice()));
                        else
                            dbhelper.addData(id, name+" ("+food_cat+")", quantity, (price * quantity),cat_type,food_cat, Integer.parseInt(menuLists.get(position).getPrice()));
                    }
                }
                holder.count.setText(""+quantity);
                dbhelper.close();
                activity.invalidateOptionsMenu();
            }
        });

    }

    public void showAlertDialog() {

        final AlertDialog dialog;
        dialog=new AlertDialog.Builder(activity).setTitle(R.string.confirm).setMessage(R.string.db_exist_alert)
                .setPositiveButton(R.string.option_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbhelper.deleteAllData();
                        activity.invalidateOptionsMenu();
                        dbhelper.close();
                    }
                }).setNegativeButton(R.string.option_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbhelper.close();
//                        dialog.cancel();
                    }
                }).show();
        TextView textView=dialog.findViewById(android.R.id.message);
        Typeface face= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            face = activity.getResources()
                    .getFont(R.font.roboto_medium);
        }
        textView.setTypeface(face);


       /* AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.confirm);
        builder.setMessage(activity.getString(R.string.db_exist_alert));
        builder.setCancelable(false);
        builder.setPositiveButton(activity.getString(R.string.option_yes), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dbhelper.deleteAllData();
                activity.invalidateOptionsMenu();
                dbhelper.close();
            }
        });

        builder.setNegativeButton(activity.getString(R.string.option_no), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dbhelper.close();
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();*/

    }

    public void increaseInteger(int position,TextView view) {
        minteger = minteger + 1;
        display(minteger,view);
    }public void decreaseInteger(int position,TextView view) {
            minteger = minteger - 1;
            display(minteger,view);
    }

    private void display(int number,TextView view) {

        view.setText("" + number);
    }

    @Override
    public int getItemCount() {
        return menuLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView_Gallery,food_type;
        private TextView count,name,price,desc,tvAddtoCart,tvtime;
        private ImageView incre,decre;
        private LinearLayout llkg;
        private Spinner spinkg;
        private RelativeLayout r_addcart;
        private CardView row_productlist_cvContainer;

        public ViewHolder(View itemView) {
            super(itemView);

            llkg = itemView.findViewById(R.id.rel_kg);
            spinkg = itemView.findViewById(R.id.spinner);
            tvAddtoCart = itemView.findViewById(R.id.tvAddtoCart);
            food_type = itemView.findViewById(R.id.foodtype_img);
            imageView_Gallery = (ImageView) itemView.findViewById(R.id.imageView_gallery_adapter);
            incre =  itemView.findViewById(R.id.increase);
            decre = itemView.findViewById(R.id.decrease);
            count =  itemView.findViewById(R.id.count);
            name = (TextView) itemView.findViewById(R.id.name);
            price = (TextView) itemView.findViewById(R.id.price);
            desc = (TextView) itemView.findViewById(R.id.desc);
            r_addcart = itemView.findViewById(R.id.r_addcart);
            tvtime = itemView.findViewById(R.id.tvtime);
            row_productlist_cvContainer = itemView.findViewById(R.id.row_productlist_cvContainer);

        }
    }

}

