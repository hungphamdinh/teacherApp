package com.example.learnenglishta.ViewHolder;

import android.content.Context;
import android.graphics.Color;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.learnenglishta.Common.Common;
import com.example.learnenglishta.Interface.ItemClickListener;
import com.example.learnenglishta.Model.Order;
import com.example.learnenglishta.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener
{
    public TextView txtItemName,txtPrice;
    public ImageView imageCartCount;
    private ItemClickListener itemClickListener;
    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txtItemName=(TextView)itemView.findViewById(R.id.txtCartItemName);
        txtPrice=(TextView)itemView.findViewById(R.id.txtCartItemPrice);
        imageCartCount=(ImageView)itemView.findViewById(R.id.itemCartCount);
      //  itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }
    public void setTxtCartName(ImageView imageCartCount){
        this.imageCartCount=imageCartCount;
    }
    @Override
    public void onClick(View v) {
     //   itemClickListener.onClick(v,getAdapterPosition(),false);
    }
    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select this action");
        contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
        contextMenu.add(0,1,getAdapterPosition(), Common.DELETE);
    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{
    ArrayList<Order> listData;
    Context context;

    public CartAdapter(ArrayList<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View itemView=inflater.inflate(R.layout.cart_layout,viewGroup,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i) {
        TextDrawable drawable= TextDrawable.builder()
                .buildRound(""+listData.get(i).getSchedule(), Color.RED);
        cartViewHolder.imageCartCount.setImageDrawable(drawable);
        Locale locale=new Locale("en","US");//change country language
        NumberFormat fmt= NumberFormat.getCurrencyInstance(locale);//format and parse numbers for any locale
        int price=(Integer.parseInt(listData.get(i).getPrice()));
        cartViewHolder.txtPrice.setText(fmt.format(price));
        cartViewHolder.txtItemName.setText(listData.get(i).getCourseName());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
