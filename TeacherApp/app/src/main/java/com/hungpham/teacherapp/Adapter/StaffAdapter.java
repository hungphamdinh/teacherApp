package com.hungpham.teacherapp.Adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hungpham.teacherapp.Common.Common;
import com.hungpham.teacherapp.Interface.ItemClickListener;
import com.hungpham.teacherapp.Model.Entities.Course;
import com.hungpham.teacherapp.Presenter.MyCourseList.MyCourseListPresenter;
import com.hungpham.teacherapp.R;
import com.hungpham.teacherapp.View.LoadDetailMyCourse.StudentDetailActivity;
import com.hungpham.teacherapp.View.MyCourseList.IMyListCourseView;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder>implements IMyListCourseView {
    private Context context;
    private ArrayList<Course> course;
    private String userPhone;
    private MyCourseListPresenter myCourseListPresenter;
    private ArrayList<String>keys;
    public StaffAdapter(Context context, ArrayList<Course> course,String userPhone,ArrayList<String>keys) {
        this.context = context;
        this.course = course;
        this.userPhone=userPhone;
        this.keys=keys;
    }

    public StaffAdapter() {

    }

    @NonNull
    @Override
    public StaffAdapter.StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.my_course_layout, parent, false);
        StaffViewHolder holder = new StaffViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, int position) {
        myCourseListPresenter=new MyCourseListPresenter(this,holder);
            holder.txtCourseName.setText(course.get(position).getCourseName());
            holder.txtSchedule.setText(course.get(position).getSchedule());
            Glide.with(context.getApplicationContext())
                    .load(course.get(position).getImage())
                    .centerCrop()
                    // .placeholder(R.drawable.loading_spinner)
                    .into(holder.image);
            myCourseListPresenter.setCourseList(keys,position, userPhone,course);

    }

    @Override
    public int getItemCount() {
        return course.size();
    }



    @Override
    public void onDisplayStudent(HashMap<String, Object> map, StaffViewHolder holder) {
        holder.txtName.setText(map.get("studentName").toString());
        holder.txtEmail.setText(map.get("studentMail").toString());
        Glide.with(context.getApplicationContext())
                .load(map.get("studentImage"))
                .centerCrop()
                // .placeholder(R.drawable.loading_spinner)
                .into(holder.profileImage);

    }

    @Override
    public void onDisplayCourse(HashMap<String, Object> map, StaffViewHolder holder,int pos) {

    }

    @Override
    public void onDisplayOnline(String msg, StaffViewHolder holder) {
        holder.txtStatus.setText("Học viên hiện đang hoạt động");
        holder.txtStatus.setTextColor(Color.parseColor(	"#00FF00"));
        holder.imgStatus.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDisplayOffline(String msg, StaffViewHolder holder) {
        holder.txtStatus.setText("Học viên hiện không hoạt động");
        holder.txtStatus.setTextColor(Color.parseColor("#FF0000"));
        holder.imgStatus.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onNullItem(String msg, StaffViewHolder holder) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
        holder.itemView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoadDataToClick(ArrayList<String> dataList, StaffViewHolder holder) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,StudentDetailActivity.class);
                intent.putExtra("ChatId",dataList);
                context.startActivity(intent);
            }
        });
    }

    public class StaffViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtName, txtCourseName, txtStatus, txtEmail, txtSchedule;
        private ItemClickListener itemClickListener;
        private CircleImageView profileImage,imgStatus;
        private ImageView image;

        public StaffViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtUserNameMyCourse);
            txtEmail = (TextView) itemView.findViewById(R.id.txtEmailMyCourse);
            txtCourseName = (TextView) itemView.findViewById(R.id.txtTitleMyCourse);
            txtStatus = (TextView) itemView.findViewById(R.id.txtTutorStatus);
            txtSchedule = (TextView) itemView.findViewById(R.id.txtScheduleMyCourse);
            image=(ImageView)itemView.findViewById(R.id.imgMyCourse);
            imgStatus=(CircleImageView) itemView.findViewById(R.id.imgStatusTutor);
            profileImage=(CircleImageView)itemView.findViewById(R.id.imgProfileMyCourse);
            itemView.setOnClickListener(this);
//            itemView.setOnCreateContextMenuListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }

//        @Override
//        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
//            contextMenu.setHeaderTitle("Select this action");
//            contextMenu.add(0, 0, getAdapterPosition(), Common.UPDATE);
//            contextMenu.add(0, 1, getAdapterPosition(), Common.DELETE);
//
//        }

    }
}