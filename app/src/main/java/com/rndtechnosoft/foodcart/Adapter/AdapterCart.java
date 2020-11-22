package com.rndtechnosoft.foodcart.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.SQLException;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rndtechnosoft.foodcart.Activity.ActivityCart;
import com.rndtechnosoft.foodcart.Helper.DBHelper;
import com.rndtechnosoft.foodcart.Item.ItemCart;
import com.rndtechnosoft.foodcart.R;

import java.util.List;

public class AdapterCart extends RecyclerView.Adapter<AdapterCart.ViewHolder> {

    private Context context;
    private List<ItemCart> arrayItemCart;
    DBHelper dbhelper;
    private String cat_type;
    int min,max;
    private String kilo;
    private int kg=0;
    private int kgcal=0;

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgDelete;
        TextView txtMenuName;
        TextView txtQuantity;
        TextView txtPrice;
        TextView txtperprice;
        ImageView imgMinus;
        ImageView imgPlus;
        Spinner spinkg;

        public ViewHolder(View view) {
            super(view);

            imgDelete = (ImageView) view.findViewById(R.id.imgDelete);
            txtMenuName = (TextView) view.findViewById(R.id.txtMenuName);
            txtQuantity = (TextView) view.findViewById(R.id.txtQuantity);
            txtPrice = (TextView) view.findViewById(R.id.txtPrice);
            txtperprice = (TextView) view.findViewById(R.id.txtperprice);
            imgMinus = view.findViewById(R.id.imgMinus);
            imgPlus = view.findViewById(R.id.imgPlus);
            spinkg = view.findViewById(R.id.spinkg);

        }

    }

    public AdapterCart(Activity context, List<ItemCart> arrayItemCart) {
        this.context = context;
        this.arrayItemCart = arrayItemCart;
        dbhelper = new DBHelper(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lsv_item_order_list, parent, false);
            return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        cat_type=ActivityCart.cat_type;
       /* if (cat_type.equals("advance")) {
            if ((ActivityCart.min.get(position) != null) && (ActivityCart.max.get(position) != null)) {
                holder.spinkg.setVisibility(View.VISIBLE);

                min= Integer.parseInt(ActivityCart.min.get(position));
                max= Integer.parseInt(ActivityCart.max.get(position));
                Log.e("minkg", String.valueOf(min));
                Log.e("maxkg", String.valueOf(max));
                List<String> categories = new ArrayList<String>();
                for (int i=min;i<=max;i++)
                    categories.add(i+" kg");



                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, R.layout.spinnerkg_text, categories);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(R.layout.spinner_dd);

                // attaching data adapter to spinner
                holder.spinkg.setAdapter(dataAdapter);

                String[] separated = ActivityCart.kg.get(position).split(" ");

                kilo=ActivityCart.kg.get(position);
               // kilo= separated[0];
                for (int i=0;i<categories.size();i++){
                    if (kilo.equals(categories.get(i))){
                        //int spinnerPosition = dataAdapter.getPosition(kilo);
                        holder.spinkg.setSelection(i);
                        break;
                    }
                }

            }
        }*/


        holder.spinkg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                kilo = adapterView.getItemAtPosition(i).toString();

                Toast.makeText(context, "CART", Toast.LENGTH_SHORT).show();

                String[] kilosep = kilo.split(" ");

                kgcal= Integer.parseInt(kilosep[0]);
                String[] separated = ActivityCart.kg.get(position).split(" ");
                kg = Integer.parseInt(separated[0]);
                Integer quantity =Integer.parseInt(holder.txtQuantity.getText().toString());
                Double price=((ActivityCart.Sub_total_price.get(position)/(quantity*kg)));
                Long id= Long.valueOf(ActivityCart.Menu_ID.get(position));
                dbhelper.updateDataAdv(id, quantity, ((price * quantity)*kgcal),kilo);
                //adapterView.notifyAll();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        holder.txtMenuName.setText(ActivityCart.Menu_name.get(position));
        holder.txtQuantity.setText(String.valueOf(ActivityCart.Quantity.get(position)));
        holder.txtPrice.setText(context.getResources().getString(R.string.rupee)+ActivityCart.Sub_total_price.get(position));
        holder.txtperprice.setTextColor(context.getResources().getColor(R.color.new_green));
        holder.txtperprice.setText("Price : "+context.getResources().getString(R.string.rupee)+ActivityCart.per_price.get(position));
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.confirm);
                builder.setMessage(context.getString(R.string.clear_one_order));
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                                dbhelper.deleteData(ActivityCart.Menu_ID.get(position));
                                ActivityCart.Menu_ID.remove(position);
                                notifyItemRemoved(position);
                        ActivityCart cart=(ActivityCart)context;
                        cart.refreshdata();
                        //notifyDataSetChanged();
                                //new getDataTask().execute();
                        }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                TextView textView=alert.findViewById(android.R.id.message);
                Typeface face= null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    face =context.getResources()
                            .getFont(R.font.roboto_medium);
                }
                textView.setTypeface(face);
            }
        });
        holder.txtMenuName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.confirm);
                builder.setMessage(context.getString(R.string.clear_one_order));
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dbhelper.deleteData(ActivityCart.Menu_ID.get(position));
                        ActivityCart.Menu_ID.remove(position);
                        notifyItemRemoved(position);
                        ActivityCart cart=(ActivityCart)context;
                        cart.refreshdata();
                        //clearData();
                        //new getDataTask().execute();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

                TextView textView=alert.findViewById(android.R.id.message);
                Typeface face= null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    face = context.getResources()
                            .getFont(R.font.roboto_medium);
                }
                textView.setTypeface(face);
            }
        });

        holder.imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //holder.txtQuantity.setText(String.valueOf(ActivityCart.Quantity.get(position)-1));
                try {
                    dbhelper.openDataBase();
                } catch (SQLException sqle) {
                    throw sqle;
                }

                Integer quantity =Integer.parseInt(holder.txtQuantity.getText().toString());
                Double price=(ActivityCart.Sub_total_price.get(position)/quantity);
                Long id= Long.valueOf(ActivityCart.Menu_ID.get(position));
                quantity=quantity-1;

                //if (dbhelper.isDataExist(id)) {
                dbhelper.updateData(id, quantity,(price * quantity));
                holder.txtQuantity.setText(""+quantity);
                //dbhelper.close();
                ActivityCart cart=(ActivityCart)context;
                cart.refreshdata();
            }
        });

        holder.imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //holder.txtQuantity.setText(String.valueOf(ActivityCart.Quantity.get(position)+1));

                try {
                    dbhelper.openDataBase();
                } catch (SQLException sqle) {
                    throw sqle;
                }

                Integer quantity =Integer.parseInt(holder.txtQuantity.getText().toString());
                Double price=(ActivityCart.Sub_total_price.get(position)/quantity);
                Long id= Long.valueOf(ActivityCart.Menu_ID.get(position));
                quantity=quantity+1;

                //if (dbhelper.isDataExist(id)) {
                    dbhelper.updateData(id, quantity,(price * quantity));

                holder.txtQuantity.setText(""+quantity);
                //dbhelper.close();
                ActivityCart cart=(ActivityCart)context;
                cart.refreshdata();
                //activity.invalidateOptionsMenu();
            }
        });

    }

    public void clearData() {
        ActivityCart.Menu_ID.clear();
        ActivityCart.Menu_name.clear();
        ActivityCart.Quantity.clear();
        ActivityCart.Sub_total_price.clear();
    }

    @Override
    public int getItemCount() {
        return ActivityCart.Menu_ID.size();
    }

}
