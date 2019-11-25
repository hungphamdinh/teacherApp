package com.example.learnenglishta.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learnenglishta.Common.Common;
import com.example.learnenglishta.Interface.ItemClickListener;
import com.example.learnenglishta.Model.Chat;
import com.example.learnenglishta.Model.Course;
import com.example.learnenglishta.Model.Request;
import com.example.learnenglishta.Model.User;
import com.example.learnenglishta.R;
import com.example.learnenglishta.StudentDetailActivity;
import com.example.learnenglishta.ViewHolder.StaffViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


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
        holder.txtDescript.setText(course.get(position).getDescript());
        DatabaseReference coureRef=FirebaseDatabase.getInstance().getReference("Course");
        coureRef.orderByChild("tutorPhone").equalTo(course.get(position).getTutorPhone()).addValueEventListener(new ValueEventListener() {
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
                viewHolder.txtName.setText(student.getUsername());
                viewHolder.txtEmail.setText(student.getEmail());
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
        public TextView txtName, txtCourseName, txtDescript, txtEmail, txtSchedule;
        private ItemClickListener itemClickListener;

        public StaffViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtUserNameMyCourse);
            txtEmail = (TextView) itemView.findViewById(R.id.txtEmailMyCourse);
            txtCourseName = (TextView) itemView.findViewById(R.id.txtTitleMyCourse);
            txtDescript = (TextView) itemView.findViewById(R.id.txtCourseDescriptMyCourse);
            txtSchedule = (TextView) itemView.findViewById(R.id.txtScheduleMyCourse);
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