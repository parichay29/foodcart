package com.rndtechnosoft.foodcart.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rndtechnosoft.foodcart.Activity.ScratchCardActivity;
import com.rndtechnosoft.foodcart.Item.ScratchModel;
import com.rndtechnosoft.foodcart.R;
import com.rndtechnosoft.foodcart.Util.Constant_Api;
import com.rndtechnosoft.foodcart.Util.Constants;
import com.rndtechnosoft.foodcart.Util.SharedPref;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import in.myinnos.androidscratchcard.ScratchCard;

public class ScratchCardTypeAdapter extends RecyclerView.Adapter {

    private ArrayList<ScratchModel> dataSet;
    Activity mContext;
    int total_types;
    ScratchCardActivity scratchCardActivity;

    public static class TextTypeViewHolder extends RecyclerView.ViewHolder {

        TextView txtTitle,txtAmount;
        ImageView image;
        LinearLayout textscratch;

        public TextTypeViewHolder(View itemView) {
            super(itemView);

            this.image = (ImageView) itemView.findViewById(R.id.imgviewText);
            this.textscratch = (LinearLayout) itemView.findViewById(R.id.textscratch);
            this.txtTitle = (TextView) itemView.findViewById(R.id.texttitle);
            this.txtAmount = (TextView) itemView.findViewById(R.id.textamount);
        }
    }

    public static class ImageTypeViewHolder extends RecyclerView.ViewHolder {

        ImageView image;

        public ImageTypeViewHolder(View itemView) {
            super(itemView);

            this.image = (ImageView) itemView.findViewById(R.id.imgview);
        }
    }

    public ScratchCardTypeAdapter(ArrayList<ScratchModel>data, Activity context) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();
        scratchCardActivity = (ScratchCardActivity) mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case ScratchModel.TEXT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scratchtext, parent, false);
                return new TextTypeViewHolder(view);
            case ScratchModel.IMAGE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scratchimage, parent, false);
                return new ImageTypeViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        switch (dataSet.get(position).type) {
            case 0:
                return ScratchModel.TEXT_TYPE;
            case 1:
                return ScratchModel.IMAGE_TYPE;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        ScratchModel object = dataSet.get(listPosition);

        if (object != null) {
            switch (object.type) {
                case ScratchModel.TEXT_TYPE:
                    try {
                        if (Constants.INVISIBLE_STATUS.equals(object.getScratch().getStatus())) {
                            ((TextTypeViewHolder) holder).image.setVisibility(View.VISIBLE);
                            ((TextTypeViewHolder) holder).textscratch.setVisibility(View.GONE);
                        } else {
                            ((TextTypeViewHolder) holder).image.setVisibility(View.GONE);
                            ((TextTypeViewHolder) holder).textscratch.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    ((TextTypeViewHolder) holder).textscratch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialog(mContext, "text", listPosition);
                        }
                    });

                    try {
                        Picasso.with(mContext).load(R.drawable.sc2).into(((TextTypeViewHolder) holder).image);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    ((TextTypeViewHolder) holder).image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialog(mContext, "text", listPosition);
                        }
                    });
                    if (Integer.parseInt(object.getScratch().getAmount()) > 0) {
                        ((TextTypeViewHolder) holder).txtTitle.setText(object.getScratch().getCoupon_text());
                        ((TextTypeViewHolder) holder).txtAmount.setText(object.getScratch().getAmount());
                    } else {
                        ((TextTypeViewHolder) holder).txtTitle.setText(object.getScratch().getSorrymsg());
                        ((TextTypeViewHolder) holder).txtAmount.setVisibility(View.GONE);
                    }

                    break;
                case ScratchModel.IMAGE_TYPE:
                    try {
                        if (Constants.INVISIBLE_STATUS.equals(object.getScratch().getStatus())) {
                            try {
                                Picasso.with(mContext).load(R.drawable.sc2).into(((ImageTypeViewHolder) holder).image);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                Picasso.with(mContext).load(object.getScratch().getImage()).into(((ImageTypeViewHolder) holder).image);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ((ImageTypeViewHolder) holder).image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialog(mContext, "image", listPosition);
                        }
                    });
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void showDialog(final Activity activity, String msg, final int position){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dialog.dismiss();
                scratchCardActivity.getCouponList();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                scratchCardActivity.getCouponList();
            }
        });
        dialog.setContentView(R.layout.scratch_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        final ScratchCard mScratchCard = dialog.findViewById(R.id.scratchImage);
        ImageView img_cancel = dialog.findViewById(R.id.img_cancel);
        Button dialogButton2 = (Button) dialog.findViewById(R.id.btn2);
        LinearLayout linearTextScratch = dialog.findViewById(R.id.textscratch);
        TextView texttitle = dialog.findViewById(R.id.texttitle);
        TextView textamount = dialog.findViewById(R.id.textamount);
        final TextView text_dialog = dialog.findViewById(R.id.text_dialog);
        ImageView imgview= dialog.findViewById(R.id.imgview);
        text_dialog.setVisibility(View.GONE);

        if (msg.equalsIgnoreCase("text")) {
            linearTextScratch.setVisibility(View.VISIBLE);
            imgview.setVisibility(View.GONE);
            if (Integer.parseInt(dataSet.get(position).getScratch().getAmount())>0) {
                texttitle.setText(dataSet.get(position).getScratch().getCoupon_text());
                text_dialog.setText(dataSet.get(position).getScratch().getCongratsmsg());
                textamount.setText(dataSet.get(position).getScratch().getAmount());
                textamount.setVisibility(View.VISIBLE);
            }else {
                texttitle.setText(dataSet.get(position).getScratch().getSorrymsg());
                text_dialog.setText(dataSet.get(position).getScratch().getSorrymsg());
                textamount.setVisibility(View.GONE);
            }
        }else {
            linearTextScratch.setVisibility(View.GONE);
            imgview.setVisibility(View.VISIBLE);
            if (Integer.parseInt(dataSet.get(position).getScratch().getAmount())>0) {
                text_dialog.setText(dataSet.get(position).getScratch().getCongratsmsg());
            }else {
                text_dialog.setText(dataSet.get(position).getScratch().getSorrymsg());
            }
            try {
                Picasso.with(mContext).load(dataSet.get(position).getScratch().getImage()).into(imgview);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (Constants.INVISIBLE_STATUS.equals(dataSet.get(position).getScratch().getStatus())){
            mScratchCard.setVisibility(View.VISIBLE);
        }else {
            mScratchCard.setVisibility(View.GONE);
        }

        RelativeLayout relativeLayout= dialog.findViewById(R.id.rel_dialog);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                scratchCardActivity.getCouponList();
            }
        });

        mScratchCard.setOnScratchListener(new ScratchCard.OnScratchListener() {
            @Override
            public void onScratch(ScratchCard scratchCard, float visiblePercent) {
                if (visiblePercent > 0.7) {
                    text_dialog.setVisibility(View.VISIBLE);
                    mScratchCard.setVisibility(View.GONE);
                    int amount = Integer.parseInt(dataSet.get(position).getScratch().getAmount());
                    int sc_id = Integer.parseInt(dataSet.get(position).getScratch().getId());
                    walletpay(amount,sc_id);
                }
            }
        });

        img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                scratchCardActivity.getCouponList();
            }
        });

        dialogButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                scratchCardActivity.getCouponList();
            }
        });

        dialog.show();

    }


    private void walletpay(int amount,int sc_id){

        String url = Constant_Api.addtowallet+"&amount="+amount+"&sc_id="+sc_id;
        Log.e("walletpayurl->>>",amount+"->>>"+sc_id);

        AsyncHttpClient client = new AsyncHttpClient();
        /*client.addHeader("App-Id",Constant_Api.HOTEL_ID);*/
        client.addHeader("userid", SharedPref.getUserId(mContext));
        client.get(url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                Log.d("Response", new String(responseBody));
                String response = new String(responseBody);
                String[] separated = response.split("end");
                String res= separated[1];

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray("WALLET_PAY");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String error = object.getString("error");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //progressBar.hide();
            }
        });

    }

}
