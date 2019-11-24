package com.example.learnenglishta.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.learnenglishta.Common.Common;
import com.example.learnenglishta.Interface.ItemClickListener;
import com.example.learnenglishta.R;

public class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {
    public TextView txtName,txtPrice,txtDescript, txtTutorName;
    private ItemClickListener itemClickListener;
    public CourseViewHolder(View itemView) {
        super(itemView);
        txtName=(TextView)itemView.findViewById(R.id.txtCourseName);
        txtPrice=(TextView)itemView.findViewById(R.id.txtCoursePrice);
        txtDescript=(TextView)itemView.findViewById(R.id.txtCourseDescript);
        txtTutorName =(TextView)itemView.findViewById(R.id.txtTutorName);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }
    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }
    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select this action");
        menu.add(0,0,getAdapterPosition(), Common.UPDATE);
        menu.add(0,1,getAdapterPosition(), Common.DELETE);

    }
}
