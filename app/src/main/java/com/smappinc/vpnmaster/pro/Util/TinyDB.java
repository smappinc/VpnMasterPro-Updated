package com.smappinc.vpnmaster.pro.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.smappinc.vpnmaster.pro.Model.api_response;

import java.util.ArrayList;
import java.util.Arrays;


public class TinyDB {

    private Context context;
    private SharedPreferences preferences;

    public TinyDB(Context appContext) {
        preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        context = appContext;
    }


    public ArrayList<api_response> get_array_list_from_cache(String key) {

        if (preferences.contains(key)) {
            Gson gson = new Gson();

            ArrayList<String> objStrings = get_array_list_from_cache_string(key);
            ArrayList<api_response> playerList = new ArrayList<>();

            for (String jObjString : objStrings) {
                api_response player = gson.fromJson(jObjString, api_response.class);
                playerList.add(player);
            }
            return playerList;
        } else {
            return null;
        }

    }

    public ArrayList<String> get_array_list_from_cache_string(String key) {
        return new ArrayList<>(Arrays.asList(TextUtils.split(preferences.getString(key, ""), "‚‗‚")));
    }


    public void put_array_list_from_cache(String key, ArrayList<api_response> objArray) {
        if (preferences.contains(key)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove(key).apply();
        }

        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<>();
        for (Object obj : objArray) {
            objStrings.add(gson.toJson(obj));
        }
        put_array_list_from_cache_string(key, objStrings);
    }

    public void put_array_list_from_cache_string(String key, ArrayList<String> stringList) {
        if (preferences.contains(key)) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove(key).apply();
        }

        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
    }
}
