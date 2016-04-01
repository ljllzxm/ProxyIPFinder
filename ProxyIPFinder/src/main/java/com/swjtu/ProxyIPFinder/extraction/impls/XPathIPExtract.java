package com.swjtu.ProxyIPFinder.extraction.impls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlcleaner.CommentNode;
import org.htmlcleaner.ContentNode;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.HtmlNode;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import com.swjtu.ProxyIPFinder.extraction.interfaces.IAutomaticExtract;
import com.swjtu.ProxyIPFinder.model.ProxyIP;
import com.swjtu.ProxyIPFinder.model.ProxyIPList;

public class XPathIPExtract implements IAutomaticExtract<String>{

	private String webContent;
	
	private String seedXPath;
	
	private int minimalNodes ;
	
	private Set<String> portSet ;
	
	private ProxyIPList resultIPList;
	
	class RankResult {
		ProxyIPList ipList;
		Double score;
	}
	
	public XPathIPExtract() {
		init();
	}
	
	public void init() {
		

		portSet = new HashSet<String>();
		
		
		portSet.add("8080");
		portSet.add("3128");
		portSet.add("3129");
		portSet.add("8085");
		portSet.add("80");
		portSet.add("8000");
		portSet.add("8088");
		portSet.add("8081");
		portSet.add("8008");
		portSet.add("3129");
		portSet.add("8089");
		portSet.add("8090");
		portSet.add("8086");
		portSet.add("8888");
		portSet.add("82");
		portSet.add("9797");
		portSet.add("85");
		portSet.add("81");
		portSet.add("8118");
		portSet.add("9999");
		portSet.add("9000");
		portSet.add("8123");
		portSet.add("83");
		portSet.add("18001");
		portSet.add("86");
		portSet.add("8101");
		portSet.add("808");
		portSet.add("1337");
		portSet.add("843");
		portSet.add("18000");
		portSet.add("4040");	
		portSet.add("8197");
		portSet.add("6666");
		portSet.add("443");
		portSet.add("84");
		portSet.add("8898");
		portSet.add("8146");
		portSet.add("8184");
		portSet.add("8181");
		portSet.add("3308");
		portSet.add("10000");
		portSet.add("8585");
		portSet.add("90");
		portSet.add("9090");
		portSet.add("3131");
		portSet.add("6588");
		portSet.add("1234");
		portSet.add("3130");
		portSet.add("1080");
		portSet.add("8128");
		portSet.add("4444");
		portSet.add("3333");
		portSet.add("21320");
	}
	
	public void setMinimalNodes(int minimalNodes) {
		this.minimalNodes = minimalNodes;
	}

	// 得到节点的绝对路径
	public String getAbsolutePathFromRoot(TagNode curNode) {
		String fullPath = "";
		while(curNode.getName()!="html") {
			fullPath = "/" + curNode.getName() + fullPath;
			curNode = curNode.getParent();
		}
		fullPath = "./" + fullPath;
		return fullPath;
	}
	
	// 设置网页源代码
	public void setWebContent(String content) {
		// TODO Auto-generated method stub
		this.webContent = content;
	}
	
	
	// 发现IP内容
	public String findIPString(String line) {
		String regex ="((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher  = pattern.matcher(line);
		
		if(matcher.find()) {
			return matcher.group().trim();
		} else 
			return null;
		
	}
	
	// 发现端口号
	public String findPortString(String line) {
		String lineString = line.trim();
		if(portSet.contains(lineString)) {
			return lineString;
		} else
			return null;
	}
	
	// 对候选表达式进行排序
	public Map<String , RankResult> rankCandidate(List<String> xpaths) {
		Map<String ,RankResult> map = new HashMap<String , RankResult>();
		
		for(int i = 0 ; i < xpaths.size() ; i++) {
			
			RankResult rr = new RankResult();
			
			HtmlCleaner cleaner = new HtmlCleaner();
			TagNode rootNode = cleaner.clean(this.webContent);
			
			Object [] nodes = null;

			try {
				nodes =  rootNode.evaluateXPath(xpaths.get(i));
				
			} catch (XPatherException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ProxyIPList proxyList = new ProxyIPList();
			
			int mainContentLength = 0;
			
			for(int j = 0 ; j < nodes.length ; j++) {
				
				
				boolean hasPort = false;
				List<HtmlNode> childNodes = (List<HtmlNode>) ((TagNode)nodes[j]).getAllChildren();
				ProxyIP proxyIP = new ProxyIP();
				for(int k = 0 ; k < childNodes.size() ;  k++) {
					if(childNodes.get(k) instanceof ContentNode) {
//						System.out.println("content");
//						System.out.println(((ContentNode)childNodes.get(k)).getContent());
					} else if(childNodes.get(k) instanceof TagNode) {
//						System.out.println("tagNode");
//						System.out.println(((TagNode)childNodes.get(k)).getText());
						String nodeString = ((TagNode)childNodes.get(k)).getText().toString().trim();
						if(findIPString(nodeString)!=null) {
							proxyIP.setIp(nodeString);
						} else if(findPortString(nodeString)!=null) {
							proxyIP.setPort(nodeString);
							hasPort = true;
						} else if(findNumString(nodeString)!=null) {
							
						}
						
					} else if(childNodes.get(k) instanceof CommentNode) {
						System.out.println("commmentNode");
						System.out.println(((CommentNode)childNodes.get(k)).getCommentedContent());
					}
				}
				
				if(proxyIP.getPort()!=null&&proxyIP.getIp()!=null) {
					mainContentLength += proxyIP.getIp().toCharArray().length + proxyIP.getPort().toCharArray().length;
					proxyList.addProxyIP(proxyIP.getIp(), proxyIP.getPort());
				}
			}
			
			rr.score = mainContentLength / (double)rootNode.getText().toString().length();
			rr.ipList = proxyList;
			
			System.out.println(xpaths.get(i) +":" + rr.score);
			map.put(xpaths.get(i), rr);
			
		}
		
		return map;
	}
	
	
	private String findNumString(String nodeString) {
		// TODO Auto-generated method stub
		
		String regex ="(\\d+)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher  = pattern.matcher(nodeString);
		
		if(matcher.find()) {
			return matcher.group().trim();
		} else 
			return null;
	}

	// 正则表达式抽取
	public List<ProxyIP> getRegularExpression() {
		String regex ="((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))):\\d+";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher  = pattern.matcher(webContent.toString());
		
		ArrayList<ProxyIP> arrayList = new ArrayList<ProxyIP>();
		
		while(matcher.find()) {
			String [] proxy = matcher.group().split(":");
			ProxyIP tmpProxy = new ProxyIP(proxy[0],proxy[1]);
			arrayList.add(tmpProxy);
			
		}

		return arrayList;
	}
	
	// 算法流程主函数
	public List<ProxyIP> execute() throws Exception {
		
		List<ProxyIP> arr = null;
		if((arr = getRegularExpression()) != null && arr.size() > 0) {
			return arr;
		}
		
		if(this.seedXPath == null) {
			System.err.print("No Seed XPath");
			return null;
		}
		
		HtmlCleaner cleaner = new HtmlCleaner();
		
		Set<String> groupXPathSet = new HashSet<String>();
		
		TagNode rootNode = cleaner.clean(this.webContent);
		
		Object [] nodes = null;

		nodes =  rootNode.evaluateXPath(seedXPath);

		
		// 
		for(int i = 0 ; i <nodes.length ; i++) {
			TagNode tmpNode = (TagNode)nodes[i];

			groupXPathSet.add(getAbsolutePathFromRoot(tmpNode));
		}
		
		// 对数据进行聚类
		
		nodes = null;
		List<String> tmpXPathList = new ArrayList<String>();
		for(String tmpXPath : groupXPathSet) {
			
			nodes = rootNode.evaluateXPath(tmpXPath);
			
			if(nodes.length < this.minimalNodes) {
				tmpXPathList.add(tmpXPath);
			}	
		}
		
		
		for(int i = 0 ; i < tmpXPathList.size() ; i++) 
			groupXPathSet.remove(tmpXPathList.get(i));
		
		List<String> candidateIPList = new ArrayList<String>();
		
		for(String e : groupXPathSet) {
			
			System.out.println("xpath :" +e);
			String parentX = e;
			Object [] pCalculateNodes = rootNode.evaluateXPath(parentX);
			int lastNum = pCalculateNodes.length;
			
			String currentX = parentX.substring(0, parentX.lastIndexOf('/'));
			Object [] cCalculateNodes = rootNode.evaluateXPath(currentX);
			int curNum = cCalculateNodes.length;
			
			
			
			System.out.println("parentX:" + lastNum + " currentX:" + curNum);
			while(curNum == lastNum) {
				parentX = currentX;
				lastNum = curNum;
				
				currentX = parentX.substring(0, parentX.lastIndexOf('/'));
				cCalculateNodes = rootNode.evaluateXPath(currentX);
				curNum = cCalculateNodes.length;
			}
			
			candidateIPList.add(currentX);
			
		}
		
		 Map<String, RankResult> tmpMap = rankCandidate(candidateIPList);
		 Set<String> tmpSet = tmpMap.keySet();
		 double maxScore = -0.1;
		 ProxyIPList record = null;
		 for(String ss : tmpSet) {
			 RankResult tmpRR = tmpMap.get(ss);
			 if(maxScore < tmpRR.score) {
				 maxScore = tmpRR.score;
				 record = tmpRR.ipList;
			 }
			
		 }
		 
		 resultIPList = record;
		 
		 List<ProxyIP> ll = resultIPList.getProxyipList();
		 for(int i = 0 ; i < ll.size() ;i++) {
			 ProxyIP ip = ll.get(i);
			 
			 System.out.println(ip.getIp() + ":" + ip.getPort());
		 }
		 return ll;
	}

	public String[] getAllResult() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getBestResult() {
		
		return null;
	}

	// 设置种子表达式
	public void setSeedXPath(String seedXPath) {
		this.seedXPath = seedXPath;
	}
	
	
}
