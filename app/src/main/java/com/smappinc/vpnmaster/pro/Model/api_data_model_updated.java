package com.smappinc.vpnmaster.pro.Model;

public class api_data_model_updated {

    private int server_id;
    private String host_name;
    private String ip_Address, type;

    public int getServer_id() {
        return server_id;
    }

    public void setServer_id(int server_id) {

        this.server_id = server_id;
    }

    public String getHost_name() {

        return host_name;
    }

    public void setHost_name(String host_name ) {

        this.host_name = host_name;
    }

    public String getIp_Address() {

        return ip_Address;
    }

    public void setIp_Address(String ip_Address) {

        this.ip_Address = ip_Address;
    }

    public String get_type() {

        return type;
    }

    public void set_type(String type) {

        this.type = type;
    }
}
