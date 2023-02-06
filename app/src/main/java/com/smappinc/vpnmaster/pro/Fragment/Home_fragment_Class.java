package com.smappinc.vpnmaster.pro.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import static com.smappinc.vpnmaster.pro.Record_Time.Connection_Timer.getTimer_value;
import static com.smappinc.vpnmaster.pro.Util.Constant.ADMOB_INTERSETIAL_AD;
import static com.smappinc.vpnmaster.pro.Util.Constant.ADMOB_NATIVE_TESTING;
import static com.smappinc.vpnmaster.pro.Util.Constant.AUTO_CONNECTION;
import static com.smappinc.vpnmaster.pro.Util.Constant.CLICKED_PROTOCOL;
import static com.smappinc.vpnmaster.pro.Util.Constant.IKEV2_CONNECTION;
import static com.smappinc.vpnmaster.pro.Util.Constant.IS_ADMOB_ENABLE;
import static com.smappinc.vpnmaster.pro.Util.Constant.IS_RUN;
import static com.smappinc.vpnmaster.pro.Util.Constant.OVPN_CONNECTION;
import static com.smappinc.vpnmaster.pro.Util.Constant.decodeSampledBitmapFromResource;
import static com.smappinc.vpnmaster.pro.Util.Constant.getBestServer_after_calculation;
import static com.smappinc.vpnmaster.pro.Util.FragActivity.TAG;
import static com.smappinc.vpnmaster.pro.core.OpenVpnService.mConnectionState;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.VpnService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.flag_selection.Country_Flag_Picker;
import com.flag_selection.Country_Names;
import com.flag_selection.listeners.Country_Picker_Listener_Interface;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewarded.ServerSideVerificationOptions;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.smappinc.vpnmaster.pro.Activity.Disconnect.Disconnect_Feature;
import com.smappinc.vpnmaster.pro.Activity.Home.Home_Activity;

import com.smappinc.vpnmaster.pro.Activity.Splash_Screen;
import com.smappinc.vpnmaster.pro.BuildConfig;
import com.smappinc.vpnmaster.pro.Model.api_response;
import com.smappinc.vpnmaster.pro.Premium.Premium_Class;
import com.smappinc.vpnmaster.pro.R;
import com.smappinc.vpnmaster.pro.Record_Time.Connection_Timer;
import com.smappinc.vpnmaster.pro.Activity.Server_Feature.Server_Screen;
import com.smappinc.vpnmaster.pro.Util.Constant;
import com.smappinc.vpnmaster.pro.Util.Glob;
import com.smappinc.vpnmaster.pro.Util.PrefManager;
import com.smappinc.vpnmaster.pro.core.OpenConnectManagementThread;
import com.smappinc.vpnmaster.pro.core.OpenVpnService;
import com.smappinc.vpnmaster.pro.core.ProfileManager;
import com.smappinc.vpnmaster.pro.core.VPNConnector;
import com.sdsmdg.tastytoast.TastyToast;
import com.skyfishjy.library.RippleBackground;

import org.jetbrains.annotations.NotNull;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Map;
import java.util.Objects;


public class Home_fragment_Class extends Fragment implements Country_Picker_Listener_Interface {

    View view;
    ConstraintLayout changeServerlyt2;
    FragmentActivity fragmentActivity;
    TextView tvTime;
    NativeAd nativeAd_home, native_refresh;
    TextView auto_protocol, ikev2_protocol, udp_protocol, vpn_connection_status, changeServer;
    private RelativeLayout premium;
    SharedPreferences home_fragment_shared_pref;
    ImageView connect_button, ivFlag;
    Chronometer time_info_tv;
    String certificate_value;
    LinearLayout protocol;
    RippleBackground ripple, ripple2;
    private InterstitialAd sInterstitial;
    private OpenVpnService OpenService;
    private int SpinerIndex = 0;
    Boolean checTimerbtn=false;
    //ovpn
    ServiceConnection service_connection;

    public final String BROADCAST_ACTION = "de.blinkt.openvpn.VPN_STATUS";
    Dialog dialog_home_Activity;
    CountDownTimer timer_connecting;
    Country_Names countryName;
    public Country_Flag_Picker countryFlagPicker_home;
    LinearLayout lineTimer;
    TextView disconnTv;
    //ikev2
 public static boolean is_premium;
  public static String timeV;
    //disconnect_native_ad
    FrameLayout Dis_FrameLayout;
    Boolean checkConnected=false;


    public Home_fragment_Class() {
        // Required empty public constructor
    }

    public static Home_fragment_Class newInstance() {
        return new Home_fragment_Class();
    }

    @Override
    public void onStart() {
       /* load_ikev2_service();
        BroadcastReceiver opnVPNStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getStringExtra("status") != null) {
                    openVpnStatus = ConnectionStatus.valueOf(intent.getStringExtra("status"));
                    Log.d("OpenVpnStatus", intent.getStringExtra("status"));
                }
            }
        };

        fragmentActivity.registerReceiver(opnVPNStateReceiver, new IntentFilter(BROADCAST_ACTION));*/
        fragmentActivity.registerReceiver(connect_vpn, new IntentFilter("connect_vpn"));
        fragmentActivity.registerReceiver(premium_dialog, new IntentFilter("premium_clicked"));

        super.onStart();
    }

  /*  private void load_ikev2_service() {

        if (fragmentActivity != null) {
            *//*load_ovpn_service();*//*
            fragmentActivity.bindService(new Intent(fragmentActivity, VpnStateService.class), mServiceConnection, Service.BIND_AUTO_CREATE);
            Intent intentss = new Intent(fragmentActivity, OpenVPNService.class);
            intentss.setAction(OpenVPNService.START_SERVICE);
            fragmentActivity.bindService(intentss, service_connection, Context.BIND_AUTO_CREATE);
        }
    }*/

    /*private void load_ovpn_service() {
        service_connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName className, IBinder service) {
                OpenVPNService.LocalBinder binder = (OpenVPNService.LocalBinder) service;
                openvpn_service = binder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
    }
*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {

        if (context instanceof Activity) {
            fragmentActivity = (FragmentActivity) context;
        }

        if (home_fragment_shared_pref == null)
            home_fragment_shared_pref = fragmentActivity.getSharedPreferences("DATA", MODE_PRIVATE);

        is_premium = home_fragment_shared_pref.getBoolean("premium_status", false);
        if (!is_premium) {
            if (IS_ADMOB_ENABLE) {
                load_interstitial_Ad(fragmentActivity);
            }
        }

        LocalBroadcastManager.getInstance(fragmentActivity).registerReceiver(connect_vpn, new IntentFilter("connect_vpn"));
        LocalBroadcastManager.getInstance(fragmentActivity).registerReceiver(premium_dialog, new IntentFilter("premium_clicked"));

        super.onAttach(context);
    }

    public BroadcastReceiver connect_vpn = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            if (IS_RUN) {

                                final Handler handler_auto = new Handler();
                                handler_auto.postDelayed(() -> startVPN(), 1000);
                    } else {
                        //no selection from tabs
                            final Handler handler_auto = new Handler();
                            handler_auto.postDelayed(() -> startVPN(), 1000);
                        }



            IS_RUN = false;
        }
    };

    public BroadcastReceiver premium_dialog = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            if (IS_RUN) {
                if (fragmentActivity != null) {
                    Premium_Class dialog = new Premium_Class(fragmentActivity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                    if (!getActivity().isFinishing()) {
                        dialog.show();
///check now sir, ok... sir first see
                        // when not premium user and tap on blue button dialog says 'you are premium user'
                        //then when i sunscribe dialog doesnt show, only takes me to premium activity for buying
                        //

                    }
                }
                IS_RUN = false;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_layout, container, false);

        init_view(view);

        if (!is_premium) {
            if (IS_ADMOB_ENABLE) {
                // banner ad removed
                // show_banner_ad_home();
                refreshAdMain();
                /*refreshAd();*/
            }
        }
        if(BuildConfig.VERSION_NAME.matches(Constant.Version)) {
            InitiateFirstDialog();
        }

        return view;
    }

    private void show_banner_ad_home() {
        View adContainer = view.findViewById(R.id.adMobView_home_screen);
        AdView mAdView = new AdView(fragmentActivity);
        mAdView.setAdSize(AdSize.SMART_BANNER);
        mAdView.setAdUnitId(Constant.ADMOB_BANNER_AD);
        ((RelativeLayout) adContainer).addView(mAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }





    private void populateUnifiedNativeAdView_home(NativeAd nativeAd, NativeAdView adView) {

        adView.setMediaView(adView.findViewById(R.id.on_screen_ad_media));
        adView.setHeadlineView(adView.findViewById(R.id.on_screen_ad_headline));
        adView.setBodyView(adView.findViewById(R.id.on_screen_ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.on_screen_ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.on_screen_ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.on_screen_ad_price));
        adView.setStoreView(adView.findViewById(R.id.on_screen_ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.on_screen_ad_advertiser));

        ((TextView) Objects.requireNonNull(adView.getHeadlineView())).setText(nativeAd.getHeadline());
        Objects.requireNonNull(adView.getMediaView()).setMediaContent(Objects.requireNonNull(nativeAd.getMediaContent()));

        if (nativeAd.getBody() == null) {
            Objects.requireNonNull(adView.getBodyView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(adView.getBodyView()).setVisibility(View.VISIBLE);
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

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);
        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getMediaContent().getVideoController();
        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {
            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.
                    super.onVideoEnd();
                }
            });
        }
    }

    private void init_view(View view) {
        if (fragmentActivity != null) {
            home_fragment_shared_pref = fragmentActivity.getSharedPreferences("DATA", MODE_PRIVATE);
        }

        changeServerlyt2 = view.findViewById(R.id.changeServerlyt2);
        connect_button = view.findViewById(R.id.connect_button);
        ivFlag = view.findViewById(R.id.ivFlag);
        ripple = view.findViewById(R.id.ripple);
        ripple2 = view.findViewById(R.id.ripple2);
        premium = view.findViewById(R.id.proo);
        tvTime=view.findViewById(R.id.timeadd);
        lineTimer=view.findViewById(R.id.linetime);
        disconnTv=view.findViewById(R.id.disconnectadd);

        time_info_tv = view.findViewById(R.id.time_info_tv);
        vpn_connection_status = view.findViewById(R.id.vpn_connection_status);
        changeServer = view.findViewById(R.id.changeServer);
      //  InitiateFirstDialog();

        /*if(PrefManager.isFirst(getActivity())){

        firstDialog.show();
        }*/

       tvTime.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

                   checTimerbtn=true;
                   RateDialog.show();
           }
       });
       disconnTv.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               if(OpenService.getConnectionState()==OpenConnectManagementThread.STATE_CONNECTED){
                   dialog_home_Activity.show();
                  // OpenService.stopVPN();
               }
           }
       });
        vpn_connection_status.setOnClickListener(v -> {
            connect();
        });
        /*MobileAds.initialize(
                getActivity(),
                new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {
                       // loadRewardedInterstitialAd();
                        Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
                        for (String adapterClass : statusMap.keySet()) {
                            AdapterStatus status = statusMap.get(adapterClass);
                            Log.d("MyApp", String.format(
                                    "Adapter name: %s, Description: %s, Latency: %d",
                                    adapterClass, status.getDescription(), status.getLatency()));
                        }
                    }
                });*/
        protocol = view.findViewById(R.id.protocol);

        auto_protocol = view.findViewById(R.id.auto_protocol);
        ikev2_protocol = view.findViewById(R.id.ikev2_protocol);
        udp_protocol = view.findViewById(R.id.udp_protocol);

        InitiateDiscountDialog();

/*
        if (isConnected()) {
            show_timer_value();
        } else if (vpn_connection_status()) {
            show_timer_value();
        }*/

        if (home_fragment_shared_pref.contains("selected_protocol")) {
            CLICKED_PROTOCOL = home_fragment_shared_pref.getString("selected_protocol", null);
            if (CLICKED_PROTOCOL != null) {
                set_clicked(CLICKED_PROTOCOL);
            }
        }
        premium.setOnClickListener(v -> {

            if(!Splash_Screen.checkPrimumUsers){
            Premium_Class dialog = new Premium_Class(fragmentActivity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialog.show();

            }
            else{

                Toast.makeText(getActivity(),R.string.premium_user,Toast.LENGTH_SHORT).show();
            }
        });
        //onclick events
        changeServerlyt2.setOnClickListener(view1 -> {
            if (fragmentActivity != null) {
                startActivity(new Intent(fragmentActivity, Server_Screen.class));
            }
        });

        auto_protocol.setOnClickListener(view1 -> {
            if (home_fragment_shared_pref != null) {
                SharedPreferences.Editor editor = home_fragment_shared_pref.edit();
                if (home_fragment_shared_pref.contains("selected_protocol")) {
                    editor.remove("selected_protocol").apply();
                }

                CLICKED_PROTOCOL = AUTO_CONNECTION;
                set_clicked(CLICKED_PROTOCOL);
                editor.putString("selected_protocol", CLICKED_PROTOCOL).apply();
            }
        });
        ikev2_protocol.setOnClickListener(view1 -> {
            if (fragmentActivity != null) {
                //perform ikev2
                if (home_fragment_shared_pref != null) {
                    SharedPreferences.Editor editor = home_fragment_shared_pref.edit();
                    if (home_fragment_shared_pref.contains("selected_protocol")) {
                        editor.remove("selected_protocol").apply();
                    }
                    CLICKED_PROTOCOL = IKEV2_CONNECTION;
                    set_clicked(CLICKED_PROTOCOL);
                    editor.putString("selected_protocol", CLICKED_PROTOCOL).apply();
                }
            }
        });
        udp_protocol.setOnClickListener(view1 -> {
            if (fragmentActivity != null) {
                //perform ikev2
                if (home_fragment_shared_pref != null) {
                    SharedPreferences.Editor editor = home_fragment_shared_pref.edit();
                    if (home_fragment_shared_pref.contains("selected_protocol")) {
                        editor.remove("selected_protocol").apply();
                    }
                    CLICKED_PROTOCOL = OVPN_CONNECTION;
                    set_clicked(CLICKED_PROTOCOL);
                    editor.putString("selected_protocol", CLICKED_PROTOCOL).apply();
                }
            }
        });
        InitiaterateDialog();
        connect_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(OpenService.getConnectionState() == OpenConnectManagementThread.STATE_CONNECTED){
                   // Disconnect();
                    dialog_home_Activity.show();
                 //   OpenService.stopVPN();
                }else {
                    //ConnectCommand = true;


                    startVPN();
                    checkConnected=true;


                    //oFirstWorkFirst();

                }
            }
        });
    }

    public void start_connection() {

       /* if (CLICKED_PROTOCOL == null)
            CLICKED_PROTOCOL = AUTO_CONNECTION;
        switch (CLICKED_PROTOCOL) {
            case AUTO_CONNECTION:
                if (isConnected()) {
                    show_already_connected_dialog(IKEV2);
                } else if (vpn_connection_status()) {
                    show_already_connected_dialog(OVPN);
                } else {
                    perform_auto_connection();
                }
                break;

            case OVPN_CONNECTION:
                if (vpn_connection_status()) {
                    show_already_connected_dialog(OVPN);
                } else if (isConnected()) {
                    show_already_connected_dialog(IKEV2);
                } else {
                    perform_ovpn_connection();
                }
                break;

            case IKEV2_CONNECTION:
                if (isConnected()) {
                    show_already_connected_dialog(IKEV2);
                } else if (vpn_connection_status()) {
                    show_already_connected_dialog(OVPN);
                } else {
                    perform_ikev2();
                }
                break;
        }*/
    }

    @SuppressLint("StaticFieldLeak")
    class Perform_Connection extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            show_dialog();
        }

        @Override
        protected void onPostExecute(Void result) {
            start_connection();
        }
    }

    public void connect() {
        Perform_Connection myTask = new Perform_Connection();
        myTask.execute();
    }

    public void show_timer_value() {

        time_info_tv.setOnChronometerTickListener(chronometer -> chronometer.setText(getTimer_value()));
        time_info_tv.start();
    }

    private void start_timer() {
        if (fragmentActivity != null) {
            fragmentActivity.startService(new Intent(fragmentActivity, Connection_Timer.class));
            time_info_tv.setOnChronometerTickListener(cArg -> {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();
                int h = (int) (time / 3600000);
                int m = (int) (time - h * 3600000) / 60000;
                int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                String hh = h < 10 ? "0" + h : h + "";
                String mm = m < 10 ? "0" + m : m + "";
                String ss = s < 10 ? "0" + s : s + "";
                cArg.setText(hh + "" + ":" + mm + "" + ":" + ss + "");
            });

            time_info_tv.setBase(SystemClock.elapsedRealtime());
            time_info_tv.stop();

            time_info_tv.start();
        }
    }

  /*  private void perform_auto_connection() {
        //perform auto connection
        connect_auto();
    }
*/
 /*   private void connect_auto() {
        if (home_fragment_shared_pref != null) {
            if (home_fragment_shared_pref.contains("default_protocol")) {
                String default_protocol = home_fragment_shared_pref.getString("default_protocol", null);
                if (default_protocol != null) {
                    if (default_protocol.equals(IKEV2)) {
                        //connect by ikev2
                        perform_ikev2();
                    } else if (default_protocol.equals(OVPN)) {
                        //ovpn
                        perform_ovpn_connection();
                    }
                }
            }
        }
    }*/

    public void show_disconnect_screen() {
        if (fragmentActivity != null) {
                if(OpenVpnService.countDownTimer!=null){

                      OpenVpnService.countDownTimer.cancel();
                      OpenVpnService.countDownTimer=null;

                }

            fragmentActivity.startActivity(new Intent(fragmentActivity, Disconnect_Feature.class));
        }
    }

  /*  private void perform_ovpn_connection() {
        //perform ovpn connection
        if (getBestServer_after_calculation(home_fragment_shared_pref) != null) {
            byte[] data = new byte[0];
            try {
                if (!Objects.requireNonNull(getBestServer_after_calculation(home_fragment_shared_pref)).getOvpn_config().isEmpty()) {
                    data = Base64.decode(Objects.requireNonNull(getBestServer_after_calculation(home_fragment_shared_pref)).getOvpn_config(), Base64.DEFAULT);
                }
            } catch (Exception e) {
                Log.d("Exception_Cert_value", "" + e.getMessage());
                e.printStackTrace();
            }

            certificate_value = null;
            certificate_value = new String(data, StandardCharsets.UTF_8);

            set_connecting();

            if (!certificate_value.isEmpty()) {
                if (fragmentActivity != null) {

                    Intent intent = VpnService.prepare(fragmentActivity);
                    if (intent != null) {
                        startActivityForResult(intent, REQUEST_CODE);
                    } else startVpn(certificate_value);//have already permission

                    if (timer_connecting != null) {
                        timer_connecting.cancel();
                    }

                    timer_connecting = new CountDownTimer(15000, 1000) {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        public void onTick(long millisUntilFinished) {

                            if (openVpnStatus != null && openVpnStatus.equals(ConnectionStatus.LEVEL_CONNECTED)) {
                                enable_button();
                                fragmentActivity.runOnUiThread(() -> {
                                    vpn_connection_status.setText("");
                                    vpn_connection_status.setText(R.string.connected);
                                    vpn_connection_status.setTextColor(getResources().getColor(R.color.greenButton));
                                    time_info_tv.setVisibility(View.VISIBLE);

//                                    start_service();
                                    start_timer();
                                    ripple.startRippleAnimation();
                                    ripple2.stopRippleAnimation();


                                    if (timer_connecting != null) {
                                        timer_connecting.cancel();

                                        TastyToast.makeText(fragmentActivity, "Vpn Connected! . Buy Subscription to avail amazing service", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                                    }

                                    if (!((fragmentActivity).isFinishing())) {
                                        fragmentActivity.startService(new Intent(fragmentActivity, Connection_Timer.class));

//                            if (!is_premium_home) {
                                        if (Constant.IS_ADMOB_ENABLE) {
                                            showInterstitial("connected");
                                        }
//                            }

                                if (Constant.isValidContextForGlide(fragmentActivity)) {
                                    Glide.with(fragmentActivity).load(R.drawable.btn_greenok).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).into(connect_button);
                                }

                                        connect_button.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        }

                        public void onFinish() {

                            fragmentActivity.runOnUiThread(() -> {

                                if (!((fragmentActivity).isFinishing())) {
                                    reset_values_again();
                                }
                            });

                            time_info_tv.setVisibility(View.GONE);
                            stop_timer();

                            ProfileManager.setConntectedVpnProfileDisconnected(fragmentActivity);
                            if (openvpn_service != null && openvpn_service.getManagement() != null) {
                                openvpn_service.getManagement().stopVPN(false);
                            }

                        }
                    }.start();
                }
            } else {
                Log.d("Activity_null1", "Activity_null");
            }
        }
    }*/

    public void set_connecting() {
        fragmentActivity.runOnUiThread(() -> {
            vpn_connection_status.setText("");
            if (Constant.isValidContextForGlide(fragmentActivity)) {
                Glide.with(fragmentActivity).load(R.drawable.btn_bbb).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).into(connect_button);
            }
            vpn_connection_status.setText(R.string.connecting);
            connect_button.setEnabled(false);
            vpn_connection_status.setTextColor(getResources().getColor(R.color.colorWhite));
            time_info_tv.setVisibility(View.GONE);
             ripple2.startRippleAnimation();
            //ripple.stopRippleAnimation();
        //    connect_button.setVisibility(View.INVISIBLE);
        });

    }

    public void reset_values_again() {
        vpn_connection_status.setText("");
        vpn_connection_status.setText(R.string.connect);
        vpn_connection_status.setTextColor(getResources().getColor(R.color.white));
        Glide.with(fragmentActivity).load(R.drawable.disconnected).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).into(connect_button);

        ripple.stopRippleAnimation();
        ripple2.stopRippleAnimation();
    }



    private void showInterstitial(String disconnect_screen) {
        if (sInterstitial != null) {
            if (disconnect_screen.contains("disconnect")) {
                //disconnect screen..!!
                sInterstitial.show(fragmentActivity);

                sInterstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        show_disconnect_screen();
                        super.onAdFailedToShowFullScreenContent(adError);
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {

                        sInterstitial = null;
                        load_interstitial_Ad(fragmentActivity);
                        show_disconnect_screen();
                        super.onAdDismissedFullScreenContent();
                    }

                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                    }
                });
            } else {
                sInterstitial.show(fragmentActivity);

                sInterstitial.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {

                        sInterstitial = null;
                        if(checTimerbtn) {
                            OpenVpnService.totalSeconds = OpenVpnService.TimeDown + Constant.timerValue * 60;
                             checTimerbtn=false;
                            // OpenVpnService.countDownTimer.onFinish();
                            if(OpenVpnService.countDownTimer!=null){

                                OpenVpnService.countDownTimer.cancel();
                                OpenVpnService.countDownTimer=null;

                            }
                            OpenService.getTimeV();

                        }
                        load_interstitial_Ad(fragmentActivity);
                        super.onAdDismissedFullScreenContent();
                    }

                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                    }
                });
            }
        }
        else {
            if (disconnect_screen.contains("disconnect")) {
                show_disconnect_screen();
                //Added
                load_interstitial_Ad(fragmentActivity);
            }
            Toast.makeText(getActivity(), "No Ads Loaded try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void load_interstitial_Ad(Context context) {

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(context, ADMOB_INTERSETIAL_AD, adRequest,
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
    }

    public void disable_button() {
        connect_button.setEnabled(false);
        connect_button.setClickable(false);
        protocol.setEnabled(false);
        protocol.setClickable(false);
    }

    public void enable_button() {
        connect_button.setEnabled(true);
        connect_button.setClickable(true);
        protocol.setEnabled(true);
        protocol.setClickable(true);
    }




    private void populateUnifiedNativeAdView_home_2(NativeAd nativeAd, NativeAdView adView) {

        Log.d("refresh_premium", "" + nativeAd.getBody()
                + "    " + nativeAd.getHeadline() + "    " + nativeAd.getPrice() + "   " + nativeAd.getMediaContent());
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        if (adView.getHeadlineView() != null) {
            ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        }

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
    }

// Added
    /*private void refreshAd() {

        if (isAdded() && fragmentActivity != null) {
            AdLoader.Builder builder = new AdLoader.Builder(fragmentActivity, ADMOB_NATIVE_TESTING);

            builder.forNativeAd(nativeAd -> {
                native_refresh = null;
                native_refresh = nativeAd;

                FrameLayout frameLayout = dialog_home_Activity.findViewById(R.id.dscnt_ad);
                NativeAdView adView = (NativeAdView) fragmentActivity.getLayoutInflater().inflate(R.layout.disconnect_dialog_ad_layout, null);
                populateUnifiedNativeAdView_home_2(native_refresh, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            });

            VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(true).build();
            NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

            builder.withNativeAdOptions(adOptions);

            AdLoader adLoader = builder.withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(@NotNull LoadAdError loadAdError) {
                }
            })
                    .build();
            adLoader.loadAd(new AdRequest.Builder().build());
//        if (fragmentActivity != null) {
//            AdLoader.Builder builder = new AdLoader.Builder(fragmentActivity, ADMOB_NATIVE_TESTING);
//
//            builder.forNativeAd(nativeAd -> {
//                nativeAd.destroy();
//                native_refresh = null;
//
//                native_refresh = nativeAd;
//                FrameLayout frameLayout = dialog_home_Activity.findViewById(R.id.dscnt_ad);
//
//                // Check the fragment status
//                NativeAdView adView = (NativeAdView) fragmentActivity.getLayoutInflater().inflate(R.layout.disconnect_dialog_ad_layout, null);
//                populateUnifiedNativeAdView_home2(native_refresh, adView);
////                frameLayout.removeAllViews();
//                frameLayout.addView(adView);
//            });
////            builder.forUnifiedNativeAd(unifiedNativeAd -> {
////                if (nativeAd_home != null) {
////                    nativeAd_home.destroy();
////                }
////                nativeAd_home = unifiedNativeAd;
////                FrameLayout frameLayout = dialog_home_Activity.findViewById(R.id.dscnt_ad);
////
////                if (isAdded()) {
////                    // Check the fragment status
////                    @SuppressLint("InflateParams") UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater().inflate(R.layout.disconnect_dialog_ad_layout, null);
////                    populateUnifiedNativeAdView(unifiedNativeAd, adView);
////                    frameLayout.removeAllViews();
////                    frameLayout.addView(adView);
////                }
////            });
//
//            VideoOptions videoOptions = new VideoOptions.Builder()
//                    .setStartMuted(false)
//                    .build();
//
//            NativeAdOptions adOptions = new NativeAdOptions.Builder()
//                    .setVideoOptions(videoOptions)
//                    .build();
//
//            builder.withNativeAdOptions(adOptions);
//
//            AdLoader adLoader = builder.withAdListener(new AdListener() {
//                @Override
//                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                    super.onAdFailedToLoad(loadAdError);
//                }
//
//                @Override
//                public void onAdLoaded() {
//                    super.onAdLoaded();
//                }
//            }).build();
//
//            adLoader.loadAd(new AdRequest.Builder().build());
        }
    }*/
    private void refreshAdMain() {

        if (isAdded() && fragmentActivity != null) {
            AdLoader.Builder builder = new AdLoader.Builder(fragmentActivity, ADMOB_NATIVE_TESTING);

            builder.forNativeAd(nativeAd -> {
                native_refresh = null;
                native_refresh = nativeAd;
                RelativeLayout adContainer = view.findViewById(R.id.adMobView_home_screen);

              //  FrameLayout frameLayout = dialog_home_Activity.findViewById(R.id.dscnt_ad);
                NativeAdView adView = (NativeAdView) fragmentActivity.getLayoutInflater().inflate(R.layout.disconnect_dialog_ad_layoutsmall, null);
                populateUnifiedNativeAdView_home_2(native_refresh, adView);
                adContainer.removeAllViews();
                adContainer.addView(adView);
            });

            VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(true).build();
            NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

            builder.withNativeAdOptions(adOptions);

            AdLoader adLoader = builder.withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(@NotNull LoadAdError loadAdError) {
                        }
                    })
                    .build();
            adLoader.loadAd(new AdRequest.Builder().build());
//        if (fragmentActivity != null) {
//            AdLoader.Builder builder = new AdLoader.Builder(fragmentActivity, ADMOB_NATIVE_TESTING);
//
//            builder.forNativeAd(nativeAd -> {
//                nativeAd.destroy();
//                native_refresh = null;
//
//                native_refresh = nativeAd;
//                FrameLayout frameLayout = dialog_home_Activity.findViewById(R.id.dscnt_ad);
//
//                // Check the fragment status
//                NativeAdView adView = (NativeAdView) fragmentActivity.getLayoutInflater().inflate(R.layout.disconnect_dialog_ad_layout, null);
//                populateUnifiedNativeAdView_home2(native_refresh, adView);
////                frameLayout.removeAllViews();
//                frameLayout.addView(adView);
//            });
////            builder.forUnifiedNativeAd(unifiedNativeAd -> {
////                if (nativeAd_home != null) {
////                    nativeAd_home.destroy();
////                }
////                nativeAd_home = unifiedNativeAd;
////                FrameLayout frameLayout = dialog_home_Activity.findViewById(R.id.dscnt_ad);
////
////                if (isAdded()) {
////                    // Check the fragment status
////                    @SuppressLint("InflateParams") UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater().inflate(R.layout.disconnect_dialog_ad_layout, null);
////                    populateUnifiedNativeAdView(unifiedNativeAd, adView);
////                    frameLayout.removeAllViews();
////                    frameLayout.addView(adView);
////                }
////            });
//
//            VideoOptions videoOptions = new VideoOptions.Builder()
//                    .setStartMuted(false)
//                    .build();
//
//            NativeAdOptions adOptions = new NativeAdOptions.Builder()
//                    .setVideoOptions(videoOptions)
//                    .build();
//
//            builder.withNativeAdOptions(adOptions);
//
//            AdLoader adLoader = builder.withAdListener(new AdListener() {
//                @Override
//                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                    super.onAdFailedToLoad(loadAdError);
//                }
//
//                @Override
//                public void onAdLoaded() {
//                    super.onAdLoaded();
//                }
//            }).build();
//
//            adLoader.loadAd(new AdRequest.Builder().build());
        }
    }

    public void InitiateDiscountDialog() {

        if (fragmentActivity != null) {
            dialog_home_Activity = new Dialog(fragmentActivity);
            dialog_home_Activity.setContentView(R.layout.disconnect_window_layout);
            Button Dis_disconnect = dialog_home_Activity.findViewById(R.id.btn_dscnt);
            Button Dis_Cancle = dialog_home_Activity.findViewById(R.id.btn_cncl);


            Dis_disconnect.setOnClickListener(view -> {

                       OpenService.stopVPN();
                      // timeV=time_info_tv.getText().toString();
                    //strongswan connected
                    dialog_home_Activity.dismiss();

                        showInterstitial("disconnect");



            });

            Dis_Cancle.setOnClickListener(view -> dialog_home_Activity.dismiss());
            dialog_home_Activity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog_home_Activity.setCancelable(false);
        }
    }


    private void set_clicked(String selected_protocol) {

        if (selected_protocol == null)
            selected_protocol = AUTO_CONNECTION;

        switch (selected_protocol) {
            case AUTO_CONNECTION:
                udp_protocol.setTextColor(getResources().getColor(R.color.colorWhite));
                auto_protocol.setTextColor(getResources().getColor(R.color.app_background));
                ikev2_protocol.setTextColor(getResources().getColor(R.color.colorWhite));
                break;

            case OVPN_CONNECTION:
                udp_protocol.setTextColor(getResources().getColor(R.color.app_background));
                ikev2_protocol.setTextColor(getResources().getColor(R.color.colorWhite));
                auto_protocol.setTextColor(getResources().getColor(R.color.colorWhite));
                break;

            case IKEV2_CONNECTION:
                udp_protocol.setTextColor(getResources().getColor(R.color.colorWhite));
                ikev2_protocol.setTextColor(getResources().getColor(R.color.app_background));
                auto_protocol.setTextColor(getResources().getColor(R.color.colorWhite));
                break;
        }
    }

    public static boolean isHostReachable(String serverAddress, int serverTCPport, int timeoutMS){
        boolean connected = false;
        Socket socket;
        try {
            socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(serverAddress, serverTCPport);
            socket.connect(socketAddress, timeoutMS);
            if (socket.isConnected()) {
                connected = true;
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket = null;
        }
        return connected;
    }
    private VPNConnector mConn;
    @Override
    public void onResume() {
        set_country();
        set_press_to_connect_image();

        mConn = new VPNConnector(getActivity(), false) {
            @Override
            public void onUpdate(OpenVpnService service) {
                OpenService = service;
                UpdateUI(service);
                if(OpenService.getConnectionState()==OpenConnectManagementThread.STATE_CONNECTED){

                  //  Log.e("bestserver===>>>","host ip is working ==>>"+OpenVpnService.registerKeepAlive(););

                }
               /* if(OnOpen){
                    if(OpenService.getConnectionState() == OpenConnectManagementThread.STATE_CONNECTED){
                        prefManager = new PrefManager(getBaseContext(),PrefManager.PRF_APP_DATA,PrefManager.MODE_READ);
                        SpinerIndex = prefManager.ReadInt(PrefManager.KEY_SPINER_INDEX);
                        check =true;
//						ProShield.setImageResource(R.drawable.sheild);
                    }else {
                        prefManager = new PrefManager(getBaseContext(),PrefManager.PRF_APP_DATA,PrefManager.MODE_WRITE);
                        SpinerIndex = 0;
                        check = false;
                        prefManager.SaveIntData(PrefManager.KEY_SPINER_INDEX,SpinerIndex);
//						ProShield.setImageResource(R.drawable.shield_off);
                    }
                    //LocationView.setText(DataManager.Server_NameS[SpinerIndex]);
                    Img_Flg.setImageResource(DataManager.FlagIds[SpinerIndex]);
                    OnOpen = false;*/
               /* }*/


            }


        };

        super.onResume();
    }

    private void set_country() {
        if (home_fragment_shared_pref != null) {
            String default_country = Objects.requireNonNull(getBestServer_after_calculation(home_fragment_shared_pref)).getHost_name();
            if (default_country != null && !default_country.isEmpty()) {
                changeServer.setText(default_country);
                countryFlagPicker_home = new Country_Flag_Picker.Builder().with(fragmentActivity).listener(this).build();
                countryName = countryFlagPicker_home.getCountryByName(default_country);
                if (countryName != null) {
                    ivFlag.setImageBitmap(decodeSampledBitmapFromResource(fragmentActivity.getResources(), countryName.getFlag(), 50, 50));
                } else {
                    ivFlag.setImageResource(R.drawable.flag_us);
                }
            } else {
                ivFlag.setImageResource(R.drawable.flag_us);
            }
        }
    }

    private void set_press_to_connect_image() {
       /* if (fragmentActivity != null) {
            if (isConnected()) {
                vpn_connection_status.setText("");
                vpn_connection_status.setText(R.string.connected);
                time_info_tv.setVisibility(View.VISIBLE);
            } else if (vpn_connection_status()) {
                vpn_connection_status.setText("");
                vpn_connection_status.setText(R.string.connected);
                time_info_tv.setVisibility(View.VISIBLE);
                vpn_connection_status.setTextColor(getResources().getColor(R.color.greenButton));
            } else {
                reset_values_again();
            }*/
//            if (!isConnected()) {
//                if (vpn_connection_status()) {
//
//                    vpn_connection_status.setText("");
//                    vpn_connection_status.setText(R.string.connected);
//                    time_info_tv.setVisibility(View.VISIBLE);
//                    TastyToast.makeText(fragmentActivity, "Vpn Connected", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
//
////                    if (Constant.isValidContextForGlide(fragmentActivity)) {
////                        Glide.with(fragmentActivity).load(R.drawable.vpn_connected_icon).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).into(connect_button);
////                    }
//                } else {
//                    time_info_tv.setVisibility(View.GONE);
//                }
//            } else if (isConnected()) {
//                vpn_connection_status.setText("");
//                vpn_connection_status.setText(R.string.connected);
//                time_info_tv.setVisibility(View.VISIBLE);
//
////                if (Constant.isValidContextForGlide(fragmentActivity)) {
////                    Glide.with(fragmentActivity).load(R.drawable.vpn_connected_icon).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).into(connect_button);
////                }
//            }

    }

    @Override
    public void onSelectCountry(Country_Names countryNames) {

    }
PrefManager prefManager;
    public  void startVPN() {

        Toast.makeText(getActivity(), "Wait Connecting!" ,
                Toast.LENGTH_LONG).show();
        Intent prepIntent;
        try {
            prepIntent = VpnService.prepare(getActivity());
        } catch (Exception e) {
            //	reportBadRom(e);
            //finish();
            return;
        }

        if (prepIntent != null) {
            try {
                startActivityForResult(prepIntent, 0);
            } catch (Exception e) {
                //	reportBadRom(e);
                //finish();
                return;
            }
        } else {
            onActivityResult(0, RESULT_OK, null);
        }
    }

    SharedPreferences sharedPreferences;

    /* Called by Android OS after user clicks "OK" on VpnService.prepare() dialog */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity().setResult(resultCode);
        Log.e("tryresult==>> ",OpenConnectManagementThread.iptoConnect);

        //SelectedUUID = ProfileManager.
        if (resultCode == RESULT_OK) {
            int n;

                sharedPreferences = getActivity().getSharedPreferences("DATA", MODE_PRIVATE);
                api_response Api_response_model = Constant.getBestServer_after_calculation(sharedPreferences);
                //  n=Api_response_model.getServer_id();
                Glob.username = Api_response_model.getUsername();
                Glob.password = Api_response_model.getPassword();
                ProfileManager.mProfiles.clear();

                String s = ProfileManager.create(Api_response_model.getIp_Address()).getUUID().toString();
                // String s = ProfileManager.create(DataManager.Server_IPS[0]).getUUID().toString();
                Log.e("apires==>>>",Api_response_model.getIp_Address());
                Intent intent = new Intent(getActivity(), OpenVpnService.class);
                intent.putExtra(OpenVpnService.EXTRA_UUID, s);
                getActivity().startService(intent);




        }
        // finish();
    }
    public void UpdateUI(OpenVpnService service){
        int state = service.getConnectionState();
        service.startActiveDialog(getActivity());


        if (mConnectionState != state) {

            mConnectionState = state;
        }
         if(state == OpenConnectManagementThread.STATE_CONNECTING){
             //tvTime.setVisibility(View.INVISIBLE);
             lineTimer.setVisibility(View.INVISIBLE);
             set_connecting();
            // connect_button.setVisibility(View.VISIBLE);
             connect_button.setEnabled(false);

             /*
             vpn_connection_status.setText("");
             vpn_connection_status.setText(R.string.connecting);
             connect_button.setEnabled(false);
             vpn_connection_status.setTextColor(getActivity().getResources().getColor(R.color.pup));
             time_info_tv.setVisibility(View.GONE);
             ripple2.startRippleAnimation();*/

         }
         if(state== OpenConnectManagementThread.STATE_DISCONNECTED){
             OpenVpnService.TimeDown= Constant.timerValue * 60;
             OpenVpnService.totalSeconds=Constant.timerValue * 60;
             lineTimer.setVisibility(View.INVISIBLE);
             reset_values_again();
             connect_button.setVisibility(View.VISIBLE);
             connect_button.setEnabled(true);
             time_info_tv.setVisibility(View.GONE);

         }
        if (state == OpenConnectManagementThread.STATE_CONNECTED) {
            vpn_connection_status.setText(R.string.connected);
            connect_button.setVisibility(View.VISIBLE);
            ripple2.stopRippleAnimation();
         //   OpenVpnService.formatElapsedTime(service.startTime.getTime());
          //  tvTime.setVisibility(View.VISIBLE);

            time_info_tv.setVisibility(View.VISIBLE);
            if(!is_premium) {
                if(Constant.timerCheck) {
                    lineTimer.setVisibility(View.VISIBLE);

                    time_info_tv.setText(OpenVpnService.formatElapsedT(OpenVpnService.TimeDown));
                }


            }
            else {

              //  time_info_tv.setText(OpenVpnService.formatElapsedTime(service.startTime.getTime()));
               // tvTime.setVisibility(View.INVISIBLE);
               lineTimer.setVisibility(View.INVISIBLE);
            }
            Glide.with(fragmentActivity).load(R.drawable.connected).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).into(connect_button);
            connect_button.setVisibility(View.VISIBLE);
            connect_button.setEnabled(true);
            timeV=OpenVpnService.formatElapsedTime(service.startTime.getTime());
           // ripple.startRippleAnimation();
            if (Constant.IS_ADMOB_ENABLE && checkConnected) {
                checkConnected=false;

                showInterstitial("connected");

                TastyToast.makeText(fragmentActivity, "Vpn Connected!", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

            }
          /*  Glide.with(this)
                    .load(R.drawable.connected)
                    .into(PlayButton);
            contectionStatus.setText("Connected");
            downImg.setImageResource(R.drawable.downloading_connect);
            upIMg.setImageResource(R.drawable.uploading_connect);
            lottieAnimationView.pauseAnimation();
            lottieAnimationView.setVisibility(View.INVISIBLE);
            PlayButton.setVisibility(View.VISIBLE);
            DownInfo.setText(

                    OpenVpnService.humanReadableByteCount(mConn.newStats.rxBytes, false)
            );
//			TimeInfo.setText(OpenVpnService.formatElapsedTime(service.startTime.getTime()));
            UpInfo.setText(
                    OpenVpnService.humanReadableByteCount(mConn.newStats.txBytes, false));

        }else if (mConnectionState == OpenConnectManagementThread.STATE_DISCONNECTED) {
            Glide.with(this)
                    .load(R.drawable.disconnect)
                    .into(PlayButton);
            contectionStatus.setText("Disconnected");
            lottieAnimationView.pauseAnimation();
            lottieAnimationView.setVisibility(View.INVISIBLE);
            PlayButton.setVisibility(View.VISIBLE);
            DownInfo.setText("0 Mbps");
            UpInfo.setText("0 Mbps");
            downImg.setImageResource(R.drawable.downloaing_dis_graph);
            upIMg.setImageResource(R.drawable.disconnectupload_graph);*/
        }

    }
    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(fragmentActivity).unregisterReceiver(connect_vpn);

        super.onDestroy();

    }

    @Override
    public void onPause() {
        mConn.stopActiveDialog();
        mConn.unbind();

        super.onPause();
    }
    private RewardedAd mRewardedAd;
    public void RewardRequest(){

        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(getActivity(), Constant.ADMOB_Reward_TESTING,
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.toString());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;

                        ServerSideVerificationOptions options = new ServerSideVerificationOptions
                                .Builder()
                                .setCustomData("SAMPLE_CUSTOM_DATA_STRING")
                                .build();
                        mRewardedAd.setServerSideVerificationOptions(options);
                        Log.d(TAG, "Ad was loaded.");
                    }
                });


    }

    public void ShoeRD(){


        if (mRewardedAd != null) {
            Activity activityContext = getActivity();
            mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.d(TAG, "The user earned the reward.");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                    Home_Activity.checkTimeV=false;
                    prefManager = new PrefManager(getActivity(),PrefManager.PRF_APP_DATA,PrefManager.MODE_WRITE);
                    //val =  prefManager.ReadInt(PrefManager.KEYTIME) + Integer.parseInt(ch);
                    prefManager.SaveIntData(PrefManager.KEYTIME,0);
                }
            });

            mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    // Called when a click is recorded for an ad.
                    Log.d(TAG, "Ad was clicked.");
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Set the ad reference to null so you don't show the ad a second time.
                    Log.d(TAG, "Ad dismissed fullscreen content.");
                    mRewardedAd = null;
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    // Called when ad fails to show.
                    Log.e(TAG, "Ad failed to show fullscreen content.");
                    mRewardedAd = null;
                }

                @Override
                public void onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    Log.d(TAG, "Ad recorded an impression.");
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    Log.d(TAG, "Ad showed fullscreen content.");
                }
            });

        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.");
        }
    }
    RewardedInterstitialAd rewardedInterstitialAd;
    private void showRewardedVideo() {

        if (rewardedInterstitialAd == null) {
            Toast.makeText(getActivity(), "No Video Load at this time", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        rewardedInterstitialAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    /** Called when ad showed the full screen content. */
                    @Override
                    public void onAdShowedFullScreenContent() {
                        Log.d(TAG, "onAdShowedFullScreenContent");

                        Toast.makeText(getActivity(), "onAdShowedFullScreenContent", Toast.LENGTH_SHORT)
                                .show();
                    }

                    /** Called when the ad failed to show full screen content. */
                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        Log.d(TAG, "onAdFailedToShowFullScreenContent: " + adError.getMessage());

                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedInterstitialAd = null;
                        //loadRewardedInterstitialAd();

                        Toast.makeText(
                                        getActivity(), "onAdFailedToShowFullScreenContent", Toast.LENGTH_SHORT)
                                .show();
                    }

                    /** Called when full screen content is dismissed. */
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedInterstitialAd = null;
                        Log.d(TAG, "onAdDismissedFullScreenContent");
                        Toast.makeText(getActivity(), "onAdDismissedFullScreenContent", Toast.LENGTH_SHORT)
                                .show();
                        // Preload the next rewarded interstitial ad.
                       // loadRewardedInterstitialAd();
                    }
                });

        Activity activityContext = getActivity();
        rewardedInterstitialAd.show(
                activityContext,
                new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        // Handle the reward.
                        Log.d(TAG, "The user earned the reward.");
                        Home_Activity.checkTimeV=false;
                        prefManager = new PrefManager(getActivity(),PrefManager.PRF_APP_DATA,PrefManager.MODE_WRITE);
                        //val =  prefManager.ReadInt(PrefManager.KEYTIME) + Integer.parseInt(ch);
                        prefManager.SaveIntData(PrefManager.KEYTIME,0);
                     //   addCoins(rewardItem.getAmount());
                    }
                });
    }

    private void loadRewardedInterstitialAd() {
        if (rewardedInterstitialAd == null) {
           // isLoadingAds = true;

            AdRequest adRequest = new AdRequest.Builder().build();
            // Use the test ad unit ID to load an ad.
            RewardedInterstitialAd.load(
                    getActivity(),
                    Constant.ADMOB_Reward_TESTING,
                    adRequest,
                    new RewardedInterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(RewardedInterstitialAd ad) {
                            Log.d(TAG, "onAdLoaded");

                            rewardedInterstitialAd = ad;
                            //isLoadingAds = false;
                            Toast.makeText(getActivity(), "onAdLoaded", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            Log.d(TAG, "onAdFailedToLoad: " + loadAdError.getMessage());

                            // Handle the error.
                            rewardedInterstitialAd = null;
                         //   isLoadingAds = false;
                            Toast.makeText(getActivity(), "onAdFailedToLoad", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
Dialog RateDialog,firstDialog;
    public void InitiaterateDialog() {
        this.RateDialog = new Dialog(getActivity());
        this.RateDialog.setContentView(R.layout.rating_window);
        this.RateDialog.setCancelable(true);
        TextView rat=RateDialog.findViewById(R.id.textView14);
        TextView rat16=RateDialog.findViewById(R.id.textView16);

      //  rat.setTypeface(type);
      //  rat16.setTypeface(type);
        ((TextView) this.RateDialog.findViewById(R.id.btn_rt)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Premium_Class dialog = new Premium_Class(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.show();
                RateDialog.dismiss();
               // MainActivity2 mainActivity2 = MainActivity2.this;
              //  PrefManager unused = mainActivity2.prefManager = new PrefManager(mainActivity2.getBaseContext(), PrefManager.PRF_APP_DATA, PrefManager.MODE_WRITE);
             //   MainActivity2.this.prefManager.SaveIntData(PrefManager.KEY_RATE_INDEX, 420);
               // int unused2 = MainActivity2.this.RateIndex = 420;
               // MainActivity2.this.RateDialog.dismiss();
               // MainActivity2.this.Rate();
            }
        });
        ((TextView) this.RateDialog.findViewById(R.id.btn_later)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

              //  RateDialog.dismiss();

                showInterstitial("connected");
                RateDialog.dismiss();
                //finish();
                ///finishAffinity();

            }
        });

        this.RateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

    }

    public void InitiateFirstDialog() {
        this.firstDialog = new Dialog(getActivity());
        this.firstDialog.setContentView(R.layout.update_window);
        if(!Constant.canlableDialog) {
            this.firstDialog.setCancelable(true);
        }
        else {

            this.firstDialog.setCancelable(false);
        }

        TextView ratdec=firstDialog.findViewById(R.id.textView16);

        ratdec.setText(Constant.discription);

        TextView rat16=firstDialog.findViewById(R.id.btn_now);
                // rat.setTypeface(type);
        //  rat16.setTypeface(type);
        rat16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.appURl)));


            }
        });

        ((TextView) this.firstDialog.findViewById(R.id.btn_later)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //  RateDialog.dismiss();

             //   showInterstitial("connected");
                if(!Constant.canlableDialog) {
                    firstDialog.dismiss();
                }
                else {

                    Toast.makeText(getActivity(),"Must Update",Toast.LENGTH_SHORT).show();
                }

                //finish();
                ///finishAffinity();

            }
        });

        this.firstDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        if(Constant.showDialogUpdate){

            firstDialog.show();

        }
    }
}