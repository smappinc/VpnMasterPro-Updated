package com.smappinc.vpnmaster.pro.Api_Fetch_Service;

public class api_response_updated {

    private int server_id;
    private String Country_name;
    private String ip_Address, type, Opc_ip, Opc_user, Opc_ps;

    public int getServer_id() {
        return server_id;
    }

    public void setServer_id(int server_id) {

        this.server_id = server_id;
    }

    public String getCountry_name() {

        return Country_name;
    }

    public void setCountry_name(String Country_name) {

        this.Country_name = Country_name;
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

    public void setOpc_ip(String opc_ip) {

        this.Opc_ip = opc_ip;
    }

    public String getOpc_ip() {

        return Opc_ip;
    }

    public void setOpc_user(String opc_user) {

        this.Opc_user = opc_user;
    }

    public String getOpc_user() {

        return Opc_user;
    }

    public void setOpc_ps(String opcPs) {

        this.Opc_ps = opcPs;
    }

    public String getOpc_ps() {
        return Opc_ps;
    }
}
