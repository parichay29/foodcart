package com.rndtechnosoft.foodcart.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.CountDownTimer;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rndtechnosoft.foodcart.Activity.ActivityConfirmMessage;
import com.rndtechnosoft.foodcart.Item.MyOrderList;
import com.rndtechnosoft.foodcart.R;
import com.rndtechnosoft.foodcart.Util.Constant_Api;
import com.rndtechnosoft.foodcart.Util.Method;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder> {

    private Activity activity;
    public List<MyOrderList> menuLists;
    private Method method;
    int counter;
    private long milliseconds;
    private long startTime;
    private long diff;
    private CountDownTimer mCountDownTimer;

    public MyOrdersAdapter(Activity activity, List<MyOrderList> menuLists) {
        this.activity = activity;
        this.menuLists = menuLists;
        method = new Method(activity);
        Resources r = activity.getResources();
//        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, r.getDisplayMetrics());
//        columnWidth = (int) ((method.getScreenWidth() - ((3 + 1) * padding)) / 2);
    }

    @Override
    public MyOrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.menu_myorder_adapter, parent, false);
        return new MyOrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyOrdersAdapter.ViewHolder holder, final int position) {

        counter=35;
        if(menuLists.get(position).getStatus().equalsIgnoreCase("completed")){
            holder.imageView_Gallery.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_receipt));
        }else if(menuLists.get(position).getStatus().equalsIgnoreCase("order cancelled")){
            holder.imageView_Gallery.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_no_food));
        }else if(menuLists.get(position).getStatus().equalsIgnoreCase("dispatched")){
            holder.imageView_Gallery.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_moped));
        }else if(menuLists.get(position).getStatus().equalsIgnoreCase("in progress")){
            holder.imageView_Gallery.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_progress));
        }else{
            holder.imageView_Gallery.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_pending));
        }
        holder.orderno.setText("Order No. "+menuLists.get(position).getId());
        holder.time.setText("Ordered at "+Html.fromHtml(menuLists.get(position).getDatetime()));
        holder.type.setText(menuLists.get(position).getOrder_type());
        holder.status.setText(menuLists.get(position).getStatus());

        /*String del_time=menuLists.get(position).getDel_time();
        if (del_time!=null && !del_time.equals("")) {
            reverseTimer(Integer.parseInt(del_time)*60, holder.deliveryTime, menuLists.get(position).getDel_msg(), menuLists.get(position).getDel_done());
        }*/

        if(menuLists.get(position).getStatus().equalsIgnoreCase(Constant_Api.TAKE_AWAY)){
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ActivityConfirmMessage.class).putExtra("order_id",Integer.parseInt(menuLists.get(position).getId()));
                activity.startActivity(intent);
            }
        });

        if(menuLists.get(position).getCat_type().equalsIgnoreCase("instock")) {
            if (!menuLists.get(position).getStatus().equalsIgnoreCase("pending")) {
                holder.deliveryTime.setVisibility(View.VISIBLE);
                String del_time = menuLists.get(position).getDel_time();
                if (del_time != null && !del_time.equals("")) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy, HH:mm:ss");
                    formatter.setLenient(false);

                    String endTime = del_time;
                    Date endDate;
                    try {
                        endDate = formatter.parse(endTime);
                        milliseconds = endDate.getTime();

                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    startTime = System.currentTimeMillis();
                    diff = milliseconds - startTime;
                    mCountDownTimer = new CountDownTimer(milliseconds, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            startTime = startTime - 1;
                            Long serverUptimeSeconds =
                                    (millisUntilFinished - startTime) / 1000;
                            long min = ((serverUptimeSeconds % 86400) % 3600) / 60;
                            long hour = ((serverUptimeSeconds % 86400) / 3600);
                            long sec = ((serverUptimeSeconds % 86400) % 3600) % 60;
                            if (min > 0) {
                                if (hour<10 && min<10 && sec<10) {
                                    String minutesLeft = null;
                                    try {
                                        minutesLeft = String.format(menuLists.get(position).getDel_msg(), "0"+hour + ":0" + min + ":0" + sec);
                                        holder.deliveryTime.setText(minutesLeft);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }else if (hour<10 && min<10){
                                    String minutesLeft = null;
                                    try {
                                        minutesLeft = String.format(menuLists.get(position).getDel_msg(), "0"+hour + ":0" + min + ":" + sec);
                                        holder.deliveryTime.setText(minutesLeft);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }else if (hour<10 && sec<10){
                                    String minutesLeft = null;
                                    try {
                                        minutesLeft = String.format(menuLists.get(position).getDel_msg(), "0"+hour + ":" + min + ":0" + sec);
                                        holder.deliveryTime.setText(minutesLeft);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }else if (min<10 && sec<10){
                                    String minutesLeft = null;
                                    try {
                                        minutesLeft = String.format(menuLists.get(position).getDel_msg(), hour + ":0" + min + ":0" + sec);
                                        holder.deliveryTime.setText(minutesLeft);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }else if (hour<10){
                                    String minutesLeft = null;
                                    try {
                                        minutesLeft = String.format(menuLists.get(position).getDel_msg(), "0"+hour + ":" + min + ":" + sec);
                                        holder.deliveryTime.setText(minutesLeft);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }else if (min<10){
                                    String minutesLeft = null;
                                    try {
                                        minutesLeft = String.format(menuLists.get(position).getDel_msg(), hour + ":0" + min + ":" + sec);
                                        holder.deliveryTime.setText(minutesLeft);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }else if (sec<10){
                                    String minutesLeft = null;
                                    try {
                                        minutesLeft = String.format(menuLists.get(position).getDel_msg(), hour + ":" + min + ":0" + sec);
                                        holder.deliveryTime.setText(minutesLeft);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }else {
                                    String minutesLeft = null;
                                    try {
                                        minutesLeft = String.format(menuLists.get(position).getDel_msg(), hour + ":" + min + ":" + sec);
                                        holder.deliveryTime.setText(minutesLeft);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                            } else {
                                try {
                                    holder.deliveryTime.setText(menuLists.get(position).getDel_done());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFinish() {
                            holder.deliveryTime.setText(menuLists.get(position).getDel_done());
                        }
                    }.start();
                }
            }
        }else {
            if (menuLists.get(position).getStatus().equalsIgnoreCase("completed")){
                holder.deliveryTime.setVisibility(View.VISIBLE);
                holder.deliveryTime.setText(menuLists.get(position).getDel_done());
            }else {
                holder.deliveryTime.setVisibility(View.VISIBLE);
                String msg = String.format(menuLists.get(position).getAdmsg(), menuLists.get(position).getAddate() + " at " + menuLists.get(position).getAdtime());
                holder.deliveryTime.setText(msg);
            }
        }
    }

    @Override
    public int getItemCount() {
        return menuLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView_Gallery;
        CardView cardView;
        private TextView orderno,time,type,status,deliveryTime;

        public ViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_view);
            imageView_Gallery = (ImageView) itemView.findViewById(R.id.imgstatus);
            orderno = (TextView) itemView.findViewById(R.id.orderno);
            time = (TextView) itemView.findViewById(R.id.time);
            type = (TextView) itemView.findViewById(R.id.ordertype);
            status = (TextView) itemView.findViewById(R.id.status);
            deliveryTime = (TextView) itemView.findViewById(R.id.deliveryTime);

        }
    }
}

