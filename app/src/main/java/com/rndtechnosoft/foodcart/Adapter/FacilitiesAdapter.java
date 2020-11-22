package com.rndtechnosoft.foodcart.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rndtechnosoft.foodcart.R;
import com.rndtechnosoft.foodcart.Util.Method;

public class FacilitiesAdapter extends RecyclerView.Adapter<FacilitiesAdapter.ViewHolder> {

    private Activity activity;
    private String[] separated;
    private Method method;

    public FacilitiesAdapter(Activity activity, String[] separated) {
        this.activity = activity;
        this.separated = separated;
        method = new Method(activity);
    }

    @Override
    public FacilitiesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.facilities_adapter, parent, false);

        return new FacilitiesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.textView_Name.setText(separated[position]);

    }

    @Override
    public int getItemCount() {
        return separated.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView_Name;

        public ViewHolder(View itemView) {
            super(itemView);

            textView_Name = (TextView) itemView.findViewById(R.id.textView_name_facilities_adapter);

        }
    }
}
