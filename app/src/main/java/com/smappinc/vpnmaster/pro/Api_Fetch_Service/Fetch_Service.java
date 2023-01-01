package com.smappinc.vpnmaster.pro.Api_Fetch_Service;import android.content.Intent;import android.content.SharedPreferences;import android.content.pm.ApplicationInfo;import android.content.pm.PackageInfo;import android.content.pm.PackageManager;import android.os.Build;import android.os.IBinder;import android.util.Log;import androidx.annotation.NonNull;import androidx.annotation.Nullable;import androidx.annotation.RequiresApi;import androidx.core.app.JobIntentService;import androidx.localbroadcastmanager.content.LocalBroadcastManager;import com.android.volley.DefaultRetryPolicy;import com.android.volley.Request;import com.android.volley.RequestQueue;import com.android.volley.toolbox.StringRequest;import com.android.volley.toolbox.Volley;import com.smappinc.vpnmaster.pro.Activity.Splash_Screen;import com.smappinc.vpnmaster.pro.Model.api_response;import com.smappinc.vpnmaster.pro.Util.Constant;import com.smappinc.vpnmaster.pro.Util.TinyDB;import org.jetbrains.annotations.NotNull;import org.json.JSONArray;import org.json.JSONException;import org.json.JSONObject;import java.io.Serializable;import java.util.ArrayList;import java.util.List;import java.util.Random;import static com.smappinc.vpnmaster.pro.Util.Constant.ADMOB_BANNER_AD;import static com.smappinc.vpnmaster.pro.Util.Constant.ADMOB_INTERSETIAL_AD;import static com.smappinc.vpnmaster.pro.Util.Constant.ADMOB_NATIVE_TESTING;import static com.smappinc.vpnmaster.pro.Util.Constant.DEFAULT_PROTOCOL;import static com.smappinc.vpnmaster.pro.Util.Constant.IS_ADMOB_ENABLE;import static com.smappinc.vpnmaster.pro.Util.Constant.SERVER_URL;import static com.smappinc.vpnmaster.pro.Util.Constant.storeValueToPreference;import static com.smappinc.vpnmaster.pro.core.OpenVpnService.totalSeconds;public class Fetch_Service extends JobIntentService {    SharedPreferences fetch_service_sp;    api_response api_model, api_model_exclude_type;   public static ArrayList<api_response> api_array;    int random_selection = 0;    PackageManager packageManager;    List<PackageInfo> packageList1;    @Override    public void onCreate() {        super.onCreate();    }    @Override    public void onDestroy() {        super.onDestroy();    }    @Nullable    @Override    public IBinder onBind(@NotNull Intent intent) {        return null;    }    @Override    protected void onHandleWork(@NonNull Intent intent) {    }    @RequiresApi(api = Build.VERSION_CODES.N)    @Override    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {        startWork();        return super.onStartCommand(intent, flags, startId);    }    @RequiresApi(api = Build.VERSION_CODES.N)    private void startWork() {        init_array_list();        getdatafromapi();    }    private void init_array_list() {        fetch_service_sp = getSharedPreferences("DATA", MODE_PRIVATE);        api_array = new ArrayList<>();    }    @RequiresApi(api = Build.VERSION_CODES.N)    private void getdatafromapi() {        hit_api();    }    @RequiresApi(api = Build.VERSION_CODES.N)    private void hit_api() {        RequestQueue queue = Volley.newRequestQueue(Fetch_Service.this);        StringRequest stringRequest = new StringRequest(Request.Method.GET, SERVER_URL, this::parse_data, error -> {            Intent intent = new Intent("all_data_fetched").putExtra("data_fetched", false);            LocalBroadcastManager.getInstance(Fetch_Service.this).sendBroadcast(intent);        });        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, 3, 3));        queue.add(stringRequest);    }    JSONArray json_timer;    JSONArray json_settings;    @RequiresApi(api = Build.VERSION_CODES.N)    private void parse_data(String input) {        api_array.clear();        try {            JSONObject jsonObject = new JSONObject(input);            JSONArray jsonArray = jsonObject.getJSONArray("Servers");             json_settings  = jsonObject.getJSONArray("Settings");             JSONArray json_update = jsonObject.getJSONArray("Update");              json_timer = jsonObject.getJSONArray("Timer");            if (jsonArray.length() > 0) {                for (int i = 0; i < jsonArray.length(); i++) {                    JSONObject object = jsonArray.getJSONObject(i);                    try {                        api_model = new api_response();                        api_model.setHost_name(object.getString("HostName"));                        api_model.setIp_Address(object.getString("IP"));                        api_model.setCity(object.getString("Flag"));                       // api_model.setCity(null);                        api_model.setOvpn_config(object.getString("Flag"));                        api_model.setIkev2_config(object.getString("Flag"));                        api_model.setUsername(object.getString("user"));                        api_model.setPassword(object.getString("password"));                        api_model.set_type(object.getString("Type"));                        api_array.add(api_model);                    } catch (JSONException e) {                        Log.e("JSONException", "" + e.getMessage());                        e.printStackTrace();                    }                }                 for(int bb=0;bb<api_array.size();bb++){                     Log.e("api_array_size", "" + api_array.get(bb).toString());                 }                if (fetch_service_sp != null) {                    if (api_array != null && !api_array.isEmpty()) {                        TinyDB tinydb = new TinyDB(Fetch_Service.this);                        tinydb.put_array_list_from_cache("saved_list", api_array);                        List<api_response> newList = new ArrayList<>(api_array);                        if(Splash_Screen.checkPrimumUsers){/*                           // newList.removeIf(p -> p.get_type().equals("2"));                            its finish sir now it will open for premin users..test it if found any issue contact me then ok sir??any other issue??                            ok sir                            No other issue, let me test now */                        }                        else {                            newList.removeIf(p -> p.get_type().equals("2"));                        }                        Random randomGenerator = new Random();                        random_selection = randomGenerator.nextInt(newList.size());                        api_model_exclude_type = new api_response();                        api_model_exclude_type.setHost_name(newList.get(random_selection).getHost_name());                        api_model_exclude_type.setIp_Address(newList.get(random_selection).getIp_Address());                        api_model_exclude_type.setCity(null);                      //  api_model_exclude_type.setOvpn_config(newList.get(random_selection).getOvpn_config());                        //api_model_exclude_type.setIkev2_config(newList.get(random_selection).getIkev2_config());                        api_model_exclude_type.setUsername(newList.get(random_selection).getUsername());                        api_model_exclude_type.setPassword(newList.get(random_selection).getPassword());                        api_model_exclude_type.set_type(newList.get(random_selection).get_type());                        storeValueToPreference(fetch_service_sp, "selected_server", api_model_exclude_type);                        get_Update_ids(json_update);                    }                }            }        } catch (JSONException e) {            Intent intent = new Intent("all_data_fetched").putExtra("data_fetched", false);            LocalBroadcastManager.getInstance(Fetch_Service.this).sendBroadcast(intent);            Log.d("JSONException22", "" + e.getMessage());            e.printStackTrace();        }    }    private void get_Admob_ids(JSONArray response) {        Log.d("Response_Val", "" + response);        if (response.length() > 0) {            for (int i = 0; i < response.length(); i++) {                try {                    JSONObject object = response.getJSONObject(i);                    ADMOB_NATIVE_TESTING =object.getString("admob_native");                    ADMOB_BANNER_AD = object.getString("admob_banner");                    ADMOB_INTERSETIAL_AD =object.getString("admob_inter");                    Constant.ADMOB_Reward_TESTING =object.getString("admob_reward");                    IS_ADMOB_ENABLE =object.getBoolean("active");                } catch (JSONException e) {                    Intent intent = new Intent("all_data_fetched").putExtra("data_fetched", false);                    LocalBroadcastManager.getInstance(Fetch_Service.this).sendBroadcast(intent);                    e.printStackTrace();                }            }        }            DEFAULT_PROTOCOL = "1";            if (fetch_service_sp != null) {                SharedPreferences.Editor editor = fetch_service_sp.edit();                if (fetch_service_sp.contains("admob_native") && fetch_service_sp.contains("admob_banner")                        && fetch_service_sp.contains("is_admob_enable") && fetch_service_sp.contains("default_protocol")) {                    editor.remove("admob_native").remove("admob_banner").remove("admob_inter").remove("is_admob_enable")                            .remove("default_protocol").apply();                }                editor.putString("admob_banner", ADMOB_BANNER_AD).putString("admob_inter", ADMOB_INTERSETIAL_AD)                        .putString("admob_native", ADMOB_NATIVE_TESTING)                        .putBoolean("is_admob_enable", IS_ADMOB_ENABLE).putString("default_protocol", DEFAULT_PROTOCOL).apply();                getUserInstalledApplications();                if (packageList1 != null && !packageList1.isEmpty()) {                    Intent intent = new Intent("all_data_fetched").putExtra("data_fetched", true)                            .putExtra("intersetial_ad" , ADMOB_INTERSETIAL_AD).putExtra("all_apps", (Serializable) packageList1);                    LocalBroadcastManager.getInstance(Fetch_Service.this).sendBroadcast(intent);                }//                intent.putExtra("all_apps", (Serializable) all_apps);//                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);            }    }    private void get_Update_ids(JSONArray response) {        if (response.length() > 0) {            for (int i = 0; i < response.length(); i++) {                try {                    JSONObject object = response.getJSONObject(i);                    Constant.Version =object.getString("version");                    Constant.discription = object.getString("description");                    Constant.showDialogUpdate =object.getBoolean("dialogshow");                    Constant.canlableDialog =object.getBoolean("version_name");                    Constant.appURl = object.getString("url");                    Log.e("Response_update", "" + response);                } catch (JSONException e) {                    Intent intent = new Intent("all_data_fetched").putExtra("data_fetched", false);                    LocalBroadcastManager.getInstance(Fetch_Service.this).sendBroadcast(intent);                    e.printStackTrace();                }            }            get_timer_ids(json_timer);            get_Admob_ids(json_settings);        }    }    private void get_timer_ids(JSONArray response) {        if (response.length() > 0) {            for (int i = 0; i < response.length(); i++) {                try {                    JSONObject object = response.getJSONObject(i);                    Constant.timerValue =object.getLong("timevalue");                     if(Constant.timerValue.toString().matches("0")){                         Constant.timerCheck=false;                     }                     else {                         Constant.timerCheck=true;                         totalSeconds=  Constant.timerValue * 60;                     }                    Log.e("Response_time", "" + Constant.timerValue.toString()+" value "+Constant.timerCheck);                } catch (JSONException e) {                    Intent intent = new Intent("all_data_fetched").putExtra("data_fetched", false);                    LocalBroadcastManager.getInstance(Fetch_Service.this).sendBroadcast(intent);                    e.printStackTrace();                }            }        }    }    private boolean isSystemPackage(PackageInfo pkgInfo) {        return (pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;    }    List<PackageInfo> getUserInstalledApplications() {        packageManager = getPackageManager();        List<PackageInfo> packageList = packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS);        if (packageList1 == null)            packageList1 = new ArrayList<>();        packageList1.clear();        /*To filter out System apps*/        for (PackageInfo pi : packageList) {            boolean b = isSystemPackage(pi);            if (!b) {                packageList1.add(pi);            }        }        return packageList1;    }}