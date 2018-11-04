package com.uccu.textricator;

public class ColBoundary {
	protected String ulx;
	protected String lrx;
	
	public ColBoundary(String ulx, String lrx) {
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
	
}
