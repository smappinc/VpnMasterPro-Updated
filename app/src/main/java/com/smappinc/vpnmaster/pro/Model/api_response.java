package com.smappinc.vpnmaster.pro.Model;

public class api_response {

    private int server_id;
    private String host_name;
    private String ip_Address, type,city,ovpn_config,ikev2_config,username,password;


    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

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

    public String getCity() {
        return city;
    }

    public void setCity(String city ) {
        this.city = city;
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



    public String getOvpn_config() {

        return ovpn_config;
    }

    public void setOvpn_config(String ovpn_config) {

        this.ovpn_config = ovpn_config;
    }


    public String getIkev2_config() {
        return ikev2_config;
    }

    public void setIkev2_config(String ikev2_config) {

        this.ikev2_config = ikev2_config;
    }

    @Override
    public String toString() {
        return "api_response{" +
                "server_id=" + server_id +
                ", host_name='" + host_name + '\'' +
                ", ip_Address='" + ip_Address + '\'' +
                ", type='" + type + '\'' +
                ", city='" + city + '\'' +
                ", ovpn_config='" + ovpn_config + '\'' +
                ", ikev2_config='" + ikev2_config + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
