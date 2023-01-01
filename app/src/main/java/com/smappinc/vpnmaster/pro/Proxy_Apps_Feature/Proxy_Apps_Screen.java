package com.smappinc.vpnmaster.pro.Proxy_Apps_Feature;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smappinc.vpnmaster.pro.R;
import com.sdsmdg.tastytoast.TastyToast;

import static com.smappinc.vpnmaster.pro.Util.Constant.appInfos;


public class Proxy_Apps_Screen extends AppCompatActivity {

    Dialog dialog;
    ProgressBar progressBar;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proxy_apps_feature);

        RecyclerView mRecyclerView = findViewById(R.id.proxy_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Proxy_Apps_Screen.this);
        mRecyclerView.setLayoutManager(layoutManager);

        start_dialog();
        if (appInfos != null && !appInfos.isEmpty()) {
            Log.d("app_info", "" + appInfos.size());
            RecyclerView.Adapter<InstalledAppsAdapter.ViewHolder> mAdapter = new InstalledAppsAdapter(Proxy_Apps_Screen.this, appInfos);
            mRecyclerView.setAdapter(mAdapter);

            stop_dialog();
        } else {
            stop_dialog();
            TastyToast.makeText(Proxy_Apps_Screen.this, "No Apps Found", Toast.LENGTH_LONG  , TastyToast.INFO);
        }
        back.setOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void start_dialog() {
        dialog = new Dialog(Proxy_Apps_Screen.this);
        dialog.setContentView(R.layout.getting_all_proxy_dialog);
        progressBar = dialog.findViewById(R.id.imageDialog);
        back = findViewById(R.id.proxy_apps_back_pressed);
        progressBar.setVisibility(View.VISIBLE);
        dialog.setCancelable(false);

        if (!(isFinishing())) {
            //show dialog
            dialog.show();
        }
    }

    public void stop_dialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            if (progressBar != null) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }
}

