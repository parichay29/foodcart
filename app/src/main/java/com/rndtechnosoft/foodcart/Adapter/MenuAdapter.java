package com.rndtechnosoft.foodcart.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rndtechnosoft.foodcart.Activity.MenuDetailActivity;
import com.rndtechnosoft.foodcart.Item.MenuList;
import com.rndtechnosoft.foodcart.R;
import com.rndtechnosoft.foodcart.Util.Method;
import com.bumptech.glide.Glide;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private Activity activity;
    private List<MenuList> menuLists;
    private Method method;

    public MenuAdapter(Activity activity, List<MenuList> menuLists) {
        this.activity = activity;
        this.menuLists = menuLists;
        method = new Method(activity);
//        Resources r = activity.getResources();
//        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, r.getDisplayMetrics());
//        columnWidth = (int) ((method.getScreenWidth() - ((3 + 1) * padding)) / 2);
    }

    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.menu_adapter, parent, false);

        return new MenuAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuAdapter.ViewHolder holder, final int position) {

//        holder.imageView_Gallery.setLayoutParams(new RelativeLayout.LayoutParams(columnWidth, ViewGroup.LayoutParams.MATCH_PARENT));
//
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(columnWidth,  ViewGroup.LayoutParams.MATCH_PARENT);
//        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        holder.view.setLayoutParams(params);

        Glide.with(activity).load(menuLists.get(position).getCategory_image()).into(holder.imageView_Gallery);
        holder.textView_galleryName.setText(menuLists.get(position).getCategory_name());
//        holder.textView_galleryName.setTypeface(method.OpenSans_Regular);

        holder.imageView_Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MenuDetailActivity.class);
                intent.putExtra("id", menuLists.get(position).getCid());
                intent.putExtra("title", menuLists.get(position).getCategory_name());
                intent.putExtra("position",position);
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return menuLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView_Gallery;
        private TextView textView_galleryName;
        private View view;
        private RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView_Gallery = (ImageView) itemView.findViewById(R.id.imageView_gallery_adapter);
            view = (View) itemView.findViewById(R.id.view_gallery_adapter);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout_gallery_adapter);
            textView_galleryName = (TextView) itemView.findViewById(R.id.textView_galleryname_gallery_adapter);

        }
    }
}

