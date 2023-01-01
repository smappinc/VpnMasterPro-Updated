package com.smappinc.vpnmaster.pro.Activity.Home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.smappinc.vpnmaster.pro.Activity.Account_Activity;
import com.smappinc.vpnmaster.pro.Fragment.Home_fragment_Class;
import com.smappinc.vpnmaster.pro.Premium.Premium_Class;
import com.smappinc.vpnmaster.pro.Proxy_Apps_Feature.ProxyActivity;
import com.smappinc.vpnmaster.pro.R;
import com.smappinc.vpnmaster.pro.Activity.Settings_Activity;


public class Home_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences home_activity_shared_pref;
    DrawerLayout mDrawer;
    private CardView premium_click;
    ImageView menu_btn;
    Toolbar toolbar;
   public static Boolean checkTimeV=false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_screen);

        init();
    }




    @SuppressLint("SetTextI18n")
    private void init() {
        home_activity_shared_pref = getSharedPreferences("DATA", MODE_PRIVATE);

        toolbar = initToolbar();
        mDrawer = findViewById(R.id.drawer_layout);
        premium_click = findViewById(R.id.premium_click);
        menu_btn = findViewById(R.id.menu);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        setUpNavView(toolbar, mDrawer);
        loadHomeFragment();

        premium_click.setOnClickListener(v -> {
            Premium_Class dialog = new Premium_Class(Home_Activity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialog.show();
        });

    }

    private Toolbar initToolbar() {
        return findViewById(R.id.toolbar);
    }

    @SuppressLint("NonConstantResourceId")
    private void setUpNavView(Toolbar main_toolbar, final DrawerLayout main_drawerLayout) {

        ActionBarDrawerToggle DrawerToggle = new ActionBarDrawerToggle(Home_Activity.this, main_drawerLayout, main_toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        main_drawerLayout.setDrawerListener(DrawerToggle);
        DrawerToggle.syncState();
    }

    private void loadHomeFragment() {

        if (!(Home_Activity.this.isFinishing())) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment content = fragmentManager.findFragmentById(R.id.frame);
            if (!(content instanceof Home_fragment_Class)) {
                final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Home_fragment_Class dummyFragment = Home_fragment_Class.newInstance();
                fragmentTransaction.replace(R.id.frame, dummyFragment);
                fragmentTransaction.commitNow();
            }
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.myAccount) {
            startActivity(new Intent(Home_Activity.this, Account_Activity.class));
        } else if (id == R.id.contact) {
            Intent intent = new Intent("android.intent.action.SEND");
            String[] recipients = {"bazuwee.com"};
            intent.putExtra(Intent.EXTRA_EMAIL, recipients);
            intent.putExtra(Intent.EXTRA_SUBJECT, "feedback For "+ getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_CC, "mailcc@gmail.com");
            intent.setType("text/html");
            intent.setPackage("com.google.android.gm");
            this.startActivity(Intent.createChooser(intent, "Send Email"));
        } else if (id == R.id.lisence)
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com")));
        else if (id == R.id.privacy) {
            //ProxyActivity
            startActivity(new Intent(Home_Activity.this, ProxyActivity.class));
        } else if (id == R.id.premium) {
            Premium_Class dialog = new Premium_Class(Home_Activity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialog.show();
        } else if (id == R.id.rateUs) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
        } else if (id == R.id.about) {
            startActivity(new Intent(Home_Activity.this, Settings_Activity.class));
        } else if (id == R.id.share) {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "share com");
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name) + getString(R.string.url));
            //shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_name) + getPackageName());
            startActivity(Intent.createChooser(shareIntent, "share_txt"));
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            setResult(Activity.RESULT_CANCELED);
            finishAffinity();
        }
    }

//    @Override
//    public void open_menu() {
//        if (!mDrawer.isDrawerOpen(GravityCompat.START)) {
//            mDrawer.openDrawer(GravityCompat.START);
//        } else {
//            mDrawer.closeDrawer(GravityCompat.END);
//        }
//    }
}
