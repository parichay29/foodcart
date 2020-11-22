package com.rndtechnosoft.foodcart.Adapter;

import android.app.Activity;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rndtechnosoft.foodcart.Activity.CouponActivity;
import com.rndtechnosoft.foodcart.Item.CouponList;
import com.rndtechnosoft.foodcart.R;
import com.rndtechnosoft.foodcart.Util.Method;

import java.util.ArrayList;

public class CouponCodeAdapter extends RecyclerView.Adapter<CouponCodeAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<CouponList> couponLists;
    private Method method;
    String amount;

    public CouponCodeAdapter(Activity activity, ArrayList<CouponList> couponLists,String amount) {
        this.activity = activity;
        this.couponLists = couponLists;
        method = new Method(activity);
        this.amount = amount;
    }

    @Override
    public CouponCodeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.coupon_code_list, parent, false);

        return new CouponCodeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CouponCodeAdapter.ViewHolder holder, final int position) {

        holder.txtcode.setText(couponLists.get(position).getCoupon_code());
        holder.title.setText(couponLists.get(position).getTitle());
        holder.desc.setText(couponLists.get(position).getTnc());
        holder.tnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final BottomSheetDialog dialog = new BottomSheetDialog(activity);
                    dialog.setContentView(R.layout.bottom_dialog);
                    TextView cancelBtn = dialog.findViewById(R.id.cancel);
                    Button doneBtn = dialog.findViewById(R.id.done);
                    TextView term_condition = dialog.findViewById(R.id.term_condition);
                    term_condition.setText(couponLists.get(position).getTnc());
                    dialog.show();

                    doneBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        holder.apply_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CouponActivity context=(CouponActivity) activity;
                context.getDiscount(holder.txtcode.getText().toString(),amount);
            }
        });

    }

    @Override
    public int getItemCount() {
        return couponLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtcode,title, desc, tnc;
        Button apply_button;

        public ViewHolder(View itemView) {
            super(itemView);

            txtcode = itemView.findViewById(R.id.coupon_code);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            tnc = itemView.findViewById(R.id.tnc);
            apply_button = itemView.findViewById(R.id.apply_button);

        }
    }
}

