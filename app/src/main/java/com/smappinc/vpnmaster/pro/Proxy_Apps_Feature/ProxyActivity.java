package com.smappinc.vpnmaster.pro.Proxy_Apps_Feature;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.smappinc.vpnmaster.pro.R;

public class ProxyActivity extends AppCompatActivity {

    Button appSelection;
    ImageView cross_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxy);

        init_proxy();
    }

    private void init_proxy() {
        appSelection = findViewById(R.id.appSelection);
        cross_icon = findViewById(R.id.cross_icon);

        appSelection.setOnClickListener(v -> {
            startActivity(new Intent(ProxyActivity.this , Proxy_Apps_Screen.class));
        });

        cross_icon.setOnClickListener(v -> onBackPressed());
    }
}