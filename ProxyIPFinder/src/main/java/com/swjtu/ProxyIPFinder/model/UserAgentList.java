package com.swjtu.ProxyIPFinder.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserAgentList {
	private List<String>  userAgentList = new ArrayList<String>();;
	
	public UserAgentList() {
		
		setDefaultUserAgents();
	}
	
	public void setDefaultUserAgents() {
		userAgentList.add("Mozilla/5.0 (Windows; U; Windows NT 6.1; rv:2.2) Gecko/20110201");
		
		userAgentList.add("Mozilla/5.0 (Windows; U; Windows NT 6.1; it; rv:2.0b4) Gecko/20100818");
		
		userAgentList.add("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
		
		userAgentList.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2227.1 Safari/537.36");
		
		userAgentList.add("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2227.0 Safari/537.36");
	}
	
	public String getOneUserAgent() {
		if(userAgentList== null || userAgentList.size() == 0) {
			setDefaultUserAgents();
		}
		Random rand = new Random();
		int index = rand.nextInt(userAgentList.size()); 
		return userAgentList.get(index);
	}
	public List<String> getUserAgentList() {
		return userAgentList;
	}
	
	
}
