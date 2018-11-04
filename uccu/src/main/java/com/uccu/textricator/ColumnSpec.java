package com.uccu.textricator;

import java.util.List;

public class ColumnSpec {
	
	private List bounds;
	
	public ColumnSpec() { }
	
	public ColumnSpec(List bounds) {
		this.bounds = bounds;
	}
	
	public void addBoundary(String ulx, String lrx) {
		bounds.add(new ColBoundary(ulx, lrx));
	}
	
	public void addBoundary(ColBoundary boundary) {
		bounds.add(boundary);
	}
}
