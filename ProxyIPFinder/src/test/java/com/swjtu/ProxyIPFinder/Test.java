package com.swjtu.ProxyIPFinder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String p = "　　习近平指出";
		Pattern pattern = Pattern.compile("(.*电(&nbsp;)+)(.*)");
		Matcher m = pattern.matcher(p);
	}

}
