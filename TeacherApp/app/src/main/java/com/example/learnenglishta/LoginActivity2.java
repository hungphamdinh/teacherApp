package com.example.learnenglishta;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learnenglishta.Common.Common;
import com.example.learnenglishta.Model.Tutor;
import com.example.learnenglishta.SQliteDatabase.BaseResipistory;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;
import com.sinch.android.rtc.SinchError;

import java.io.IOException;

import io.paperdb.Paper;


public class LoginActivity2 extends BaseActivity implements SinchService.StartFailedListener {
    private EditText username,password;
    private Button login;
    private TextView txtSignUp;
    private Cursor c=null;
    private DatabaseReference tutorRef =null;
    private FirebaseDatabase firebaseDatabase=null;
    private CheckBox ckbRemember;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        username = (EditText)findViewById(R.id.edtPhoneLogin);
        password= (EditText)findViewById(R.id.edtPassword);
        ckbRemember=(CheckBox) findViewById(R.id.ckbRememberUser);
        login= (Button)findViewById(R.id.btnLogin);
        txtSignUp=(TextView)findViewById(R.id.txtSignUpNewAc);
        setupUI(findViewById(R.id.parent));
        firebaseDatabase=FirebaseDatabase.getInstance();
        tutorRef =firebaseDatabase.getReference("Tutor");
        Firebase.setAndroidContext(LoginActivity2.this);
        SignUp();
//      ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
//        readFromAssets();
        Paper.init(this);
        Login(tutorRef);
//        String userRemember=Paper.book().read(Common.USER_KEY);
//        String passRemember=Paper.book().read(Common.PWD_KEY);
//        if(userRemember!=null&&passRemember!=null) {
//            if (!userRemember.isEmpty() && !passRemember.isEmpty())
//                loginRemember(userRemember, passRemember);
//        }
        readFromAssets();
    }

    private void loginRemember(final String userRemember, final String passRemember) {
        if (Common.isConnectedToInternet(getBaseContext())) {
            final ProgressDialog progress = getProgressDialog();
            tutorRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (userRemember.equals("") || passRemember.equals("")) {
                        progress.dismiss();
                        Toast.makeText(LoginActivity2.this, "Please check your username and password", Toast.LENGTH_SHORT).show();
                    } else {
                        if (dataSnapshot.child(userRemember).exists()) {
                            Tutor tutor = dataSnapshot.child(userRemember).getValue(Tutor.class);
                            tutor.setEmail(userRemember);
                            if (tutor.getPassword().equals(passRemember)) {
                                progress.dismiss();
                                Intent intent = new Intent(LoginActivity2.this, Home2Activity.class);
                                intent.putExtra("phoneUser",userRemember);
//                                Intent intentPhoneNumber=new Intent(LoginActivity2.this,MyAccountActivity.class);
//                                intentPhoneNumber.putExtra("phoneNum",userRemember);
                                Common.currentUser = tutor;
                                startActivity(intent);
                                finish();
                            } else {
                                progress.dismiss();
                                Toast.makeText(LoginActivity2.this, "Please check your username and password", Toast.LENGTH_SHORT).show();
                                //  username.setText(uUser.getPassword());
                            }
                        } else {
                            progress.dismiss();
                            Toast.makeText(LoginActivity2.this, "This account is not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else {
            Toast.makeText(LoginActivity2.this,"Check your connection",Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private ProgressDialog getProgressDialog() {
        final ProgressDialog progress = new ProgressDialog(LoginActivity2.this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        return progress;
    }


    private void SignUp() {
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity2.this, SignUpActivity.class));
               // finish();
            }
        });
    }


    public void Login(final DatabaseReference table_user) {
        login.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {

                if (Common.isConnectedToInternet(getBaseContext())) {
                    if(ckbRemember.isChecked()){
                        Paper.book().write(Common.USER_KEY, username.getText().toString());
                        Paper.book().write(Common.PWD_KEY,password.getText().toString());
                    }
                    progressDialog = getProgressDialog();
                    final String userNameTemp = username.getText().toString();
                    final String passwordTemp = password.getText().toString();
                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (userNameTemp.equals("") || passwordTemp.equals("")) {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity2.this, "Please check your username and password", Toast.LENGTH_SHORT).show();
                            } else {
                                if (dataSnapshot.child(username.getText().toString()).exists()) {
                                    Tutor tutor = dataSnapshot.child(username.getText().toString()).getValue(Tutor.class);
                                    //tutor.setPhone(username.getText().toString());

                                    if (tutor.getPassword().equals(password.getText().toString())) {
                                        if (!getSinchServiceInterface().isStarted()) {
                                            getSinchServiceInterface().startClient(userNameTemp);
                                            progressDialog = getProgressDialog();
                                        }
                                        Intent intent = new Intent(LoginActivity2.this, Home2Activity.class);
                                        intent.putExtra("phoneUser",userNameTemp);
                                        Common.currentUser = tutor;
                                        startActivity(intent);
                                        finish();
                                        Toast.makeText(LoginActivity2.this,"Log in successfully",Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(LoginActivity2.this, "Please check your username and password", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity2.this, "This account is not exist", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    Toast.makeText(LoginActivity2.this,"Check your connection",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
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
                    hideSoftKeyboard(LoginActivity2.this);
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
    private void readFromAssets() {
        BaseResipistory myDbHelper = new BaseResipistory(LoginActivity2.this);
        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
        myDbHelper.openDataBase();
        //     Toast.makeText(LoginActivity.this, "Successfully Imported", Toast.LENGTH_SHORT).show();
        c = myDbHelper.query("OrderDetail", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
//                Toast.makeText(LoginActivity.this,
//                        "ID"+c.getInt(0)+"\n"+
//                                "ItemId: " + c.getString(1) + "\n" +
//                                "ItemName: " + c.getString(2) + "\n" +
//                                "Quantity: " + c.getString(3) + "\n" +
//                                "Price:  " + c.getString(4)+"\n"+
//                                "Discount: "+c.getString(5),
//                        Toast.LENGTH_LONG).show();
            } while (c.moveToNext());
        }
    }




    @Override
    protected void onServiceConnected() {
        login.setEnabled(true);
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    protected void onPause() {
//        if (progressDialog != null) {
//            progressDialog.dismiss();
//        }
        super.onPause();
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onStarted() {
//        openPlaceCallActivity();
    }



}