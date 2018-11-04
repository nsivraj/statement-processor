package com.uccu.textricator;

public class Col extends ColBoundary {
	
	private String data;
	
	public Col(String data, String ulx, String lrx) {
		super(ulx, lrx);
		this.data = data;
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
