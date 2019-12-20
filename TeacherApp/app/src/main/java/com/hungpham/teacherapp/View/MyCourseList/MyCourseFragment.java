package com.hungpham.teacherapp.View.MyCourseList;


import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hungpham.teacherapp.Adapter.StaffAdapter;
import com.hungpham.teacherapp.Model.Entities.Course;
import com.hungpham.teacherapp.Presenter.MyCourseList.MyCourseListAdapterPre;
import com.hungpham.teacherapp.R;
import com.hungpham.teacherapp.View.MyCourseList.IMyCourseAdapterView;
import com.hungpham.teacherapp.ViewHolder.StaffViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by User on 2/28/2017.
 */

public class MyCourseFragment extends Fragment implements IMyCourseAdapterView {
    private Context context;
    private String userPhone;
    public MyCourseFragment(){}
    public MyCourseFragment(Context context, String userPhone){
        this.context=context;
        this.userPhone=userPhone;
    }
    private static final String TAG = "MyCourseFragment";
    private RecyclerView recyclerMenu;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference courseRef;
    private FirebaseDatabase database;
    private MyCourseListAdapterPre myCourseListAdapterPre;
    private FirebaseRecyclerAdapter<Course, StaffViewHolder> adapter;
    private StaffAdapter staffAdapter;
    private ArrayList<Course> courseList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_course,container,false);
        database= FirebaseDatabase.getInstance();
        courseRef =database.getReference("Course");
        recyclerMenu=(RecyclerView)view.findViewById(R.id.listOrderRecycler);
        recyclerMenu.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(getContext());
        recyclerMenu.setLayoutManager(layoutManager);
        myCourseListAdapterPre=new MyCourseListAdapterPre(this);
        myCourseListAdapterPre.setAdapter(userPhone);
        //loadListTutor();
        return view;
    }

    @Override
    public void callAdapter(ArrayList<Course> courses,ArrayList<String> keys) {
        staffAdapter=new StaffAdapter(context,courses,userPhone,keys);
        staffAdapter.notifyDataSetChanged();
        recyclerMenu.setAdapter(staffAdapter);
    }

    @Override
    public void ơnError(String msg) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
        recyclerMenu.setVisibility(View.INVISIBLE);
    }
}
