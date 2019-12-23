package com.hungpham.teacherapp.Adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hungpham.teacherapp.Common.Common;
import com.hungpham.teacherapp.Interface.ItemClickListener;
import com.hungpham.teacherapp.Model.Entities.Doc;
import com.hungpham.teacherapp.R;

import java.util.ArrayList;
import java.util.HashMap;


public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder>  {
    private Context context;
    private ArrayList<Doc> doc;
    private ArrayList<String> docKey;
    public TestAdapter(Context context, ArrayList<Doc> doc,ArrayList<String>docKey) {
        this.context = context;
        this.doc = doc;
        this.docKey =docKey;
    }
    public TestAdapter() {
    }
    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.test_layout, parent, false);
        TestViewHolder holder = new TestViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        holder.txtDocName.setText(doc.get(position).getDocName());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Uri uri = Uri.parse(doc.get(position).getDocUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });
        holder.imgBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog(docKey.get(position),holder);
            }
        });
    }

    private void deleteDialog(final String key,TestViewHolder holder) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Xóa");
        alertDialog.setMessage("Bạn có chắc muốn xóa?");
        //alertDialog.create();
        //alertDialog.show();
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                    DatabaseReference docRef = FirebaseDatabase.getInstance().getReference("Doc");
                    docRef.child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Doc doc=dataSnapshot.getValue(Doc.class);
                            if(doc.getStatus()==0){
                                holder.itemView.setVisibility(View.GONE);
                            }
                            else {
                                HashMap<String,Object>map=new HashMap<>();
                                map.put("status",0);
                                docRef.child(key).updateChildren(map);
                                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                dialogInterface.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return doc.size();
    }


    public class TestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtDocName,txtGoToCreate;
        private ImageView imgBtnDelete;
        private ItemClickListener itemClickListener;

        public TestViewHolder(View itemView) {
            super(itemView);
            txtDocName=(TextView)itemView.findViewById(R.id.txtTest);
            imgBtnDelete =(ImageView)itemView.findViewById(R.id.imgBtnDelete);
            itemView.setOnClickListener(this);
         //   itemView.setOnCreateContextMenuListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener=itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }

        //@Override
//        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
//            contextMenu.setHeaderTitle("Select this action");
//            contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
//            contextMenu.add(0,1,getAdapterPosition(), Common.DELETE);
//
//        }

    }

}












