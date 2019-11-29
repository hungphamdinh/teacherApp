package com.hungpham.teacherapp.Fragment;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hungpham.teacherapp.R;


/**
 * Created by User on 2/28/2017.
 */

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private Context context;
    private Button btnStart;
    private String userPhone;
    public HomeFragment(){}
    public HomeFragment(Context context,String userPhone){
        this.context=context;
        this.userPhone=userPhone;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        btnStart = (Button) view.findViewById(R.id.btnStart);
        String phone=userPhone;
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent=new Intent(context,LoginActivity.class);
               // intent.putExtra("userPhone",phone);
               // startActivity(intent);
            }
        });

        return view;
    }
}
