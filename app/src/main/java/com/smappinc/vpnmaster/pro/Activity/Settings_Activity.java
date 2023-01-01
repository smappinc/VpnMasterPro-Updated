package com.smappinc.vpnmaster.pro.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;


import com.smappinc.vpnmaster.pro.R;

public class Settings_Activity extends AppCompatActivity {

    ImageView cross_icon;
    Button privacyPolicy,aboutUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init_data();
    }

    private void init_data() {
        cross_icon = findViewById(R.id.cross_icon);
        cross_icon.setOnClickListener(v -> onBackPressed());

        privacyPolicy = findViewById(R.id.privacyPolicy);
        aboutUs = findViewById(R.id.aboutUs);

        privacyPolicy.setOnClickListener(v -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/document/d/1TkhxRrgXVBJgFttPNi1p-5_iugmkKGUyUD4ngGDkCGk/edit?usp=sharing")));
        });

        aboutUs.setOnClickListener(v -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
        });
    }
}