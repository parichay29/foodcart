package com.rndtechnosoft.foodcart.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.SQLException;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.rndtechnosoft.foodcart.Helper.DBHelper;
import com.rndtechnosoft.foodcart.Item.MenuDetailList;
import com.rndtechnosoft.foodcart.R;
import com.rndtechnosoft.foodcart.Util.Method;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SingleMenuAdapter extends RecyclerView.Adapter<SingleMenuAdapter.ViewHolder> {

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


    public SingleMenuAdapter(Activity activity, List<MenuDetailList> menuLists) {
        this.activity = activity;
        this.menuLists = menuLists;
        method = new Method(activity);
//        Resources r = activity.getResources();
        dbhelper = new DBHelper(activity);
    }

    @Override
    public SingleMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.layout_single_item, parent, false);

        return new SingleMenuAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SingleMenuAdapter.ViewHolder holder, final int position) {
//        holder.tv_item_name.setText(menuLists.get(position).getName());
//        Glide.with(activity).load(menuLists.get(position).getImage()).centerCrop().into(holder.imageView);

        try {
            dbhelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        Glide.with(activity).load(menuLists.get(position).getImage()).centerCrop().into(holder.imageView);
        Glide.with(activity).load(menuLists.get(position).getFood_type()).centerCrop().into(holder.foodtype_img);
        holder.tv_item_name.setText(menuLists.get(position).getName());
        holder.price.setText("₹ "+ menuLists.get(position).getPrice());
        int count=dbhelper.getQuantity(Long.parseLong(menuLists.get(position).getmId()));
        if (count!=0) {
            holder.count.setText("" + count);
            holder.r_addcart.setVisibility(View.VISIBLE);
            holder.tvAdd.setVisibility(View.GONE);
        }else{
            holder.r_addcart.setVisibility(View.GONE);
            holder.tvAdd.setVisibility(View.VISIBLE);
        }
        holder.count.setText(""+count);

        if (cat_type.equalsIgnoreCase("instock")){
            holder.spinspe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    menuLists.get(position).setCat_food_type(adapterView.getItemAtPosition(i).toString());// = adapterView.getItemAtPosition(i).toString();

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    //c_type = adapterView.getItemAtPosition(0).toString();
                }
            });
        }



        holder.tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                        Calendar now = Calendar.getInstance();

                        int hour = now.get(Calendar.HOUR);
                        int minute = now.get(Calendar.MINUTE);


//                    String someRandomTime = "15:30";
                        //String someRandomTime = hour + ":" + minute;
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
                            holder.tvAdd.setVisibility(View.GONE);
                            holder.incre.performClick();
                        } else {
                            String starttime = LocalTime.parse(menuLists.get(position).getStart_time(), DateTimeFormatter.ofPattern("HH:mm")).format(DateTimeFormatter.ofPattern("hh:mm a"));
                            String endtime = LocalTime.parse(menuLists.get(position).getEnd_time(), DateTimeFormatter.ofPattern("HH:mm")).format(DateTimeFormatter.ofPattern("hh:mm a"));
                            //Toast.makeText(activity, "Delivery not available", Toast.LENGTH_SHORT).show();
                            String msg =  String.format(menuLists.get(position).getFood_time_msg(), menuLists.get(position).getName(), starttime+" - "+endtime);
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle(R.string.app_name);
                            builder.setMessage(msg);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                            builder.show();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                    holder.r_addcart.setVisibility(View.VISIBLE);
                    holder.tvAdd.setVisibility(View.GONE);
                    holder.incre.performClick();
                }
            }
        });

        holder.incre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    dbhelper.openDataBase();
                } catch (SQLException sqle) {
                    throw sqle;
                }


                Integer quantity =Integer.parseInt(holder.count.getText().toString())+1;
                Long price=Long.parseLong(menuLists.get(position).getPrice());
                Long id=Long.parseLong(menuLists.get(position).getmId());
                String name=menuLists.get(position).getName();
                String cat_type=menuLists.get(position).getCat_type();

                String dbcat = dbhelper.getCat(Long.parseLong(menuLists.get(position).getmId()));
                if (dbcat==null || cat_type.equals(dbcat)) {

                    if (dbhelper.isDataExist(id)) {
                        dbhelper.updateData(id, quantity, (price * quantity));
                    } else {
                        Log.e("food_cat", menuLists.get(position).getCat_food_type());
                        dbhelper.addData(id, name+" (Regular)", quantity, (price * quantity), cat_type, "Regular", Integer.parseInt(menuLists.get(position).getPrice()));
                    }

                    holder.count.setText("" + quantity);
                    dbhelper.close();
                    activity.invalidateOptionsMenu();

                }

                Log.e("singledb :"," "+dbhelper.getCat(Long.parseLong(menuLists.get(position).getmId())));


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
                        dbhelper.addData(id, name+" (Regular)", quantity, (price * quantity),cat_type,food_cat, Integer.parseInt(menuLists.get(position).getPrice()));
                    }
                }
                holder.count.setText(""+quantity);
                dbhelper.close();
                activity.invalidateOptionsMenu();
            }
        });

        dbhelper.close();
    }

    @Override
    public int getItemCount() {
        return menuLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rootlayout;
        private RoundedImageView imageView;
        private ImageView foodtype_img;
        private TextView tv_item_name,count,price,tvAdd;
        private LinearLayout llspe;
        private Spinner spinspe;
        private ImageView incre,decre;
        private RelativeLayout r_addcart;

        public ViewHolder(View view) {
            super(view);

            rootlayout = view.findViewById(R.id.rootlayout);
            llspe = itemView.findViewById(R.id.rel_spe);
            spinspe = itemView.findViewById(R.id.spinnersp);
            imageView = view.findViewById(R.id.iv_home_latest);
            foodtype_img = view.findViewById(R.id.foodtype_img);
            price = (TextView) itemView.findViewById(R.id.rupees);
            tv_item_name = view.findViewById(R.id.tv_item_name);
            incre =  itemView.findViewById(R.id.incc);
            decre = itemView.findViewById(R.id.decc);
            count =  itemView.findViewById(R.id.qty);
            tvAdd = itemView.findViewById(R.id.tvAddtoCart);
            r_addcart = itemView.findViewById(R.id.r_addcart);

        }
    }
}

