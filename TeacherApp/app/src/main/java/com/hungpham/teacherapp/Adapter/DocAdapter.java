package com.hungpham.teacherapp.Adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hungpham.teacherapp.Common.Common;
import com.hungpham.teacherapp.Interface.ItemClickListener;
import com.hungpham.teacherapp.Model.Entities.Doc;
import com.hungpham.teacherapp.R;

import java.util.ArrayList;


public class DocAdapter extends RecyclerView.Adapter<DocAdapter.DocViewHolder>  {
    private Context context;
    private ArrayList<Doc> doc;
    public DatabaseReference courseRef;
    public FirebaseDatabase database;
    public DocAdapter(Context context, ArrayList<Doc> doc) {
        this.context = context;
        this.doc = doc;
    }
    public DocAdapter() {
    }
    @NonNull
    @Override
    public DocAdapter.DocViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.doc_layout, parent, false);
        DocViewHolder holder = new DocViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DocViewHolder holder, int position) {
        holder.txtDocName.setText(doc.get(position).getDocName());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Uri uri = Uri.parse(doc.get(position).getDocUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return doc.size();
    }


    public class DocViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView txtDocName;
        private ItemClickListener itemClickListener;

        public DocViewHolder(View itemView) {
            super(itemView);
            txtDocName=(TextView)itemView.findViewById(R.id.txtCourseDocDetail);
            itemView.setOnClickListener(this);
   //         itemView.setOnCreateContextMenuListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener=itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }

//        @Override
//        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
//            contextMenu.setHeaderTitle("Select this action");
//            contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
//            contextMenu.add(0,1,getAdapterPosition(), Common.DELETE);
//
//        }

    }

}


