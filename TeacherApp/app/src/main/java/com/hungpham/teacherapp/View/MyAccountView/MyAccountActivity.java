package com.hungpham.teacherapp.View.MyAccountView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.hungpham.teacherapp.Common.Common;
import com.hungpham.teacherapp.Presenter.MyAccount.MyAccountPresenter;
import com.hungpham.teacherapp.R;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAccountActivity extends AppCompatActivity implements MyAccountView{
    private EditText edtPass, edtUsername, edtProfile;
    private Button btnUpdate,btnChooseFile;
    private EditText edtEmail;
    private CircleImageView profile;
    private String phoneKey;
    private Uri imageUri;
    private ProgressDialog progressDialog;
    private MyAccountPresenter myAccountPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        edtUsername = (EditText)findViewById(R.id.edtNameU);
        edtPass = (EditText)findViewById(R.id.edtPassU);
        edtEmail = (EditText)findViewById(R.id.edtEmailtU);
        btnUpdate = (Button)findViewById(R.id.btnUpdate);
        profile=(CircleImageView)findViewById(R.id.imgProfileMyAccount);
        btnUpdate.setEnabled(false);
        myAccountPresenter=new MyAccountPresenter(this);
        HashMap<String,Object>edtMap=new HashMap<>();
        if (getIntent() != null)
            phoneKey = getIntent().getStringExtra("phoneKey");
        if (!phoneKey.isEmpty() && phoneKey != null) {
            if (Common.isConnectedToInternet(this)) {
                edtMap.put("userName",edtUsername.getText().toString());
                edtMap.put("password",edtPass.getText().toString());
                edtMap.put("email",edtEmail.getText().toString());
                edtMap.put("imageUri",imageUri);
                myAccountPresenter.loadData(phoneKey,edtMap);
            } else {
                Toast.makeText(MyAccountActivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        setOnClick();
        onClickChooseImage();

    }

    private void setOnClick() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,Object>edtMap=new HashMap<>();
                edtMap.put("userName",edtUsername.getText().toString());
                edtMap.put("password",edtPass.getText().toString());
                edtMap.put("email",edtEmail.getText().toString());
                edtMap.put("imageUri",imageUri);
                progressDialog=getProgress();
                myAccountPresenter.setOnClick(phoneKey,edtMap);
            }
        });
    }

    private void onClickChooseImage() {
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        //        if(ContextCompat.checkSelfPermission(MyAccountActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                chooseImage();
                btnUpdate.setEnabled(true);
       //         }
//            else{
//                ActivityCompat.requestPermissions(MyAccountActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
//
//            }

            }
        });
    }


    private ProgressDialog getProgress() {
        progressDialog=new ProgressDialog(MyAccountActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Đang tải...");
        progressDialog.setProgress(0);
        progressDialog.show();
        return progressDialog;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(MyAccountActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
    private void chooseImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), Common.PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if(requestCode==9&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
//        {
            chooseImage();
//        }
//        else
//            Toast.makeText(MyAccountActivity.this,"Provide pemrission",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Common.PICK_IMAGE_REQUEST&&resultCode==RESULT_OK&&data!=null){
            imageUri=data.getData();
//            edtProfile.setText(data.getData().getLastPathSegment());
        }
        else{
            Toast.makeText(MyAccountActivity.this,"Chọn file mà bạn muốn",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updateInform(HashMap<String, Object> informMap) {
        edtUsername.setText(informMap.get("userName").toString());
        edtEmail.setText(informMap.get("mail").toString());
        edtPass.setText(informMap.get("pass").toString());
        Glide.with(getApplicationContext())
                .load(informMap.get("avatar"))
                .centerCrop()
                .into(profile);
    }

    @Override
    public void onSuccess(String msg) {
        progressDialog.cancel();
        Toast.makeText(MyAccountActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String msg) {
        progressDialog.cancel();
        Toast.makeText(MyAccountActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoading(int percent) {
//        progressDialog.cancel();
        progressDialog.setProgress(percent);
    }
}
