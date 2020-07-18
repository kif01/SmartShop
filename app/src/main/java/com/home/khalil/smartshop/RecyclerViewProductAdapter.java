package com.home.khalil.smartshop;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.home.khalil.smartshop.MainActivity.amount;
import static com.home.khalil.smartshop.MainActivity.empty;
import static com.home.khalil.smartshop.MainActivity.pay;

public class RecyclerViewProductAdapter extends RecyclerView.Adapter<RecyclerViewProductAdapter.ViewProductHolder> {


    public ArrayList<Product> productList;
    private Context context;
    int qty;
    int n;


    public RecyclerViewProductAdapter(ArrayList<Product> productList) {
       this.productList=productList;

    }
    @Override
    public ViewProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, null);
        context = parent.getContext();
        return new ViewProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewProductHolder holder, final int position) {
        String title = productList.get(position).getTitle();
        int price = productList.get(position).getPrice();
        qty = productList.get(position).getQuantity();
        Log.d("Price","hey: "+price);
        String imageUri = productList.get(position).getImage();

        holder.title.setText(title);
        holder.price.setText(price+"");
        Picasso.with(context).load(imageUri).into(holder.image);
        holder.qty.setText(qty+"");

        n= Integer.parseInt(String.valueOf(amount.getText()));

        holder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qty = productList.get(position).getQuantity();
                qty++;
                productList.get(position).setQuantity(qty);
                holder.qty.setText(qty+"");

                n+=productList.get(position).getPrice();
                amount.setText(n+"");
                holder.decrease.setVisibility(View.VISIBLE);


            }
        });

        holder.decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qty = productList.get(position).getQuantity();
                qty--;
                productList.get(position).setQuantity(qty);
                holder.qty.setText(qty+"");

                n-=productList.get(position).getPrice();
                amount.setText(n+"");

                if (qty==1){
                    holder.decrease.setVisibility(View.INVISIBLE);
                }
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int q= productList.get(position).getQuantity();
                int p = productList.get(position).getPrice();
                n-=p*q;
                amount.setText(n+"");
                productList.remove(position);
                notifyDataSetChanged();
                if(productList.size()==0){
                    empty.setVisibility(View.VISIBLE);
                    pay.setVisibility(View.INVISIBLE);

                }
            }
        });
    }





    @Override
    public int getItemCount() {
        return this.productList.size();
    }

    public class ViewProductHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView price;
        ImageView image;
        TextView qty;
        ImageButton increase;
        ImageButton decrease;
        FloatingActionButton remove;

        public ViewProductHolder(View itemView){
            super(itemView);

            title= (TextView) itemView.findViewById(R.id.product_title);
            price = (TextView) itemView.findViewById(R.id.product_price);
            image = (ImageView) itemView.findViewById(R.id.product_image);
            qty = (TextView) itemView.findViewById(R.id.product_qty);
            increase = (ImageButton) itemView.findViewById(R.id.increase);
            decrease = (ImageButton) itemView.findViewById(R.id.decrease);
            remove = (FloatingActionButton) itemView.findViewById(R.id.remove_product);

        }
}





}
