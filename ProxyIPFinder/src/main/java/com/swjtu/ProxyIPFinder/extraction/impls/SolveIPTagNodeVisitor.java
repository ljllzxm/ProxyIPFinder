package com.swjtu.ProxyIPFinder.extraction.impls;

import org.htmlcleaner.ContentNode;
import org.htmlcleaner.HtmlNode;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.TagNodeVisitor;

public class SolveIPTagNodeVisitor implements TagNodeVisitor{
	
	private String currentIP;
	
	private String ipTagName;
	
	public SolveIPTagNodeVisitor(String ip) {
		this.currentIP = ip;
	}
	
	


	public String getIpTagName() {
		return ipTagName;
	}




	public boolean visit(TagNode parentNode, HtmlNode htmlNode) {
		// TODO Auto-generated method stub
		
		
		 
		if(htmlNode instanceof ContentNode) {
			ContentNode cn = (ContentNode)htmlNode;
			String cnString = cn.getContent();
			
			if(cnString.contains(this.currentIP)){

				this.ipTagName = parentNode.getName();
				return false;
			} else {
				return true;
			}
		} else 
			return true;
		
	}
	
}
