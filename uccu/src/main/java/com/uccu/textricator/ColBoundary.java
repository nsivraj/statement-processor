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

	public void setUlx(String ulx, int i) {
		this.ulx = String.valueOf(Double.parseDouble(ulx) + i);
	}
	
	public void setLrx(String lrx) {
		this.lrx = lrx;
	}
	
	public void setLrx(String lrx, int i) {
		this.lrx = String.valueOf(Double.parseDouble(lrx) + i);
	}
	
	public String boundsAsString() {
		return "("+ulx+", "+lrx+")";
	}
	
	public boolean insideBoundaryInclusive(double val, double ULX, double LRX) {
		// is val inside inclusive the ULX and LRX interval
		return (val >= ULX && val <= LRX);
	}
	
	public boolean insideBoundaryInclusive(double val1, double val2, double ULX, double LRX) {
		// is val1 and val2 inside inclusive the ULX and LRX interval
		return (insideBoundaryInclusive(val1, ULX, LRX) && insideBoundaryInclusive(val2, ULX, LRX));
	}
	
	public boolean matchesBoundary(ColBoundary bound) {
		System.out.println("See if my boundary: " + this.boundsAsString());
		System.out.println("Matches bound boundary: " + bound.boundsAsString());
		
		double myUlx = Double.parseDouble(ulx);
		double myLrx = Double.parseDouble(lrx);
		double boundUlx = Double.parseDouble(bound.ulx);
		double boundLrx = Double.parseDouble(bound.lrx);

		if(insideBoundaryInclusive(myUlx, boundUlx, boundLrx) ||
				insideBoundaryInclusive(myLrx, boundUlx, boundLrx) ||
				insideBoundaryInclusive(boundUlx, myUlx, myLrx) ||
				insideBoundaryInclusive(boundLrx, myUlx, myLrx)) {
			return true;
		} else {
			return false;
		}
	}
}
