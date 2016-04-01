package com.swjtu.ProxyIPFinder.extraction.impls;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import com.swjtu.ProxyIPFinder.extraction.interfaces.ISeedPattern;

public class ProxySeedPattern implements ISeedPattern<String>{
	
	private String sourceContent;
	
	private String seedRegex ;
	
	
	
	public void setSourceContent(String sourceContent) {
		this.sourceContent = sourceContent;
		
		setIPSeed();
	}

	public void setIPSeed() {	
		setSeed("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");
	}
	
	public void setSeed(String seedRegex) {
		// TODO Auto-generated method stub
		this.seedRegex = seedRegex;
	}
	
	public String getPattern() {
		
		Pattern pattern = Pattern.compile(seedRegex);
		Matcher matcher  = pattern.matcher(sourceContent.toString());
		String ipSequence = null;
		boolean flag = false;
		
		Map<String , Integer> patternMap = new HashMap<String , Integer>();
		
		while(matcher.find()) {
			
			flag = true;
			
			ipSequence = matcher.group();
			
			HtmlCleaner cleaner = new HtmlCleaner();
			TagNode rootNode = cleaner.clean(this.sourceContent);
			
			SolveIPTagNodeVisitor  nodeVisitor = new SolveIPTagNodeVisitor(ipSequence);
			rootNode.traverse(nodeVisitor);
			
			String patternString = ".//" + nodeVisitor.getIpTagName();
			
			if(patternMap.containsKey(patternString)) {
				int num = patternMap.get(patternString);
				num++;
				patternMap.put(patternString, num);
			} else {
				patternMap.put(patternString, 1);
			}
			
		}
		
		if(flag == false )	return null;
		else {
			String resultPattern = null;
			int total = 0;
			Set<String> keySet = patternMap.keySet();
			for(String ss : keySet) {
				if(total < patternMap.get(ss)) {
					total = patternMap.get(ss);
					resultPattern = ss;
				}
			}
			
			return resultPattern;
			
		}
	}
	
	
}
