package com.swjtu.ProxyIPFinder.model;

import java.util.ArrayList;
import java.util.List;

public class ProxyIPList {
	
	private List<ProxyIP> proxyipList = new ArrayList<ProxyIP>();

	public List<ProxyIP> getProxyipList() {
		return proxyipList;
	}

	public void setProxyipList(List<ProxyIP> proxyipList) {
		this.proxyipList = proxyipList;
	}
	
	public void addProxyIP(String ip , String port) {
		ProxyIP proxy = new ProxyIP(ip, port);
		proxyipList.add(proxy);
	}
}
