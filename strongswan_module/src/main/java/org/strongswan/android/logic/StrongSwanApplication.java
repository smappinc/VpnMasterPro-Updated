package org.strongswan.android.logic;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Build;

import org.strongswan.android.security.LocalCertificateKeyStoreProvider;

import java.security.Security;

public class StrongSwanApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    static {
        Security.addProvider(new LocalCertificateKeyStoreProvider());
    }

    @SuppressLint("HardwareIds")
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return StrongSwanApplication.mContext;
    }

    static {
        System.loadLibrary("opvpnutil");


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            System.loadLibrary("strongswan");
            System.loadLibrary("tpmtss");
            System.loadLibrary("tncif");
            System.loadLibrary("tnccs");
            System.loadLibrary("imcv");
            System.loadLibrary("charon");
            System.loadLibrary("ipsec");
        }

        System.loadLibrary("androidbridge");
    }
}
