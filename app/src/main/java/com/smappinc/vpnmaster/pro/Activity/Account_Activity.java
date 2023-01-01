package com.smappinc.vpnmaster.pro.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.smappinc.vpnmaster.pro.R;

public class Account_Activity extends AppCompatActivity {

    ImageView back_icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        back_icon= findViewById(R.id.back_icon);

        back_icon.setOnClickListener(v -> onBackPressed());
    }
}