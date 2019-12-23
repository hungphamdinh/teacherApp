package com.hungpham.teacherapp.View.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hungpham.teacherapp.Sinch.BaseActivity;
import com.hungpham.teacherapp.Home2Activity;
import com.hungpham.teacherapp.Presenter.Login.ILoginPresenter;
import com.hungpham.teacherapp.Presenter.Login.LoginPresenter;
import com.hungpham.teacherapp.R;
import com.hungpham.teacherapp.SignUpActivity;
import com.hungpham.teacherapp.Sinch.SinchService;
import com.rey.material.widget.CheckBox;
import com.sinch.android.rtc.SinchError;

import io.paperdb.Paper;


public class LoginActivity2 extends BaseActivity implements SinchService.StartFailedListener, ILoginView {
    private EditText username,password;
    private Button login;
    private TextView txtSignUp;
    private Cursor c=null;
    private ProgressDialog progressDialog;
    private ILoginPresenter loginPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        username = (EditText)findViewById(R.id.edtPhoneLogin);
        password= (EditText)findViewById(R.id.edtPassword);
        login= (Button)findViewById(R.id.btnLogin);
        // setupUI(findViewById(R.id.parent));
     //   SignUp();
//        Paper.init(this);
        loginPresenter=new LoginPresenter(this,this);
        onLogin();
    }

    private void onLogin() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog=getProgressDialog();
                loginPresenter.onLogin(username.getText().toString(),password.getText().toString());
            }
        });
    }


//
//    public static void hideSoftKeyboard(Activity activity) {
//        InputMethodManager inputMethodManager =
//                (InputMethodManager) activity.getSystemService(
//                        Activity.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(
//                activity.getCurrentFocus().getWindowToken(), 0);
//    }
//    public void setupUI(View view) {
//
//        // Set up touch listener for non-text box views to hide keyboard.
//        if (!(view instanceof EditText)) {
//            view.setOnTouchListener(new View.OnTouchListener() {
//                public boolean onTouch(View v, MotionEvent event) {
//                    hideSoftKeyboard(LoginActivity2.this);
//                    return false;
//                }
//            });
//        }
//
////        If a layout container, iterate over children and seed recursion.
//        if (view instanceof ViewGroup) {
//            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
//                View innerView = ((ViewGroup) view).getChildAt(i);
//                setupUI(innerView);
//            }
//        }
//    }



    @Override
    protected void onServiceConnected() {
        login.setEnabled(true);
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    protected void onPause() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
        super.onPause();
    }
    private ProgressDialog getProgressDialog() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        return progress;
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


    @Override
    public void setDisplaySuccess(String msg) {
        progressDialog.cancel();
        Toast.makeText(LoginActivity2.this,msg,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity2.this, Home2Activity.class);
        intent.putExtra("phoneUser",username.getText().toString());
        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(username.getText().toString());
        }
        startActivity(intent);
    }

    @Override
    public void setDisplayError(String msg) {
        progressDialog.cancel();
        Toast.makeText(LoginActivity2.this,msg,Toast.LENGTH_SHORT).show();
    }
}
