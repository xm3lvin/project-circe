package com.kjt.circe.domain;

public interface Printer {
	
	String getRid();
	
	String getPid();
	
	boolean print(PrintTask task) throws Exception;
	
	void connect(int timeout) throws Exception;
	
	void close();

}
