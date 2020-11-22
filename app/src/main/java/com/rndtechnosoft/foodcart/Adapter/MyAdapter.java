package com.rndtechnosoft.foodcart.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.rndtechnosoft.foodcart.Fragment.AdvanceFragment;
import com.rndtechnosoft.foodcart.Fragment.StockFragment;

public class MyAdapter extends FragmentPagerAdapter {
  
    private Context myContext;
    int totalTabs;  
  
    public MyAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);  
        myContext = context;  
        this.totalTabs = totalTabs;  
    }  
  
    // this is for fragment tabs  
    @Override  
    public Fragment getItem(int position) {
        switch (position) {  
            case 0:  
                StockFragment stockFragment = new StockFragment();
                return stockFragment;
            case 1:  
                AdvanceFragment advanceFragment = new AdvanceFragment();
                return advanceFragment;
            default:  
                return null;  
        }  
    }  
// this counts total number of tabs  
    @Override  
    public int getCount() {  
        return totalTabs;  
    }  
}  