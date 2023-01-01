package com.smappinc.vpnmaster.pro.Activity.Server_Feature;

import static com.smappinc.vpnmaster.pro.Util.Constant.SERVER_URL;
import static com.smappinc.vpnmaster.pro.Util.Constant.set_status_bar;
import static com.smappinc.vpnmaster.pro.Util.Constant.storeValueToPreference;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flag_selection.Country_Flag_Picker;
import com.flag_selection.Country_Names;
import com.flag_selection.listeners.Country_Picker_Listener_Interface;
import com.smappinc.vpnmaster.pro.Model.Cities_Server_List_Pojo;
import com.smappinc.vpnmaster.pro.Model.Parent_Server_Adapter;
import com.smappinc.vpnmaster.pro.Model.api_response;
import com.smappinc.vpnmaster.pro.R;
import com.smappinc.vpnmaster.pro.Util.TinyDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Server_Screen extends AppCompatActivity implements Country_Picker_Listener_Interface {

    public RecyclerView choose_server_Recyclerview;
    ImageView toolbar_back, refresh_icon;
    ArrayList<api_response> api_response_modelArrayList;
    All_server_adapter choose_server_adapter;
    SharedPreferences sharedPreferences;
    public Country_Flag_Picker countryFlagPicker;
    int random_selection_server = 0;
    api_response api_model, api_model_exclude_type;
    ArrayList<api_response> api_array_server;
    Dialog dialog_bar;
    ProgressBar progress_bar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomended_servers_list);

        set_status_bar(Server_Screen.this);
        init();
    }

    public ArrayList<Parent_Server_Adapter> getCitiesList(ArrayList<api_response> countryList) {

        ArrayList<Parent_Server_Adapter> parentList = new ArrayList<>();

        if (countryList != null && countryList.size() > 0) {

            for (api_response parent : countryList) {

                if (!isExist(parentList, parent.getHost_name())) {

                    Parent_Server_Adapter data = new Parent_Server_Adapter();

                    data.setHost_Name(parent.getHost_name());
                    data.setServerIp(parent.getIp_Address());
                    data.setOvpn_config(parent.getOvpn_config());
                    data.setIkev2_config(parent.getIkev2_config());
                    // data.setSelectedCity(parent.getHost_name());
                    data.setUsername(parent.getUsername());
                    data.setPassword(parent.getPassword());
                    data.setType(parent.get_type());

                    ArrayList<Cities_Server_List_Pojo> childList = new ArrayList<>();
                    for (int i = 0; i < countryList.size(); i++) {

                        if (countryList.get(i) != null) {

                            if (parent.getHost_name().equalsIgnoreCase(countryList.get(i).getHost_name())) {

                                Cities_Server_List_Pojo citys = new Cities_Server_List_Pojo();
                                citys.setCity(countryList.get(i).getCity());
                                citys.setType(countryList.get(i).get_type());
                                citys.setServer_ip(countryList.get(i).getIp_Address());
                                citys.setHost_Name(countryList.get(i).getHost_name());
                                citys.setOvpnconfig(countryList.get(i).getOvpn_config());
                                citys.setIkevconfig(countryList.get(i).getIkev2_config());
                                citys.setUsername_city(countryList.get(i).getUsername());
                                citys.setPassword_city(countryList.get(i).getPassword());
                                childList.add(citys);
                            }
                        }
                    }
                    data.citiesList = childList;
                    parentList.add(data);
                }
            }
        }
        return parentList;
    }

    public boolean isExist(ArrayList<Parent_Server_Adapter> arrayList, String item) {

        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getHost_Name().equalsIgnoreCase(item))
                return true;
        }
        return false;
    }

    private void init() {
        sharedPreferences = getSharedPreferences("DATA", MODE_PRIVATE);
        api_array_server = new ArrayList<>();

        api_response_modelArrayList = new ArrayList<>();
        choose_server_Recyclerview = findViewById(R.id.rv);
        toolbar_back = findViewById(R.id.serverclose);
        refresh_icon = findViewById(R.id.refresh_icon);

        countryFlagPicker = new Country_Flag_Picker.Builder().with(Server_Screen.this).listener(Server_Screen.this).build();

        if (choose_server_Recyclerview != null) {
            populate_data();
        }
        toolbar_back.setOnClickListener(v -> onBackPressed());


        refresh_icon.setOnClickListener(v -> {
            hit_api_again();
        });
    }

    private void hit_api_again() {

        show_dialog();

        RequestQueue queue = Volley.newRequestQueue(Server_Screen.this);
        StringRequest stringRequest = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            stringRequest = new StringRequest(Request.Method.GET, SERVER_URL, this::parse_data, error -> {
            });
        }
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, 3, 3));
        queue.add(stringRequest);
    }

    private void show_dialog() {

        dialog_bar = new Dialog(Server_Screen.this);
        dialog_bar.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_bar.setContentView(R.layout.custom_layout_connected);
        progress_bar = dialog_bar.findViewById(R.id.imageDialog);
        progress_bar.setVisibility(View.VISIBLE);
        dialog_bar.setCancelable(true);

        if (!isFinishing()) {
            dialog_bar.show();
        }
    }

    public void hide_dialog() {
        if (dialog_bar != null && dialog_bar.isShowing()) {
            dialog_bar.dismiss();
            if (progress_bar != null)
                progress_bar.setVisibility(View.INVISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void parse_data(String input) {
        api_array_server.clear();

        try {
            JSONObject jsonObject = new JSONObject(input);
            JSONArray jsonArray = jsonObject.getJSONArray("Servers");

            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    try {
                        api_model = new api_response();
                        api_model.setHost_name(object.getString("Country"));
                        api_model.setIp_Address(object.getString("IP"));
                        api_model.setCity(object.getString("City"));
                        api_model.setOvpn_config(object.getString("openvpn_config"));
                        api_model.setIkev2_config(object.getString("Certificate"));
                        api_model.setUsername(object.getString("Username"));
                        api_model.setPassword(object.getString("Password"));
                        api_model.set_type(object.getString("Type"));
                        api_array_server.add(api_model);

                    } catch (JSONException e) {
                        Log.d("JSONException", "" + e.getMessage());
                        e.printStackTrace();
                    }
                }

                if (sharedPreferences != null) {
                    if (api_array_server != null && !api_array_server.isEmpty()) {
                        TinyDB tinydb = new TinyDB(Server_Screen.this);
                        tinydb.put_array_list_from_cache("saved_list", api_array_server);

                        List<api_response> newList_server_screen = new ArrayList<>(api_array_server);
                        newList_server_screen.removeIf(p -> p.get_type().equals("2"));
                        Random randomGenerator = new Random();
                        random_selection_server = randomGenerator.nextInt(newList_server_screen.size());

                        api_model_exclude_type = new api_response();
                        api_model_exclude_type.setHost_name(newList_server_screen.get(random_selection_server).getHost_name());
                        api_model_exclude_type.setIp_Address(newList_server_screen.get(random_selection_server).getIp_Address());
                        api_model_exclude_type.setCity(newList_server_screen.get(random_selection_server).getCity());
                        api_model_exclude_type.setOvpn_config(newList_server_screen.get(random_selection_server).getOvpn_config());
                        api_model_exclude_type.setIkev2_config(newList_server_screen.get(random_selection_server).getIkev2_config());
                        api_model_exclude_type.setUsername(newList_server_screen.get(random_selection_server).getUsername());
                        api_model_exclude_type.setPassword(newList_server_screen.get(random_selection_server).getPassword());
                        api_model_exclude_type.set_type(newList_server_screen.get(random_selection_server).get_type());

                        storeValueToPreference(sharedPreferences, "selected_server", api_model_exclude_type);
                        populate_data();

                        hide_dialog();
                    }
                }
            }
        } catch (JSONException e) {
            Log.d("JSONException22", "" + e.getMessage());
            e.printStackTrace();
        }
    }


    private void populate_data() {

        TinyDB tinydb = new TinyDB(Server_Screen.this);

        if (!tinydb.get_array_list_from_cache("saved_list").isEmpty() && tinydb.get_array_list_from_cache("saved_list") != null) {

            choose_server_adapter = new All_server_adapter(Server_Screen.this, getCitiesList(tinydb.get_array_list_from_cache("saved_list")), countryFlagPicker);

            choose_server_Recyclerview.setLayoutManager(new LinearLayoutManager(Server_Screen.this));
            ViewCompat.setNestedScrollingEnabled(choose_server_Recyclerview, false);
            choose_server_Recyclerview.setNestedScrollingEnabled(false);
            choose_server_Recyclerview.setAdapter(choose_server_adapter);

            if (choose_server_adapter != null) {
                choose_server_adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onSelectCountry(Country_Names countryNames) {

    }
}