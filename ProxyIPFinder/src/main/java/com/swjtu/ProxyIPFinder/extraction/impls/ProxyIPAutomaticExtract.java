package com.swjtu.ProxyIPFinder.extraction.impls;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.swjtu.ProxyIPFinder.extraction.interfaces.IAutomaticExtract;
import com.swjtu.ProxyIPFinder.model.ProxyIP;
import com.swjtu.ProxyIPFinder.parameter.status.PatternType;

public class ProxyIPAutomaticExtract implements IAutomaticExtract<ProxyIP>{
	
	private String webContent;
	
	private PatternType resultType;
	

	public ProxyIP[] getRegularExpression() {
		String regex ="((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))):\\d+";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher  = pattern.matcher(webContent.toString());
		
		ArrayList<ProxyIP> arrayList = new ArrayList<ProxyIP>();
		
		while(matcher.find()) {
			String [] proxy = matcher.group().split(":");
			ProxyIP tmpProxy = new ProxyIP(proxy[0],proxy[1]);
			arrayList.add(tmpProxy);
			
		}

		ProxyIP [] proxyIP = new ProxyIP[arrayList.size()];
		for(int i = 0 ; i < proxyIP.length ; i++) {
			proxyIP[i] = arrayList.get(i);
		}
		
		return proxyIP;
	}
	
	
	
	public ProxyIP[] getAllResult() {
		// TODO Auto-generated method stub
		ProxyIP[] proxyIP = getRegularExpression();
		if(proxyIP!=null) {
			return proxyIP;
		} else {
			return null;
		}
	}


	// First , you shuold set the web content
	public void setWebContent(String content) {
	
		webContent = content;
	}
	
	
}
