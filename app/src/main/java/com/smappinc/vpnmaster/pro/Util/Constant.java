package com.smappinc.vpnmaster.pro.Util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.smappinc.vpnmaster.pro.Model.api_response;
import com.smappinc.vpnmaster.pro.R;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;

public class Constant {

    public static String ADMOB_NATIVE_TESTING;
    public static String ADMOB_Reward_TESTING;
    public static String ADMOB_BANNER_AD;
    public static String ADMOB_INTERSETIAL_AD;
    public static String DEFAULT_PROTOCOL="1";
    public static boolean IS_ADMOB_ENABLE;
    public static String json_value = "";
    public static ArrayList<PackageInfo> appInfos;
    public static String SERVER_URL = "https://vpnmasterproapp.com/admin/api/api1.php";
    public static boolean IS_RUN = false;
    public static final int REQUEST_CODE = 1;
    public static Boolean showDialogUpdate=false;
    public static Boolean canlableDialog=false;
    public static String Version;
    public static String discription;
    public static String appURl;
    public static Long timerValue;
    public static Boolean timerCheck;
    //Connection fields
    public static String CLICKED_PROTOCOL;

    public static final String AUTO_CONNECTION = "auto_connection";
    public static final String OVPN_CONNECTION = "ovpn_connect";
    public static final String IKEV2_CONNECTION = "ikev2_connect";


    //fields
    public static final String OVPN = "openvpn";
    public static final String IKEV2 = "ikev2";

    public static boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }


    public static boolean vpn_connection_status() {

        String iface = "";
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.isUp())
                    iface = networkInterface.getName();
                if (iface.contains("tun") || iface.contains("ppp") || iface.contains("pptp")) {
                    return true;
                }
            }
        } catch (SocketException e1) {
            e1.printStackTrace();
        }

        return false;
    }

    public static void storeValueToPreference(SharedPreferences sharedPreferences, String key, Object object) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(object);
            if (sharedPreferences.contains(key)) {
                editor.remove(key).apply();
            }
            editor.putString(key, json);
            editor.apply();
        }

    }

    public static api_response getBestServer_after_calculation(SharedPreferences sharedPreferences) {
        api_response responsePojo = (api_response) getPreference("selected_server", sharedPreferences);
        if (responsePojo != null) {
            if (responsePojo.getHost_name() != null) {
                return responsePojo;
            } else {
                return null;
            }
        }

        return null;
    }





    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }


    public static Object getPreference(String key, SharedPreferences global_sharedPreferences) {
        Gson gson = new Gson();
        if (global_sharedPreferences != null) {
            json_value = global_sharedPreferences.getString(key, null);
        }

        if (json_value != null) {
            if (!json_value.isEmpty()) {
                try {
                    return gson.fromJson(json_value, api_response.class);
                } catch (JsonSyntaxException | IllegalStateException e) {
                    e.printStackTrace();
                }
            } else {
                return null;
            }
        }

        return null;
    }

    public static boolean isValidContextForGlide(final Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            return !activity.isDestroyed() && !activity.isFinishing();
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void set_status_bar(Activity activity) {
        try {
            Window window = activity.getWindow();
           window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
           window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.transparent));
          //  Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


