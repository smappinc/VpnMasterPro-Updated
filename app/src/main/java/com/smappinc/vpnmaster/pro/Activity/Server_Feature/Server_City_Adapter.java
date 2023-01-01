
package com.smappinc.vpnmaster.pro.Activity.Server_Feature;

import static android.content.Context.MODE_PRIVATE;
import static com.smappinc.vpnmaster.pro.Util.Constant.IS_RUN;
import static com.smappinc.vpnmaster.pro.Util.Constant.decodeSampledBitmapFromResource;
import static com.smappinc.vpnmaster.pro.Util.Constant.getBestServer_after_calculation;
import static com.smappinc.vpnmaster.pro.Util.Constant.storeValueToPreference;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flag_selection.Country_Flag_Picker;
import com.flag_selection.Country_Names;
import com.smappinc.vpnmaster.pro.Model.Cities_Server_List_Pojo;
import com.smappinc.vpnmaster.pro.Model.Parent_Server_Adapter;
import com.smappinc.vpnmaster.pro.Model.api_response;
import com.smappinc.vpnmaster.pro.R;

import java.util.ArrayList;
import java.util.Objects;


public class Server_City_Adapter extends RecyclerView.Adapter<Server_City_View_Holder> {

    public Context mContext;
    public ArrayList<Cities_Server_List_Pojo> city_list;
    Parent_Server_Adapter parentserver;
    Country_Flag_Picker child_picker;
    Country_Names child_countries;
    SharedPreferences city_Server_preference;

    public Server_City_Adapter(Context pContext, ArrayList<Cities_Server_List_Pojo> pFlowerLists, Parent_Server_Adapter parentserver, Country_Flag_Picker child_picker) {

        mContext = pContext;
        city_list = pFlowerLists;
        this.child_picker = child_picker;
        this.parentserver = parentserver;

        if (mContext != null) {
            city_Server_preference = mContext.getSharedPreferences("DATA", MODE_PRIVATE);
        }
    }

    @NonNull
    @Override
    public Server_City_View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_city_item_, parent, false);
        return new Server_City_View_Holder(mView);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull Server_City_View_Holder holder, int position) {

        Cities_Server_List_Pojo citiesServerListPojo = city_list.get(position);
        child_countries = child_picker.getCountryByName(citiesServerListPojo.getCity());
        holder.child_countryName.setText(citiesServerListPojo.getCity());

        if (Objects.requireNonNull(getBestServer_after_calculation(city_Server_preference)).getCity().contains(citiesServerListPojo.getCity())) {
            holder.selected_icon.setVisibility(View.VISIBLE);
            holder.selected_icon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_choice_selected));
            Log.d("Cities_Server_List_Pojo", "" + citiesServerListPojo.getCity());
        }

        if (child_countries != null) {
            holder.child_countryImage.setImageBitmap(decodeSampledBitmapFromResource(mContext.getResources(), child_countries.getFlag(), 50, 50));
        } else {
            holder.child_countryImage.setImageResource(R.drawable.flag_us);
        }

        if (citiesServerListPojo.getType().equals("2")) {
            holder.imageViewVIP.setVisibility(View.VISIBLE);
            holder.imageViewVIP.setImageResource(R.drawable.premium_gold);
        }


        holder.child_item.setOnClickListener(v -> {
            if (citiesServerListPojo.getType().equals("2")) {
//                api_response Api_response_model = new api_response();
//
//                Api_response_model.setHost_name(citiesServerListPojo.getHost_Name());
//                Api_response_model.setIp_Address(citiesServerListPojo.getServer_ip());
//                Api_response_model.setCity(citiesServerListPojo.getCity());
//                Api_response_model.setOvpn_config(citiesServerListPojo.getOvpnconfig());
//                Api_response_model.setIkev2_config(citiesServerListPojo.getIkevconfig());
//                Api_response_model.set_type(citiesServerListPojo.getType());
//
//                storeValueToPreference(city_Server_preference, "selected_server", Api_response_model);
                IS_RUN = true;

                if (getBestServer_after_calculation(city_Server_preference) != null) {
                    if (Objects.requireNonNull(getBestServer_after_calculation(city_Server_preference)).getHost_name() != null) {
                        ((Activity) mContext).finish();
                        Intent intent = new Intent("premium_clicked");
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                    }
                }
            } else {
                api_response Api_response_model = new api_response();

                Api_response_model.setHost_name(citiesServerListPojo.getHost_Name());
                Api_response_model.setIp_Address(citiesServerListPojo.getServer_ip());
                Api_response_model.setCity(citiesServerListPojo.getCity());
                Api_response_model.setOvpn_config(citiesServerListPojo.getOvpnconfig());
                Api_response_model.setIkev2_config(citiesServerListPojo.getIkevconfig());
                Api_response_model.setUsername(citiesServerListPojo.getUsername_city());
                Api_response_model.setPassword(citiesServerListPojo.getPassword_city());

                Api_response_model.set_type(citiesServerListPojo.getType());

                storeValueToPreference(city_Server_preference, "selected_server", Api_response_model);
                IS_RUN = true;

                if (getBestServer_after_calculation(city_Server_preference) != null) {
                    if (Objects.requireNonNull(getBestServer_after_calculation(city_Server_preference)).getHost_name() != null) {
                        ((Activity) mContext).finish();
                        Intent intent = new Intent("connect_vpn");
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return city_list.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }
}


class Server_City_View_Holder extends RecyclerView.ViewHolder {

    public TextView child_countryName;
    ImageView child_countryImage, imageViewVIP, selected_icon;
    LinearLayout child_item;


    Server_City_View_Holder(View itemView) {

        super(itemView);
        child_countryName = itemView.findViewById(R.id.child_countryName);
        child_countryImage = itemView.findViewById(R.id.child_countryImage);
        imageViewVIP = itemView.findViewById(R.id.imageViewVIP);
        selected_icon = itemView.findViewById(R.id.selected_icon);
        child_item = itemView.findViewById(R.id.child_item);
    }
}

