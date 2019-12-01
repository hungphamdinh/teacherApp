package com.hungpham.teacherapp.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hungpham.teacherapp.Common.Common;
import com.hungpham.teacherapp.Interface.ItemClickListener;
import com.hungpham.teacherapp.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class StaffViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
    public TextView txtName,txtCourseName,txtDescript,txtEmail,txtSchedule;
    private CircleImageView profileImage;
    private ItemClickListener itemClickListener;

    public StaffViewHolder(View itemView) {
        super(itemView);
        txtName=(TextView)itemView.findViewById(R.id.txtUserNameMyCourse);
        txtEmail=(TextView)itemView.findViewById(R.id.txtEmailMyCourse);
        txtCourseName=(TextView)itemView.findViewById(R.id.txtTitleMyCourse);
//        txtDescript=(TextView)itemView.findViewById(R.id.txtCourseDescriptMyCourse);
        txtSchedule=(TextView)itemView.findViewById(R.id.txtScheduleMyCourse);
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
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select this action");
        contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
        contextMenu.add(0,1,getAdapterPosition(), Common.DELETE);

    }

}
