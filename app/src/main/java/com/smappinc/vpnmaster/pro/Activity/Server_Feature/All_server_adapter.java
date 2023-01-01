package com.smappinc.vpnmaster.pro.Activity.Server_Feature;

import static android.content.Context.MODE_PRIVATE;

import static com.smappinc.vpnmaster.pro.Util.Constant.IS_RUN;
import static com.smappinc.vpnmaster.pro.Util.Constant.decodeSampledBitmapFromResource;
import static com.smappinc.vpnmaster.pro.Util.Constant.getBestServer_after_calculation;
import static com.smappinc.vpnmaster.pro.Util.Constant.storeValueToPreference;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import com.flag_selection.Country_Flag_Picker;
import com.flag_selection.Country_Names;
import com.smappinc.vpnmaster.pro.Activity.Splash_Screen;
import com.smappinc.vpnmaster.pro.Model.Parent_Server_Adapter;
import com.smappinc.vpnmaster.pro.Model.api_response;
import com.smappinc.vpnmaster.pro.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;


public class All_server_adapter extends RecyclerView.Adapter<Server_Adapter_ViewHolder> {
    public static api_response Api_response_model;
    public static Context mContext;
    public ArrayList<Parent_Server_Adapter> modelArrayList;
    Country_Flag_Picker picker;
    Country_Names countries;
   public static SharedPreferences choose_Server_preference;
  public static   Parent_Server_Adapter parentServerAdapter;
    int count;

    public All_server_adapter(Context mContext, ArrayList<Parent_Server_Adapter> mFlowerList, Country_Flag_Picker picker
    ) {

        this.mContext = mContext;
        this.modelArrayList = mFlowerList;
        this.picker = picker;

        if (mContext != null) {
            choose_Server_preference = mContext.getSharedPreferences("DATA", MODE_PRIVATE);
        }
    }


    @NonNull
    @Override
    public Server_Adapter_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new Server_Adapter_ViewHolder(mView);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NotNull final Server_Adapter_ViewHolder holder, final int position) {

         parentServerAdapter = modelArrayList.get(position);
         countries = picker.getCountryByName(parentServerAdapter.getHost_Name());
          Log.e("mm",modelArrayList.get(position).toString());
      /*  Server_City_Adapter myAdapter = new Server_City_Adapter(mContext, parentServerAdapter.citiesList, parentServerAdapter, picker);
        holder.citiesListRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        holder.citiesListRecyclerview.setNestedScrollingEnabled(false);
        ViewCompat.setNestedScrollingEnabled(holder.citiesListRecyclerview, false);
        holder.citiesListRecyclerview.setAdapter(myAdapter);


        if (holder.citiesListRecyclerview.getAdapter() != null) {
            count = holder.citiesListRecyclerview.getAdapter().getItemCount();
            holder.location_view.setText(parentServerAdapter.getHost_Name() + " (" + count + ")");
        }*/
        if(!Splash_Screen.checkPrimumUsers) {
            if (modelArrayList.get(position).getType().equals("2")) {
                holder.arrow.setImageResource(R.drawable.ccrown);

            } else {

                holder.arrow.setImageResource(R.drawable.un_select_server);

            }
        }
        else {

            holder.arrow.setImageResource(R.drawable.un_select_server);


        }
        holder.location_view.setText(parentServerAdapter.getHost_Name());
        if (countries != null) {
            holder.country_flag.setImageBitmap(decodeSampledBitmapFromResource(mContext.getResources(), countries.getFlag(), 50, 50));
        } else {
            holder.country_flag.setImageResource(R.drawable.flag_us);
        }


       holder.server_item.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Log.e("Visibility_values" , ""+ modelArrayList.get(position).toString());


               if(!Splash_Screen.checkPrimumUsers) {
                   if (modelArrayList.get(position).getType().equals("2")) {
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


                       if (getBestServer_after_calculation(choose_Server_preference) != null) {
                           if (Objects.requireNonNull(getBestServer_after_calculation(choose_Server_preference)).getHost_name() != null) {
                               ((Activity) All_server_adapter.mContext).finish();
                               Intent intent = new Intent("premium_clicked");
                               LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                           }
                       }
                   } else {
                       Api_response_model = new api_response();

                       Api_response_model.setHost_name(modelArrayList.get(position).getHost_Name());
                       Api_response_model.setIp_Address(modelArrayList.get(position).getServerIp());
                       Api_response_model.setCity(modelArrayList.get(position).getSelectedCity());
                       Api_response_model.setOvpn_config(modelArrayList.get(position).getOvpn_config());
                       Api_response_model.setIkev2_config(modelArrayList.get(position).getIkev2_config());
                       Api_response_model.setUsername(modelArrayList.get(position).getUsername());
                       Api_response_model.setPassword(modelArrayList.get(position).getPassword());
                       Api_response_model.set_type(modelArrayList.get(position).getType());
                       storeValueToPreference(choose_Server_preference, "selected_server", Api_response_model);
                       IS_RUN = true;

                       if (getBestServer_after_calculation(choose_Server_preference) != null) {
                           if (Objects.requireNonNull(getBestServer_after_calculation(choose_Server_preference)).getHost_name() != null) {
                               ((Activity) All_server_adapter.mContext).finish();
                               Intent intent = new Intent("connect_vpn");
                               LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                           }
                       }
                   }
               }
               else {


                   Api_response_model = new api_response();

                   Api_response_model.setHost_name(modelArrayList.get(position).getHost_Name());
                   Api_response_model.setIp_Address(modelArrayList.get(position).getServerIp());
                   Api_response_model.setCity(modelArrayList.get(position).getSelectedCity());
                   Api_response_model.setOvpn_config(modelArrayList.get(position).getOvpn_config());
                   Api_response_model.setIkev2_config(modelArrayList.get(position).getIkev2_config());
                   Api_response_model.setUsername(modelArrayList.get(position).getUsername());
                   Api_response_model.setPassword(modelArrayList.get(position).getPassword());
                   Api_response_model.set_type(modelArrayList.get(position).getType());

                   storeValueToPreference(choose_Server_preference, "selected_server", Api_response_model);
                   IS_RUN = true;

                   if (getBestServer_after_calculation(choose_Server_preference) != null) {
                       if (Objects.requireNonNull(getBestServer_after_calculation(choose_Server_preference)).getHost_name() != null) {
                           ((Activity) All_server_adapter.mContext).finish();
                           Intent intent = new Intent("connect_vpn");
                           LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                       }
                   }

               }
           }
       });




        //showChildRecyclerView(citiesListRecyclerview);
        //    arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);

    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }
}

class Server_Adapter_ViewHolder extends RecyclerView.ViewHolder  {

    public TextView location_view;
    CircleImageView country_flag;
    ImageView arrow;
    RecyclerView citiesListRecyclerview;
    LinearLayout server_item;

    Server_Adapter_ViewHolder(View itemView) {
        super(itemView);
        location_view = itemView.findViewById(R.id.location_view);
        country_flag = itemView.findViewById(R.id.server_country_flag);
        server_item = itemView.findViewById(R.id.server_item);
        arrow = itemView.findViewById(R.id.arrow);
        citiesListRecyclerview = itemView.findViewById(R.id.cities_list_recyclerView);
      //  server_item.setOnClickListener(this);
    }

   /* @Override
    public void onClick(View v) {

        if (v.getId() == R.id.server_item) {

            Log.e("Visibility_values" , ""+ All_server_adapter.parentServerAdapter.toString());


                if (All_server_adapter.parentServerAdapter.getType().equals("2")) {
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


                    if (getBestServer_after_calculation(choose_Server_preference) != null) {
                        if (Objects.requireNonNull(getBestServer_after_calculation(choose_Server_preference)).getHost_name() != null) {
                            ((Activity) All_server_adapter.mContext).finish();
                            Intent intent = new Intent("premium_clicked");
                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                        }
                    }
                } else {
                     Api_response_model = new api_response();

                    Api_response_model.setHost_name(All_server_adapter.parentServerAdapter.getHost_Name());
                    Api_response_model.setIp_Address(All_server_adapter.parentServerAdapter.getServerIp());
                    Api_response_model.setCity(All_server_adapter.parentServerAdapter.getSelectedCity());
                    Api_response_model.setOvpn_config(All_server_adapter.parentServerAdapter.getOvpn_config());
                    Api_response_model.setIkev2_config(All_server_adapter.parentServerAdapter.getIkev2_config());
                    Api_response_model.setUsername(All_server_adapter.parentServerAdapter.getUsername());
                    Api_response_model.setPassword(All_server_adapter.parentServerAdapter.getPassword());
                    Api_response_model.set_type(All_server_adapter.parentServerAdapter.getType());

                    storeValueToPreference(choose_Server_preference, "selected_server", Api_response_model);
                    IS_RUN = true;

                    if (getBestServer_after_calculation(choose_Server_preference) != null) {
                        if (Objects.requireNonNull(getBestServer_after_calculation(choose_Server_preference)).getHost_name() != null) {
                            ((Activity)  All_server_adapter.mContext).finish();
                            Intent intent = new Intent("connect_vpn");
                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                        }
                    }
                }



                //showChildRecyclerView(citiesListRecyclerview);
       //    arrow.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
        }
    }
*/
    public void showChildRecyclerView(View view) {

        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = view.getMeasuredHeight();
        view.getLayoutParams().height = 0;
        view.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(targetHeight);
        valueAnimator.addUpdateListener(animation -> { view.getLayoutParams().height = (int) animation.getAnimatedValue();view.requestLayout(); });

        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(200);
        valueAnimator.start();
    }

    public void hideChildRecyclerView(View view) {

        final int initialHeight = view.getMeasuredHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialHeight, 0);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(animation -> {
            view.getLayoutParams().height = (int) animation.getAnimatedValue();
            view.requestLayout();
            if ((int) animation.getAnimatedValue() == 0)
                view.setVisibility(View.GONE);
        });
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.setDuration(200);
        valueAnimator.start();
    }

}