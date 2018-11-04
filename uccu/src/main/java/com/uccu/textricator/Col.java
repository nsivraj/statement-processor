package com.uccu.textricator;

public class Col {
	private String data;
	private String ulx;
	private String lrx;
	
	public Col(String data, String ulx, String lrx) {
		this.data = data;
		this.ulx = ulx;
		this.lrx = lrx;
	}
	
	public String getUlx() {
		return ulx;
	}
	
	public String getLrx() {
		return lrx;
	}
	
	public void setUlx(String ulx) {
		this.ulx = ulx;
	}
	
	public void setLrx(String lrx) {
		this.lrx = lrx;
	}
	
	public void appendData(String newData) {
		data += newData;
	}
	
	public String getData() {
		return data;
	}
	
	public String toString() {
		return getData();
	}
}
