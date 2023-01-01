package com.smappinc.vpnmaster.pro.Activity.Disconnect;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.internet_speed_testing.InternetSpeedBuilder;
import com.example.internet_speed_testing.ProgressionModel;
import com.flag_selection.Country_Flag_Picker;
import com.flag_selection.Country_Names;
import com.flag_selection.listeners.Country_Picker_Listener_Interface;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.smappinc.vpnmaster.pro.Fragment.Home_fragment_Class;
import com.smappinc.vpnmaster.pro.R;
import com.smappinc.vpnmaster.pro.Record_Time.Connection_Timer;
import com.smappinc.vpnmaster.pro.Util.Constant;
import com.smappinc.vpnmaster.pro.Util.PrefManager;
import com.sdsmdg.tastytoast.TastyToast;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Objects;

import static com.smappinc.vpnmaster.pro.Util.Constant.ADMOB_BANNER_AD;
import static com.smappinc.vpnmaster.pro.Util.Constant.IS_ADMOB_ENABLE;
import static com.smappinc.vpnmaster.pro.Util.Constant.getBestServer_after_calculation;
import static com.smappinc.vpnmaster.pro.Util.Constant.set_status_bar;

public class Disconnect_Feature extends AppCompatActivity implements Country_Picker_Listener_Interface {
    Toolbar toolbar;
    String success;
    public static boolean return_value = false;

    TextView disconnected_county, disconnected_download, disconnected_upload, duration_value, text_promo;
    String hrSize;
    View ratelayout;
    ImageView rate_1;
    ImageView rate_2;
    ImageView rate_3;
    ImageView rate_4;
    ImageView rate_5;
    ImageView toolbar_back_button;
    int position = 0;
    int lastPosition = 0;
    boolean isRunning = true, is_premium_disconnect;
    Country_Flag_Picker country_pick;
    Country_Names countryNamesNames3;
    ImageView country_image_connected;
    SharedPreferences sharedPreferences;
    PrefManager prefManager,prefManager1,prefManager2;
    private NativeAd nativeAd = null;
    int val=0;
    String finalV="05:00";
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint({"SetTextI18n", "LongLogTag"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.disconnect_layout);


        sharedPreferences = getSharedPreferences("DATA", MODE_PRIVATE);

        if (sharedPreferences != null) {
            if (sharedPreferences.contains("premium_status")) {
                is_premium_disconnect = sharedPreferences.getBoolean("premium_status", false);
            } else {
                is_premium_disconnect = false;
            }
        }

        set_status_bar(Disconnect_Feature.this);
        init_disconnect();
        set_country_name_timer();
        set_download_upload();

        if (!is_premium_disconnect) {
            if (IS_ADMOB_ENABLE) {
                set_banner();
                refreshAd();

            }
        }

        rate_1.setOnClickListener(v -> {
            rate_1.setImageResource(R.drawable.star_slected);
            rate_2.setImageResource(R.drawable.star);
            rate_3.setImageResource(R.drawable.star);
            rate_4.setImageResource(R.drawable.star);
            rate_5.setImageResource(R.drawable.star);
            TastyToast.makeText(
                    getApplicationContext(),
                    "Thanks For Your Feedback!",
                    TastyToast.LENGTH_LONG,
                    TastyToast.INFO
            );        });
          rate_2.setOnClickListener(v -> {
            rate_1.setImageResource(R.drawable.star_slected);
            rate_2.setImageResource(R.drawable.star_slected);
            rate_3.setImageResource(R.drawable.star);
            rate_4.setImageResource(R.drawable.star);
            rate_5.setImageResource(R.drawable.star);
            TastyToast.makeText(
                    getApplicationContext(),
                    "Thanks For Your Feedback!",
                    TastyToast.LENGTH_LONG,
                    TastyToast.INFO
            );        });
              rate_3.setOnClickListener(v -> {
            rate_1.setImageResource(R.drawable.star_slected);
            rate_2.setImageResource(R.drawable.star_slected);
            rate_3.setImageResource(R.drawable.star_slected);
            rate_4.setImageResource(R.drawable.star);
            rate_5.setImageResource(R.drawable.star);
            TastyToast.makeText(
                    getApplicationContext(),
                    "Thanks For Your Feedback!",
                    TastyToast.LENGTH_LONG,
                    TastyToast.INFO
            );        });

          rate_4.setOnClickListener(v -> {
            rate_1.setImageResource(R.drawable.star_slected);
            rate_2.setImageResource(R.drawable.star_slected);
            rate_3.setImageResource(R.drawable.star_slected);
            rate_4.setImageResource(R.drawable.star_slected);
            rate_5.setImageResource(R.drawable.star);
              startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                 });
              rate_5.setOnClickListener(v -> {
            rate_1.setImageResource(R.drawable.star_slected);
            rate_2.setImageResource(R.drawable.star_slected);
            rate_3.setImageResource(R.drawable.star_slected);
            rate_4.setImageResource(R.drawable.star_slected);
            rate_5.setImageResource(R.drawable.star_slected);
                  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));

              });





        toolbar_back_button.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void refreshAd() {
        AdLoader.Builder builder = new AdLoader.Builder(Disconnect_Feature.this, Constant.ADMOB_NATIVE_TESTING);

        builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(@NonNull @NotNull NativeAd nativeAd1) {
                if (nativeAd != null) {
                    nativeAd.destroy();
                }
                nativeAd = nativeAd1;
                FrameLayout frameLayout = findViewById(R.id.connection_ad_native);
                NativeAdView adView = (NativeAdView) getLayoutInflater()
                        .inflate(R.layout.disconnect_dialog_ad_layout, null);
                populateUnifiedNativeAdView2(nativeAd1, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }
        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(false)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());


    }
    private void populateUnifiedNativeAdView2(NativeAd nativeAd, NativeAdView adView) {
        com.google.android.gms.ads.nativead.MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);
		/*VideoController vc = nativeAd.getVideoController();
		if (vc.hasVideoContent()) {
			vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
				@Override
				public void onVideoEnd() {
					super.onVideoEnd();
				}
			});
		}*/
    }


    private void set_banner() {
     //   setbanner_Add_disconnect();
    }

    private void set_country_name_timer() {
        //get server from local cache..!!
        if (sharedPreferences != null) {
            success = Objects.requireNonNull(getBestServer_after_calculation(sharedPreferences)).getHost_name();
            if (success != null) {
                country_pick = new Country_Flag_Picker.Builder().with(Disconnect_Feature.this).listener(Disconnect_Feature.this).build();
                if (country_pick != null) {
                    if (success.equals("Germany")) {
                        country_image_connected.setImageResource(0);
                        country_image_connected.setImageResource(R.drawable.flag_de);
                        disconnected_county.setText(success);
                    } else {
                        countryNamesNames3 = country_pick.getCountryByName(success);
                        if (countryNamesNames3 != null) {
                            country_image_connected.setImageResource(0);
                            country_image_connected.setImageResource(countryNamesNames3.getFlag());
                            disconnected_county.setText(success);
                        } else {
                            country_image_connected.setImageResource(0);
                            country_image_connected.setImageResource(R.drawable.flag_us);
                            disconnected_county.setText(success);
                        }
                    }
                }
            }
        }

    /*    if (Connection_Timer.getTimer_value() != null) {
            if (Connection_Timer.end_time != null && Connection_Timer.start_time != null) {
                duration_value.setText(Connection_Timer.getTimer_value());
            }
        }*/

        duration_value.setText(Home_fragment_Class.timeV);



    }

    private void init_disconnect() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sharedPreferences = getSharedPreferences("DATA", MODE_PRIVATE);

        duration_value = findViewById(R.id.Duration);
        toolbar = findViewById(R.id.toolbar);
        disconnected_county = findViewById(R.id.disconnected_county);
        country_image_connected = findViewById(R.id.circleView_disconnect);
        toolbar_back_button = findViewById(R.id.toolbar_back_button);
        disconnected_download = findViewById(R.id.disconnected_download);
        disconnected_upload = findViewById(R.id.disconnected_upload);
        rate_1 = findViewById(R.id.star_1);
        rate_2 = findViewById(R.id.star_2);
        rate_3 = findViewById(R.id.star_3);
        rate_4 = findViewById(R.id.star_4);
        rate_5 = findViewById(R.id.star_5);


        show_hide_button();
        stopService(new Intent(Disconnect_Feature.this, Connection_Timer.class));
    }

    private void show_hide_button() {
        if (sharedPreferences != null) {

            boolean value_is = sharedPreferences.getBoolean("is_btn_clicked", false);

            if (value_is) {
          //      text_promo.setVisibility(View.GONE);
            //    good_disconnect.setVisibility(View.GONE);
            } else {
           //     text_promo.setVisibility(View.VISIBLE);
         //       good_disconnect.setVisibility(View.VISIBLE);
            }
        }
    }

    private void set_download_upload() {
        try {

            InternetSpeedBuilder builder = new InternetSpeedBuilder(Disconnect_Feature.this);
            builder.setOnEventInternetSpeedListener(new InternetSpeedBuilder.OnEventInternetSpeedListener() {
                @Override
                public void onDownloadProgress(int count, @NotNull ProgressionModel progressModel) {

                    java.math.BigDecimal bigDecimal = new java.math.BigDecimal("" + progressModel.getDownloadSpeed());
                    float finalDownload = (bigDecimal.longValue() / 1000000);

                    java.math.BigDecimal bd = progressModel.getDownloadSpeed();

                    final double d = bd.doubleValue();
                    position = getPositionByRate(finalDownload);
                    runOnUiThread(() -> {

                        if (isRunning) {
                            disconnected_download.setText("" + formatFileSize(d));

                        } else {
                            disconnected_download.setText("0 KB/S");
                        }
                    });
                    lastPosition = position;
                }


                @Override
                public void onTotalProgress(int count, final ProgressionModel progressModel) {

                }


                @Override
                public void onUploadProgress(int count, final ProgressionModel progressModel) {

                    java.math.BigDecimal bd = progressModel.getUploadSpeed();

                    final double d = bd.doubleValue();

                    runOnUiThread(() -> {

                        if (isRunning) {
                            disconnected_upload.setText("" + formatFileSize(d));

                        } else {
                            disconnected_upload.setText("0 KB/S");
                        }
                    });

                    lastPosition = position;
                }
            });
            builder.start("https://i.ytimg.com/vi/1HtTfwdgAY8/hqdefault.jpg", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String formatFileSize(double size) {
        try {
            double k = size / 1024.0;
            double m = ((size / 1024.0) / 1024.0);
            double g = (((size / 1024.0) / 1024.0) / 1024.0);
            double t = ((((size / 1024.0) / 1024.0) / 1024.0) / 1024.0);

            DecimalFormat dec = new DecimalFormat("0.00");

            if (t > 1) {
                hrSize = dec.format(t).concat(" ");
            } else if (g > 1) {
                hrSize = dec.format(g);
            } else if (m > 1) {
                hrSize = dec.format(m).concat(" mb/s");
            } else if (k > 1) {
                hrSize = dec.format(k).concat(" kb/s");
            } else {
                hrSize = dec.format(size);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hrSize;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public int getPositionByRate(float rate) {

        try {
            if (rate <= 1) {
                return (int) (rate * 30);
            } else if (rate <= 10) {
                return (int) (rate * 6) + 30;
            } else if (rate <= 30) {
                return (int) ((rate - 10) * 3) + 90;
            } else if (rate <= 50) {
                return (int) ((rate - 30) * 1.5) + 150;
            } else if (rate <= 100) {
                return (int) ((rate - 50) * 1.2) + 180;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onSelectCountry(Country_Names countryNamesNames) {
    }

    private void setbanner_Add_disconnect() {
        View adContainer = findViewById(R.id.adMobView_disconnect);
        AdView mAdView = new AdView(Disconnect_Feature.this);
        mAdView.setAdSize(AdSize.SMART_BANNER);
        mAdView.setAdUnitId(ADMOB_BANNER_AD);
        ((RelativeLayout) adContainer).addView(mAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
