package com.smappinc.vpnmaster.pro.Model;

public class Cities_Server_List_Pojo {
	private String city;
	private String server_ip;
	private String host_Name,type , ovpnconfig , ikevconfig,username_city,password_city;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {

		this.city = city;
	}


	public String getUsername_city() {
		return username_city;
	}

	public void setUsername_city(String username_city) {

		this.username_city = username_city;
	}


	public String getPassword_city() {
		return password_city;
	}

	public void setPassword_city(String password_city) {

		this.password_city = password_city;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {

		this.type = type;
	}


	public String getOvpnconfig() {
		return ovpnconfig;
	}

	public void setOvpnconfig(String ovpnconfig) {

		this.ovpnconfig = ovpnconfig;
	}


	public String getIkevconfig() {
		return ikevconfig;
	}

	public void setIkevconfig(String ikevconfig) {

		this.ikevconfig = ikevconfig;
	}

	public String getServer_ip() {

		return server_ip;
	}

	public void setServer_ip(String server_ip) {

		this.server_ip = server_ip;
	}

	public String getHost_Name() {
		return host_Name;
	}
	public void setHost_Name(String host_Name) {
		this.host_Name = host_Name;
	}

	@Override
	public String toString() {
		return "Cities_Server_List_Pojo{" +
				"city='" + city + '\'' +
				", server_ip='" + server_ip + '\'' +
				", host_Name='" + host_Name + '\'' +
				", type='" + type + '\'' +
				", ovpnconfig='" + ovpnconfig + '\'' +
				", ikevconfig='" + ikevconfig + '\'' +
				", username_city='" + username_city + '\'' +
				", password_city='" + password_city + '\'' +
				'}';
	}
}
