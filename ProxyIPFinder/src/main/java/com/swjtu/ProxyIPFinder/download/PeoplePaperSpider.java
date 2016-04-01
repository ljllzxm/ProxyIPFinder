package com.swjtu.ProxyIPFinder.download;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

public class PeoplePaperSpider {
	private String[] nums = { "", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14",
			"15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" };
	private int[] daysOfMonth = { 0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	private int currentYear, currentMonth, currentDay;
	private String urlHead = "http://paper.people.com.cn/rmrb/html/";
	private String[] nbsUrlEnds = { "nbs.D110000renmrb_01.htm", "nbs.D110000renmrb_02.htm", "nbs.D110000renmrb_03.htm",
			"nbs.D110000renmrb_04.htm" };
	private String url = new String();

	public void setBeginTime(int year, int month, int day) {
		currentYear = year;
		currentMonth = month;
		currentDay = day;
		url = urlHead + year + "-" + nums[month] + "/" + nums[day];
	}

	public PeoplePaperSpider(int year, int month, int day) {
		currentYear = year;
		currentMonth = month;
		currentDay = day;
		url = urlHead + year + "-" + nums[month] + "/" + nums[day];
	}

	private void generateNextTime() {
		currentDay--;
		if (currentDay == 0) {
			currentMonth--;
			if (currentMonth == 0) {
				currentYear--;
				currentMonth = 12;
				currentDay = 31;
			} else
				currentDay = daysOfMonth[currentMonth];
		}
	}

	public void generateNextUrl() {
		generateNextTime();
		url = urlHead + currentYear + "-" + nums[currentMonth] + "/" + nums[currentDay];
	}

	public void downloadText() {

		Downloader dl = new Downloader();
		// 新闻保存路径
		File newsFile = new File("E:\\peoplepapernews");
		newsFile.mkdirs();
		FileWriter newsfw = null;

		// 抽取每日新闻，再生成前一天新闻主页链接，循环抽取。
		while (true) {
			for (int i = 0; i < 4; i++) {
				// htm文件保存路径
				File htmFile = new File("E:\\peoplepaperhtm\\" + currentYear + nums[currentMonth] + nums[currentDay]);
				htmFile.mkdirs();
				FileWriter htmfw = null;

				
				// 下载每日新闻主页内容（内含本日新闻链接）
				String text = dl.getWebContent(url + "/" + nbsUrlEnds[i]);
				if(text == null) continue;
				try {
					htmfw = new FileWriter(new File(htmFile.getAbsolutePath() + "\\title.htm"));
					htmfw.write(text);
					htmfw.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}

				// 创建内层文件夹，存放新闻htm页面
				htmFile = new File(
						"E:\\peoplepaperhtm\\" + currentYear + nums[currentMonth] + nums[currentDay] + "\\newshtm");
				htmFile.mkdirs();

				HtmlCleaner hc = new HtmlCleaner();
				TagNode tn = hc.clean(text);

				// 抽取具体新闻链接
				String xpath1 = ".//*[@id='titleList']/ul/li/a/@href";
				Object[] objarr = null;
				try {
					objarr = tn.evaluateXPath(xpath1);
				} catch (XPatherException e) {
					e.printStackTrace();
				}
				try {
					newsfw = new FileWriter(new File(newsFile.getAbsolutePath() + "\\" + currentYear + nums[currentMonth] + nums[currentDay] +"_" + (i + 1) + ".txt"));
				} catch (Exception e) {
					e.printStackTrace();
				}

				for (int j = 0; j < objarr.length; j++) {
					String newtitle = (String) objarr[j];
					text = dl.getWebContent(url + "/" + objarr[j]);
					if(text == null) continue;
					Pattern p1 = Pattern.compile(".*_(.*_.*)\\..*");
					Matcher m1 = p1.matcher(newtitle);
					if(m1.find())
						try {
							htmfw = new FileWriter(new File(htmFile.getAbsolutePath() + "\\" + m1.group(1) + ".htm"));
							htmfw.write(text);
							htmfw.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
					else continue;

					TagNode ntn = hc.clean(text);
					String xpath2 = ".//*[@id='ozoom']/p/text()";
					Object[] news = null;
					try {
						news = ntn.evaluateXPath(xpath2);
					} catch (XPatherException e1) {
						e1.printStackTrace();
					}
					if(news == null || news.length < 1) continue;
					String p = news[0].toString();
					Pattern p2 = Pattern.compile("(.*（.*）)(.*)");
					Matcher m2 = p2.matcher(p);
					if (m2.find())
						news[0] ="  " + m2.group(2);
					else{
						p2 = Pattern.compile("(.*电(&nbsp;)+)(.*)");
						m2 = p2.matcher(p);
						if(m2.find())
							news[0] = "  "+m2.group(3);							
					}
					for (int k = 0; k < news.length; k++) {
						p = news[k].toString();
						if (p.length() < 25)
							continue;
						try {
							newsfw.write(p.substring(2)+ System.getProperty("line.separator"));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

				try {
					newsfw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			generateNextUrl();
		}
	}

	public static void main(String[] args) {
		PeoplePaperSpider pps = new PeoplePaperSpider(2016, 3, 4);
		pps.downloadText();
	}

}
 