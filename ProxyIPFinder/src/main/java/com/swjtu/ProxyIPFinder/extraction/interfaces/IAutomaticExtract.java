package com.swjtu.ProxyIPFinder.extraction.interfaces;

public interface IAutomaticExtract<T> {

	public void setWebContent(String content);
	
	public T[] getAllResult();
	
}
