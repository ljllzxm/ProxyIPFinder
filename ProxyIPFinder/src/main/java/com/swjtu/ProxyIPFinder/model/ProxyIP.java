package com.swjtu.ProxyIPFinder.model;



public class ProxyIP {
	// Proxy IP
	private String ip;
	// Port
	private String port;
	
	private Anonymity transparency;
	
	public ProxyIP() {
		this.ip = null;
		this.port = null;
		this.transparency = null;
	}
	
	public ProxyIP(String ip , String port) {
		this.ip = ip;
		this.port = port;
		this.transparency = Anonymity.TRANSPARENT;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public Anonymity getTransparency() {
		return transparency;
	}

	public void setTransparency(Anonymity transparency) {
		this.transparency = transparency;
	}
	
}
