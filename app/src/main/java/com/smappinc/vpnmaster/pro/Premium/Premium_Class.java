package com.smappinc.vpnmaster.pro.Premium;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.smappinc.vpnmaster.pro.Activity.Home.Home_Activity;
import com.smappinc.vpnmaster.pro.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class Premium_Class extends Dialog {

    Activity contexts;
    ImageView iv_close_sub;
    SharedPreferences payment_preference;
    private SkuDetails seven_days_sub, one_month_sub, twelve_month_sub;
    String TAG = "Premium_Subs";
    private BillingClient mBillingClient;
    boolean payment_bool;
    RelativeLayout seven_days, one_month, twelve_month;

    Button subBtn;
    TextView week_text, subs_status;
    TextView seven_days_text, one_month_text, twelve_month_text;
    TextView year_text;
    TextView year_rate;

    public Premium_Class(Activity context, int themeResId) {
        super(context, themeResId);
        contexts = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscription_layout);

        payment_preference = contexts.getSharedPreferences("DATA", MODE_PRIVATE);

        init_google_inapp();
        iv_close_sub = findViewById(R.id.iv_close_sub);

        seven_days = findViewById(R.id.seven_days);
        one_month = findViewById(R.id.one_month);
        twelve_month = findViewById(R.id.twelve_month);

        seven_days_text = findViewById(R.id.seven_days_text);
        one_month_text = findViewById(R.id.one_month_text);
        twelve_month_text = findViewById(R.id.twelve_month_text);

        subBtn = findViewById(R.id.premium);

        subBtn.setOnClickListener(view -> {

            //   one_month.setBackgroundColor(contexts.getResources().getColor(R.color.bluee_mid));
            // seven_days.setBackground(null);
            //twelve_month.setBackground(null);
           /* one_month.setBackgroundResource(R.drawable.shape_selected);
            twelve_month.setBackgroundResource(R.drawable.shape_rounded_sky_blue_button);
            seven_days.setBackgroundResource(R.drawable.shape_rounded_sky_blue_button);*/

//            yearly_plan.setBackground(null);
//            yearly_plan.setBackgroundResource(R.drawable.round_background);
//
//            year_text.setTextColor(R.color.colorWhite);
//            year_rate.setTextColor(R.color.colorWhite);
//
//            monthly_plan.setBackground(null);
//            monthly_plan.setBackgroundResource(R.drawable.round_background_white);
//
//            weekly_plan.setBackground(null);
//            weekly_plan.setBackgroundResource(R.drawable.round_background_white);
//
//            week_text.setTextColor(R.color.colorPrimary);
//            week_rate.setTextColor(R.color.colorPrimary);
//
//            month_text.setTextColor(R.color.colorPrimary);
//            month_rate.setTextColor(R.color.colorPrimary);
            if (one_month_sub != null) {
                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(one_month_sub)
                        .build();

                int responseCode = mBillingClient.launchBillingFlow(contexts, billingFlowParams).getResponseCode();

                if (responseCode != BillingClient.BillingResponseCode.OK)
                    Toast.makeText(contexts, "Error", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(contexts, "Error", Toast.LENGTH_SHORT).show();
        });



        //        week_rate = findViewById(R.id.week_rate);
//        month_rate = findViewById(R.id.month_rate);
//        year_rate = findViewById(R.id.year_rate);
//
//        month_text = findViewById(R.id.month_text);
//        year_text = findViewById(R.id.lifetime_plan_text);
        iv_close_sub.setOnClickListener(view -> dismiss());


        seven_days.setOnClickListener(view -> {

          //  seven_days.setBackgroundColor(contexts.getResources().getColor(R.color.bluee_mid));
            //one_month.setBackground(null);
            //twelve_month.setBackground(null);
           /* seven_days.setBackgroundResource(R.drawable.shape_selected);
            one_month.setBackgroundResource(R.drawable.shape_rounded_sky_blue_button);
            twelve_month.setBackgroundResource(R.drawable.shape_rounded_sky_blue_button);*/


//            weekly_plan.setBackground(null);
//            weekly_plan.setBackgroundResource(R.drawable.round_background);
//
//            week_text.setTextColor(contexts.getResources().getColor(R.color.colorWhite));

//            week_rate.setTextColor(R.color.colorWhite);
//
//            monthly_plan.setBackground(null);
//            monthly_plan.setBackgroundResource(R.drawable.round_background_white);
//
//            yearly_plan.setBackground(null);
//            yearly_plan.setBackgroundResource(R.drawable.round_background_white);
//
//            month_text.setTextColor(R.color.colorPrimary);
//            month_rate.setTextColor(R.color.colorPrimary);

//            year_text.setTextColor(contexts.getResources().getColor(R.color.colorPrimary));
//            year_rate.setTextColor(contexts.getResources().getColor(R.color.colorPrimary));
            if (seven_days_sub != null) {
                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(seven_days_sub)
                        .build();

                int responseCode = mBillingClient.launchBillingFlow(contexts, billingFlowParams).getResponseCode();

                if (responseCode != BillingClient.BillingResponseCode.OK)
                    Toast.makeText(contexts, "Error", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(contexts, "Error", Toast.LENGTH_SHORT).show();
        });


        one_month.setOnClickListener(view -> {

         //   one_month.setBackgroundColor(contexts.getResources().getColor(R.color.bluee_mid));
           // seven_days.setBackground(null);
            //twelve_month.setBackground(null);
           /* one_month.setBackgroundResource(R.drawable.shape_selected);
            twelve_month.setBackgroundResource(R.drawable.shape_rounded_sky_blue_button);
            seven_days.setBackgroundResource(R.drawable.shape_rounded_sky_blue_button);*/

//            yearly_plan.setBackground(null);
//            yearly_plan.setBackgroundResource(R.drawable.round_background);
//
//            year_text.setTextColor(R.color.colorWhite);
//            year_rate.setTextColor(R.color.colorWhite);
//
//            monthly_plan.setBackground(null);
//            monthly_plan.setBackgroundResource(R.drawable.round_background_white);
//
//            weekly_plan.setBackground(null);
//            weekly_plan.setBackgroundResource(R.drawable.round_background_white);
//
//            week_text.setTextColor(R.color.colorPrimary);
//            week_rate.setTextColor(R.color.colorPrimary);
//
//            month_text.setTextColor(R.color.colorPrimary);
//            month_rate.setTextColor(R.color.colorPrimary);
            if (one_month_sub != null) {
                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(one_month_sub)
                        .build();

                int responseCode = mBillingClient.launchBillingFlow(contexts, billingFlowParams).getResponseCode();

                if (responseCode != BillingClient.BillingResponseCode.OK)
                    Toast.makeText(contexts, "Error", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(contexts, "Error", Toast.LENGTH_SHORT).show();
        });


        twelve_month.setOnClickListener(view -> {

         //   twelve_month.setBackgroundColor(contexts.getResources().getColor(R.color.bluee_mid));
/*twelve_month.setBackgroundResource(R.drawable.shape_selected);
seven_days.setBackgroundResource(R.drawable.shape_rounded_sky_blue_button);
one_month.setBackgroundResource(R.drawable.shape_rounded_sky_blue_button);*/
           // seven_days.setBackground(null);
            //one_month.setBackground(null);
//            monthly_plan.setBackground(null);
//            monthly_plan.setBackgroundResource(R.drawable.round_background);
//            month_text.setTextColor(R.color.colorWhite);
//            month_rate.setTextColor(R.color.colorWhite);
//
//            yearly_plan.setBackground(null);
//            yearly_plan.setBackgroundResource(R.drawable.round_background_white);
//
//            weekly_plan.setBackground(null);
//            weekly_plan.setBackgroundResource(R.drawable.round_background_white);
//
//            week_text.setTextColor(R.color.colorPrimary);
//            week_rate.setTextColor(R.color.colorPrimary);
//
//
//            year_text.setTextColor(R.color.colorPrimary);
//            year_rate.setTextColor(R.color.colorPrimary);
            if (twelve_month_sub != null) {

                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(twelve_month_sub)
                        .build();

                int responseCode = mBillingClient.launchBillingFlow(contexts, billingFlowParams).getResponseCode();

                if (responseCode != BillingClient.BillingResponseCode.OK)
                    Toast.makeText(contexts, "Error", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(contexts, "Error", Toast.LENGTH_SHORT).show();
        });
    }

    private void isUserHasSubscription_premium() {
        if (mBillingClient == null) {

            mBillingClient = BillingClient.newBuilder(contexts)
                    .setListener(purchasesUpdatedListener)
                    .enablePendingPurchases()
                    .build();

        }
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
//                final Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.SUBS);
//
//                mBillingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS, (billingResult1, list) -> {
//
////                    if (billingResult1.getResponseCode() == BillingClient.BillingResponseCode.OK
////                            && !Objects.requireNonNull(purchasesResult.getPurchasesList()).isEmpty()) {
////
////                        subs_status.setText(contexts.getString(R.string.subs_done));
////                    } else {
////                        subs_status.setText(contexts.getString(R.string.subsc_status));
////                    }
//                });
            }

            @Override
            public void onBillingServiceDisconnected() {

            }
        });
    }

    private final PurchasesUpdatedListener purchasesUpdatedListener = (billingResult, list) -> {

        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
            for (Purchase purchase : list) {
                handlePurchase(purchase);
            }
        }  // Handle an error caused by a user cancelling the purchase flow.
        // Handle any other error codes.
    };


    private void init_google_inapp() {

        mBillingClient = BillingClient.newBuilder(contexts)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();

        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK)
                    getProducts();
                set_prices_item();

                isUserHasSubscription_premium();
            }

            @Override
            public void onBillingServiceDisconnected() {
            }
        });
    }

    private void set_prices_item() {

        List<String> skuList = new ArrayList<>();
        skuList.add("no_ads_vip_servers_one_month");
        skuList.add("no_ads_vip_servers_three_month");
        skuList.add("no_ads_vip_servers_one_year");

        SkuDetailsParams params_prices = SkuDetailsParams.newBuilder()
                .setSkusList(skuList)
                .setType(BillingClient.SkuType.SUBS)
                .build();

//        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
        mBillingClient.querySkuDetailsAsync(params_prices, (billingResult, skuDetailsList) -> {
            // Process the result.
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                if (!skuDetailsList.isEmpty()) {
                    for (SkuDetails skuDetails : skuDetailsList) {
                        switch (skuDetails.getSku()) {
                            case "no_ads_vip_servers_one_month":
                                seven_days_sub = skuDetails;
                                break;
                            case "no_ads_vip_servers_three_month":
                                one_month_sub = skuDetails;
                                break;
                            case "no_ads_vip_servers_one_year":
                                twelve_month_sub = skuDetails;
                                break;
                        }

                        if (seven_days_sub != null && one_month_sub != null && twelve_month_sub != null) {
                            Log.d("All_Prices", "     " + seven_days_sub.getPrice() + "     " + one_month_sub.getPrice() + "     " + twelve_month_sub.getPrice());

                            String weeklysub = seven_days_sub.getPrice();
                            String monthlysub = one_month_sub.getPrice();
                            String yearlysub = twelve_month_sub.getPrice();
                            set_text(weeklysub, monthlysub, yearlysub);
                        }
                    }
                }
            } else {
                if (contexts != null) {
                    Toast.makeText(contexts, "Error in retrieving " + "products from store", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void set_text(String price, String price1, String price2) {
        Log.d("price_values", "" + price + "   " + price1 + "   " + price2);

        contexts.runOnUiThread(() -> {
            seven_days_text.setText("");
            one_month_text.setText("");
            twelve_month_text.setText("");

            seven_days_text.setText(price);
            one_month_text.setText(price1);
            twelve_month_text.setText(price2);
        });

    }

    private void getProducts() {
        final List<String> skuList = new ArrayList<>();

        skuList.add("weeklysub");
        skuList.add("monthlysub");
        skuList.add("yearlysub");

        SkuDetailsParams params = SkuDetailsParams.newBuilder()
                .setSkusList(skuList)
                .setType(BillingClient.SkuType.SUBS)
                .build();


        mBillingClient.querySkuDetailsAsync(params, (billingResult, list) -> {

            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {

                if (!list.isEmpty()) {
                    for (SkuDetails skuDetails : list) {
                        Log.d("sku_detaiks", "" + skuDetails.toString());
                        switch (skuDetails.getSku()) {
                            case "weeklysub":
                                seven_days_sub = skuDetails;
                                break;
                            case "one_month":
                                one_month_sub = skuDetails;
                                break;
                            case "yearlysub":
                                twelve_month_sub = skuDetails;
                                break;
                        }
                    }
                }
            } else {
                if (contexts != null) {
                    Toast.makeText(contexts, "Error in retrieving " + "products from store", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void handlePurchase(Purchase purchase) {

        AcknowledgePurchaseParams acknowledgePurchaseParams =
                AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();

        AcknowledgePurchaseResponseListener acknowledgePurchaseResponseListener = billingResult -> {

            payment_bool = true;
            SharedPreferences.Editor editor = payment_preference.edit();
            if (editor != null) {

                if (payment_preference.contains("premium_status")) {
                    editor.remove("premium_status").apply();
                }

                editor.putBoolean("premium_status", payment_bool).apply();
                Intent intent = new Intent(contexts, Home_Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                contexts.startActivity(intent);
            }
        };
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged()) {
                mBillingClient.acknowledgePurchase(acknowledgePurchaseParams, acknowledgePurchaseResponseListener);
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dismiss();
    }
}

