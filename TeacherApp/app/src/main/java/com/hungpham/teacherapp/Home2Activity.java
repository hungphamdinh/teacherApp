package com.hungpham.teacherapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.hungpham.teacherapp.Adapter.SectionsPageAdapter;
import com.hungpham.teacherapp.Common.Common;
import com.hungpham.teacherapp.Fragment.HomeFragment;
import com.hungpham.teacherapp.Fragment.Tab3Fragment;
import com.hungpham.teacherapp.Model.Entities.Tutor;
import com.hungpham.teacherapp.View.MyAccountView.MyAccountActivity;
import com.hungpham.teacherapp.View.MyCourseList.MyCourseFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hungpham.teacherapp.View.Login.LoginActivity2;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class Home2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private String userPhone = "";
    private CircleImageView profile;
    private TextView nav_user,nav_usermane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);
        database = FirebaseDatabase.getInstance();
        //      firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference = database.getReference("Category");
        //Paper.init(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        nav_user = (TextView)hView.findViewById(R.id.txtGmailProfile);
        profile=(CircleImageView) hView.findViewById(R.id.imageViewProfile);
        nav_usermane=(TextView)hView.findViewById(R.id.txtNameProfile);
        navigationView.setNavigationItemSelectedListener(this);
        if (Common.isConnectedToInternet(this)) {
            // loadMenu();
        } else {
            Toast.makeText(Home2Activity.this, "Check your connection", Toast.LENGTH_SHORT).show();
            return;
        }
        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        if (getIntent() != null)
            userPhone = getIntent().getStringExtra("phoneUser");
        if (!userPhone.isEmpty() && userPhone != null) {
            if (Common.isConnectedToInternet(this)) {
           //       setStatus("online");
                setupViewPager(mViewPager);
                setStatus();
                setProfileInform();

            } else {
                Toast.makeText(Home2Activity.this, "Check your connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        //setStatus("online");
    }

    private void setProfileInform() {
        DatabaseReference tutorRef= FirebaseDatabase.getInstance().getReference("Tutor");
        tutorRef.child(userPhone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Tutor tutor=dataSnapshot.getValue(Tutor.class);
                nav_user.setText(tutor.getEmail());
                Glide.with(getApplicationContext())
                        .load(tutor.getAvatar())
                        .centerCrop()
                        .into(profile);
                nav_usermane.setText(tutor.getUsername());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh) {
            // loadMenu();
        } else if (id == R.id.accountInform) {
            Intent intent=new Intent(Home2Activity.this, MyAccountActivity.class);
            intent.putExtra("phoneKey",userPhone);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {

        } else if (id == R.id.nav_feedback) {
            Uri uri = Uri.parse("https://forms.gle/zvgTLKy62G1Xb46B9");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (id == R.id.nav_signout) {
            Paper.book().destroy();
            Intent signIn = new Intent(Home2Activity.this, LoginActivity2.class);
            signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signIn);
            finish();
        }
//        else if (id == R.id.nav_share) {
//
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(Home2Activity.this, userPhone), "Trang chủ");
        adapter.addFragment(new MyCourseFragment(Home2Activity.this,userPhone), "Khóa học đang dạy");
        adapter.addFragment(new Tab3Fragment(), "TAB3");
        viewPager.setAdapter(adapter);

    }


    private void setStatus() {
        final DatabaseReference user = FirebaseDatabase.getInstance().getReference("Tutor");
        HashMap<String, Object> map = new HashMap<>();
        map.put("status", "online");
        HashMap<String, Object> offMap = new HashMap<>();
        offMap.put("status","offline");
        user.child(userPhone).onDisconnect().updateChildren(offMap);
        user.child(userPhone).updateChildren(map);
        //  user.child(phone).setValue(map);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        setStatus("offline");
        final DatabaseReference user = FirebaseDatabase.getInstance().getReference("Tutor");
        HashMap<String, Object> offMap = new HashMap<>();
        offMap.put("status","offline");
        user.child(userPhone).updateChildren(offMap);

    }
}
