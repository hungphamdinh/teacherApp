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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hungpham.teacherapp.Common.Common;
import com.hungpham.teacherapp.Interface.ItemClickListener;
import com.hungpham.teacherapp.Model.Course;
import com.hungpham.teacherapp.Model.Request;
import com.hungpham.teacherapp.Model.Tutor;
import com.hungpham.teacherapp.Model.User;
import com.hungpham.teacherapp.R;
import com.hungpham.teacherapp.StudentDetailActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder>  {
    private Context context;
    private ArrayList<Course> course;
    public static final int MSG_LEFT = 0;
    public DatabaseReference courseRef;
    public FirebaseDatabase database;
    private String userId;
    public static final int MSG_RIGHT = 1;
    private String userPhone;
    //private String imgUrl;
    public StaffAdapter(Context context, ArrayList<Course> course,String userPhone) {
        this.context = context;
        this.course = course;
        this.userPhone=userPhone;
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
        holder.txtCourseName.setText(course.get(position).getCourseName());
        holder.txtSchedule.setText(course.get(position).getSchedule());
        Glide.with(context)
                .load(course.get(position).getImage())
                .centerCrop()
                // .placeholder(R.drawable.loading_spinner)
                .into(holder.image);
        DatabaseReference coureRef=FirebaseDatabase.getInstance().getReference("Course");
        coureRef.orderByChild("tutorPhone").equalTo(course.get(position).getTutorPhone()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot courseSnapshot:dataSnapshot.getChildren()){
                    String key=courseSnapshot.getKey();
                    checkRequest(key,holder);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return course.size();
    }
    private void checkRequest(String courseID, StaffViewHolder viewHolder){
        DatabaseReference requestRef=FirebaseDatabase.getInstance().getReference("Requests");
        requestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot childRequest:dataSnapshot.getChildren()){
                    Request request=childRequest.getValue(Request.class);
                    if(request.getCourseId().equals(courseID)){
                        loadStudent(request.getPhone(),viewHolder);
                        onClickItem(request, viewHolder);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void loadStudent(String studentPhone, StaffViewHolder viewHolder){
        DatabaseReference studentRef=FirebaseDatabase.getInstance().getReference("User");
        studentRef.child(studentPhone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User student=dataSnapshot.getValue(User.class);
                if(student.getStatus().equals("offline")){
                    viewHolder.txtStatus.setText("Học viên hiện không hoạt động");
                    viewHolder.txtStatus.setTextColor(Color.parseColor("#FF0000"));
                    viewHolder.imgStatus.setVisibility(View.INVISIBLE);
                }
                else {
                    viewHolder.txtStatus.setText("Học viên hiện đang hoạt động");
                    viewHolder.txtStatus.setTextColor(Color.parseColor(	"#00FF00"));
                    viewHolder.imgStatus.setVisibility(View.VISIBLE);
                }
                viewHolder.txtName.setText(student.getUsername());
                viewHolder.txtEmail.setText(student.getEmail());
                Glide.with(context)
                        .load(student.getAvatar())
                        .centerCrop()
                       // .placeholder(R.drawable.loading_spinner)
                        .into(viewHolder.profileImage);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public class ChatViewHolder extends RecyclerView.ViewHolder {
        public TextView showMessage;
        public ImageView profileImage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            showMessage = (TextView) itemView.findViewById(R.id.showMessage);
            profileImage = (ImageView) itemView.findViewById(R.id.profileImage);
        }
    }
    private void onClickItem(Request request, StaffViewHolder viewHolder) {
        viewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent intent=new Intent(context, StudentDetailActivity.class);
                String studentID=request.getPhone();
                String userID=userPhone;
                ArrayList<String> listIntent=new ArrayList<>();
                listIntent.add(studentID);
                listIntent.add(userID);
                listIntent.add(request.getCourseId());
                intent.putStringArrayListExtra("ChatID",listIntent);
                //intent.putExtra("tutorID",adapter.getRef(position).getKey());
               context.startActivity(intent);                    }
        });
    }
    public class StaffViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
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
            itemView.setOnCreateContextMenuListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select this action");
            contextMenu.add(0, 0, getAdapterPosition(), Common.UPDATE);
            contextMenu.add(0, 1, getAdapterPosition(), Common.DELETE);

        }

    }
}