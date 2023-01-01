package com.smappinc.vpnmaster.pro.Util;

import com.smappinc.vpnmaster.pro.R;

public class DataManager {

    public int Version = 1;
    public static String[] Server_UUIDS;
    public static String[] Server_IPS = {
           "149.102.135.83","0","0"
    };

    public static String[] Server_NameS = {
            "SouthUS "

    };

    public static int[] FlagIds = {
            R.drawable.flag_ua,
            R.drawable.flag_ua,
            R.drawable.flag_ua,
            R.drawable.flag_ua,
            R.drawable.flag_ua,
            R.drawable.germany,
    };
    public static int USA_SERVERS = 1;


}

/*
public class DataManager {

    public String[] Server_IPs = null;
    public String[] Server_Names = null;
    public String User_Name = "";
    public String Password = "";
    public int Version = 1;
    public int USA_Servers = 0;

    public static String username = "sharpvpn";
    public static String password = "123456";
    public static String[] Server_UUIDS;
    public static String[] Server_IPS = {
            "167.86.108.54",
            "209.145.60.123"
    };

    public static String[] Server_NameS = {
            "Auto Connect",
            "Germany     ",
            "USA         "
    };

    public static int[] FlagIds = {
            R.drawable.globe,
            R.drawable.f_21,
            R.drawable.f_1
    };
    public static int USA_SERVERS = 4;


}
*/
