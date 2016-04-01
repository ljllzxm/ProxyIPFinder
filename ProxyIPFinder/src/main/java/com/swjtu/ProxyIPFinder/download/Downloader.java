package com.swjtu.ProxyIPFinder.download;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.xml.sax.SAXException;

import com.swjtu.ProxyIPFinder.extraction.impls.ProxyIPAutomaticExtract;
import com.swjtu.ProxyIPFinder.extraction.impls.ProxySeedPattern;
import com.swjtu.ProxyIPFinder.extraction.impls.XPathIPExtract;
import com.swjtu.ProxyIPFinder.model.UserAgentList;

public class Downloader {
	// address -- no http prefix
	private String address;

	public Downloader() {
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getWebContent() {
		return getWebContent(this.address);
	}

	public String getWebContentByProxyIP(String webAddress, String proxyip, int port) {
		// CloseableHttpClient httpclient = HttpClients.createDefault();
		// String webContent = null;
		// try {
		// HttpGet request = new HttpGet(webAddress);
		//
		// UserAgentList ual = new UserAgentList();
		// request.setHeader("User-Agent", ual.getOneUserAgent());
		//
		// HttpHost proxy = new HttpHost(proxyip, port, "http");
		//
		// int timeout = 20;
		//
		// RequestConfig config =
		// RequestConfig.custom().setProxy(proxy).setSocketTimeout(timeout *
		// 1000)
		// .setConnectTimeout(timeout * 1000).build();
		//
		// request.setConfig(config);
		//
		// CloseableHttpResponse response = null;
		//
		// try {
		// //
		// httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
		// // 15000);
		//
		// response = httpclient.execute(request);
		//
		// // System.out.println(response.getStatusLine());
		//
		// HttpEntity entity = response.getEntity();
		//
		// String charset = EntityUtils.getContentCharSet(entity);
		//
		// if (charset == null) {
		//
		// String regex = "charset='([a-zA-Z0-9-]+)'|charset=([a-zA-Z0-9-]+)";
		// Pattern pattern = Pattern.compile(regex);
		// Matcher matcher = pattern.matcher(EntityUtils.toString(entity));
		// if (matcher.find()) {
		// charset = matcher.group(1);
		// if (charset == null) {
		// charset = matcher.group(2);
		// }
		//
		// } else {
		// charset = "utf-8";
		// }
		//
		// response = httpclient.execute(request);
		// entity = response.getEntity();
		//
		// }
		//
		// // charset="utf-8";
		//
		// // 注意网页的编码格式
		// webContent = EntityUtils.toString(entity, charset);
		//
		// } catch (ClientProtocolException e) {
		// // TODO Auto-generated catch block
		//
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		//
		// } finally {
		// response.close();
		// }
		//
		// } catch (Exception e) {
		//
		// } finally {
		// try {
		// httpclient.close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		return "webContent";

	}

	public String getWebContent(String address) {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		String webContent = null;
		try {
			HttpGet request = new HttpGet(address);
			UserAgentList ual = new UserAgentList();
			request.setHeader("User-Agent", ual.getOneUserAgent());

			// request.setConfig(config);
			CloseableHttpResponse response = null;
			try {
				response = httpclient.execute(request);

				//
				// System.out.println("-----------");
				// System.out.println(response.getStatusLine());

				HttpEntity entity = response.getEntity();

				String charset = EntityUtils.getContentCharSet(entity);

				if (charset == null) {

					String regex = "charset='([a-zA-Z0-9-]+)'|charset=([a-zA-Z0-9-]+)";
					Pattern pattern = Pattern.compile(regex);
					Matcher matcher = pattern.matcher(EntityUtils.toString(entity));
					if (matcher.find()) {
						charset = matcher.group(1);
						if (charset == null) {
							charset = matcher.group(2);
						}

					} else {
						charset = "utf-8";
					}

					response = httpclient.execute(request);
					entity = response.getEntity();

				}

				// charset="utf-8";

				// 注意网页的编码格式
				webContent = EntityUtils.toString(entity, charset);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block

			} catch (IOException e) {
				// TODO Auto-generated catch block

			} finally {
				response.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return webContent;
	}
	
	public void spider(){
		
	}
	
	public static void main(String[] args)
			throws ParserConfigurationException, SAXException, IOException, URISyntaxException, XPatherException {
		Downloader dl = new Downloader();
		String url = "http://paper.people.com.cn/rmrb/html/2016-03/28/nbs.D110000renmrb_01.htm";
		String text = dl.getWebContent(url);
//		System.out.println(text);
		HtmlCleaner hc = new HtmlCleaner();
		TagNode tn = hc.clean(text);
		String xpath = ".//*[@id='titleList']/ul/li/a/@href";
		Object[] objarr = null;
		objarr = tn.evaluateXPath(xpath);
		for (Object obja : objarr) {
//			TagNode tna = (TagNode) obja;
//			String str = tna.getText().toString();
//			System.out.println(str);
			System.out.println(obja);
		}
		// System.out.println(text);

//		ProxyIPAutomaticExtract ipExtract = new ProxyIPAutomaticExtract();
//		ipExtract.setWebContent(text);
////		System.out.println(ipExtract.getAllResult()[0].getPort());
//
//		XPathIPExtract xpe = new XPathIPExtract();
//		xpe.setWebContent(text);
//		xpe.setMinimalNodes(5);
//
//		ProxySeedPattern psp = new ProxySeedPattern();
//		psp.setSourceContent(text);
//
//		xpe.setSeedXPath(psp.getPattern());
//
////		System.out.println(psp.getPattern());
//		try {
//			xpe.execute();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

	}

}

// Success :
// https://www.us-proxy.org/
// http://www.haodailiip.com/guonei/510000/1
// http://www.proxy360.cn/default.aspx
// http://www.kuaidaili.com/
// http://www.xicidaili.com/wn/
// http://www.haodailiip.com/
// http://www.nianshao.me/
// http://www.66ip.cn/
// http://www.cz88.net/proxy/
// http://www.jyhack.com/article/info-152.html
//
//

/*
 * Fail: https://www.hide-my-ip.com/proxylist.shtml http://www.89ip.cn/
 * http://ip.zdaye.com/ http://proxy.goubanjia.com/ http://www.mayidaili.com/
 * http://ip.izmoney.com/ http://ip004.com/
 */
