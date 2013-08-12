package com.haks.haksvn.general.model;

import org.springframework.stereotype.Component;

@Component
public class MailConfiguration {

	private String port = "25";
	private String host = "";
	private boolean sslEnabled = false;
	private boolean authEnabled = false;
	private String username = "";
	private String password = "";
	
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public boolean getSslEnabled() {
		return sslEnabled;
	}
	public void setSslEnabled(boolean sslEnabled) {
		this.sslEnabled = sslEnabled;
	}
	public boolean getAuthEnabled() {
		return authEnabled;
	}
	public void setAuthEnabled(boolean authEnabled) {
		this.authEnabled = authEnabled;
	}
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
	
	
	
}
