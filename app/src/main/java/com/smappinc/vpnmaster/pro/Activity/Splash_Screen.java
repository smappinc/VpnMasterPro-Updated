package com.smappinc.vpnmaster.pro.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.onesignal.OneSignal;
import com.smappinc.vpnmaster.pro.Activity.Home.Home_Activity;
import com.smappinc.vpnmaster.pro.Api_Fetch_Service.Fetch_Service;

import com.smappinc.vpnmaster.pro.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.sdsmdg.tastytoast.TastyToast;



import static com.smappinc.vpnmaster.pro.Util.Constant.ADMOB_INTERSETIAL_AD;
import static com.smappinc.vpnmaster.pro.Util.Constant.IS_ADMOB_ENABLE;
import static com.smappinc.vpnmaster.pro.Util.Constant.appInfos;
import static com.smappinc.vpnmaster.pro.Util.Constant.isNetworkAvailable;
import static com.smappinc.vpnmaster.pro.Util.Constant.set_status_bar;
import static com.smappinc.vpnmaster.pro.Util.Constant.vpn_connection_status;
import static com.smappinc.vpnmaster.pro.core.VPNLog.TAG;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class Splash_Screen extends AppCompatActivity {



    SharedPreferences splash_sp;
    private BillingClient billingClient;
    InterstitialAd sInterstitial;
    public static int UPDATE_CODE = 22;
    AppUpdateManager appUpdateManager;
    private static final String ONESIGNAL_APP_ID = "efd19db8-22ca-4fc7-9f37-750f85d15b4a";
    public static Boolean checkPrimumUsers=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //MobileAds Initialization
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
                for (String adapterClass : statusMap.keySet()) {
                    AdapterStatus status = statusMap.get(adapterClass);
                    Log.d("VPMMaster", String.format(
                            "Adapter name: %s, Description: %s, Latency: %d",
                            adapterClass, status.getDescription(), status.getLatency()));

                }
            }
        });

        //interstitial ads
        loadinterstitialAd();

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        


        LocalBroadcastManager.getInstance(Splash_Screen.this).registerReceiver(data_fetch, new IntentFilter("all_data_fetched"));
        set_status_bar(Splash_Screen.this);

        
        inAppUp();
        init();
    }

    private void inAppUp() {

        appUpdateManager = AppUpdateManagerFactory.create(this);
        Task<AppUpdateInfo> task = appUpdateManager.getAppUpdateInfo();
        task.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {

                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){

                    try {
                        appUpdateManager.startUpdateFlowForResult(appUpdateInfo,AppUpdateType.IMMEDIATE,
                        Splash_Screen.this,
                        UPDATE_CODE);
                    } catch (IntentSender.SendIntentException exception) {
                        exception.printStackTrace();
                    }
                }

            }
        });
        appUpdateManager.registerListener(listener);
    }

    InstallStateUpdatedListener listener = installState -> {
        if (installState.installStatus() == InstallStatus.DOWNLOADED){
            popUp();
        }
    };

    private void popUp() {

        Snackbar snackbar = Snackbar.make(
                findViewById(android.R.id.content),
                "App Update Almost Done.",
                Snackbar.LENGTH_INDEFINITE
        );

        snackbar.setAction("Reload", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.setTextColor(Color.parseColor("#FF0000"));
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_CODE) {
            if (resultCode != RESULT_OK) {
            }
        }
    }

    private void init() {
        splash_sp = getSharedPreferences("DATA", MODE_PRIVATE);
        init_billing();
        isUserHasSubscription();

        run_next();
    }

    private void run_next() {
        if (isNetworkAvailable(Splash_Screen.this)) {
            if (vpn_connection_status()) {
               /* load_intersetial_ad(ADMOB_INTERSETIAL_AD);*/

                new Handler().postDelayed(this::showInterstitial, 1500);
            } else {
             //   move_to_home();

                start_service();
            }
        } else {
            TastyToast.makeText(Splash_Screen.this, "Internet Is Required!", TastyToast.LENGTH_LONG, TastyToast.INFO);
        }
    }

    //billing work
    private void init_billing() {
        billingClient = BillingClient.newBuilder(this).setListener((billingResult, list) -> {

            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                for (Purchase purchase : list) {
                    handlePurchase(purchase);
                }
            }
        }).enablePendingPurchases().build();
    }

    private void handlePurchase(Purchase purchase) {

        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {

            AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener = billingResult -> {
            };

            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();

                billingClient.acknowledgePurchase(acknowledgePurchaseParams,
                        acknowledgePurchaseResponseListener);
            }
        }
    }

    private void isUserHasSubscription() {

        if (billingClient == null) {
            billingClient = BillingClient.newBuilder(Splash_Screen.this)
                    .enablePendingPurchases()
                    .build();
        }

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {

                final Purchase.PurchasesResult purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.SUBS);

                billingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS, (billingResult1, list) -> {

                    if (billingResult1.getResponseCode() == BillingClient.BillingResponseCode.OK && !Objects.requireNonNull(purchasesResult.getPurchasesList()).isEmpty()) {

                        if (splash_sp != null) {
                            SharedPreferences.Editor editor = splash_sp.edit();
                            if (editor != null) {
                                if (splash_sp.contains("premium_status")) {
                                    editor.remove("premium_status").apply();
                                }
                                editor.putBoolean("premium_status", true).apply();
                                checkPrimumUsers=true;
                            }
                        }
                    } else {
                        if (splash_sp != null) {
                            SharedPreferences.Editor editor = splash_sp.edit();
                            if (editor != null) {
                                if (splash_sp.contains("premium_status")) {
                                    editor.remove("premium_status").apply();
                                }
                                editor.putBoolean("premium_status", false).apply();
                                checkPrimumUsers=false;
                            }
                        }
                    }
                });
            }

            @Override
            public void onBillingServiceDisconnected() {
            }
        });
    }

    private void move_to_next_Screen() {
        new Handler().postDelayed(this::move_to_home, 3000);
    }

    private void start_service() {
        try {
            Intent serviceIntent = new Intent(Splash_Screen.this, Fetch_Service.class);
            startService(serviceIntent);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }


    public BroadcastReceiver data_fetch = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            boolean is_data_fetched = intent.getBooleanExtra("data_fetched", false);
            String intersetial_ad = intent.getStringExtra("intersetial_ad");

            appInfos = (ArrayList<PackageInfo>) intent.getSerializableExtra("all_apps");

            if (appInfos != null && !appInfos.isEmpty()) {
                Log.d("intersetial_ad", "" + intersetial_ad);
                if (is_data_fetched) {

                  //  move_to_home();
                    if (!intersetial_ad.isEmpty() && IS_ADMOB_ENABLE ) {

                        /*load_intersetial_ad(intersetial_ad);*/

                        new Handler().postDelayed(() -> {
                            showInterstitial();
                        }, 3000);
                    }
                    else {
                        move_to_home();
                    }
                } else {
                    TastyToast.makeText(Splash_Screen.this, "Failed to fetch the data!", TastyToast.LENGTH_LONG, TastyToast.INFO);
                }
            }
        }
    };

    private void showInterstitial() {
        if (sInterstitial != null) {
            //disconnect screen..!!
            sInterstitial.show(Splash_Screen.this);

            sInterstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    move_to_home();
                    super.onAdFailedToShowFullScreenContent(adError);
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    move_to_home();
                    sInterstitial = null;
                    super.onAdDismissedFullScreenContent();
                }

                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                }
            });
        } else {
            move_to_home();
        }
    }

    /*private void load_intersetial_ad(String sInterstitial_ad) {

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(Splash_Screen.this, sInterstitial_ad, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        Log.d("intersetial_onAdLoaded", "" + interstitialAd.getAdUnitId());
                        sInterstitial = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.d("intersetial_failed", "" + loadAdError.getMessage());
                        sInterstitial = null;
                    }
                });


    }*/

    private void loadinterstitialAd(){

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, "ca-app-pub-6991932537186982/6208061685", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        sInterstitial = interstitialAd;
                        Log.i(TAG, "InterstitialAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.d(TAG, loadAdError.toString());
                        sInterstitial = null;
                    }
                });


    }


    private void move_to_home() {
        Intent intent = new Intent(Splash_Screen.this, Home_Activity.class);
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        unregister_broadcast();
        super.onDestroy();
    }

    private void unregister_broadcast() {
        if (data_fetch != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(data_fetch);
        }
    }

    @Override
    protected void onStop() {
        unregister_broadcast();
        super.onStop();
    }


}
