package com.smappinc.vpnmaster.pro.Application;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.smappinc.vpnmaster.pro.core.ProfileManager;



import java.util.Collections;
import java.util.List;

public class My_Application extends Application {

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        ProfileManager.init(getApplicationContext());
        System.loadLibrary("openconnect");
        System.loadLibrary("stoken");
        List<String> testDeviceIds = Collections.singletonList("304E9649693D185E5F687FBD6B6B7319");
        RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);

        MobileAds.initialize(getApplicationContext(), initializationStatus -> {
            MobileAds.setAppVolume(0.5f);
        });
    }

    public static Context getContext() {
        return mContext;
    }
}