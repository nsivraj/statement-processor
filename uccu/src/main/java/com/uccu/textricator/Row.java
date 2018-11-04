package com.uccu.textricator;

import java.util.ArrayList;
import java.util.List;

public class Row {
	
	public static final Double SPACE_SIZE = 5.5;
	public static final Double COORD_DELTA = 0.8;
	
	private List<Col> data;
	private ColumnSpec colSpec;
	private String page;
	private String uly;
	private String lry;
	
	public Row(String page, String uly, String lry, ColumnSpec colSpec) {
		data = new ArrayList<Col>();
		this.page = page;
		this.uly = uly;
		this.lry = lry;
		this.colSpec = colSpec;
	}
	
	public boolean matches(String page, String uly, String lry) {
		if(this.page.equalsIgnoreCase(page) && 
			sameCoordinate(this.uly, uly) &&
			sameCoordinate(this.lry, lry)) {
			return true;
		}
		
		return false;
	}
	
	public boolean sameCoordinate(String coord1, String coord2) {
		double c1 = Double.parseDouble(coord1);
		double c2 = Double.parseDouble(coord2);
		return Math.abs(c1 - c2) < COORD_DELTA;
	}
	
	public List<Col> getData() {
		return data;
	}
	
	public int numberOfCols() {
		return data.size();
	}
	
	public String toString() {
		return data.toString();
	}
	
	private boolean combineData(String colData, String newData) {
		//Balance,+,Deposits,+,Paid,-,Withdrawals,-,Charges,=,Balance
		if("Balance".equalsIgnoreCase(colData) && "+".equals(newData) ||
			"Deposits".equalsIgnoreCase(colData) && "+".equals(newData) ||
			"Paid".equalsIgnoreCase(colData) && "-".equals(newData) ||
			"Withdrawals".equalsIgnoreCase(colData) && "-".equals(newData) ||
			"Charges".equalsIgnoreCase(colData) && "=".equals(newData)) {
			return true;
		}
		return false;
	}
	
	private void addColUsingColumnSpec(int newIndex, String newData, String ulx, String lrx) {
		if(colSpec != null) {
			// find out into which column from the colSpect that the ulx and lrx fit
			// then insert the new Col into that index in the data list
		} else {
			data.add(newIndex, new Col(newData, ulx, lrx));
		}
	}
	
	private void addOrUpdateCol(int newIndex, String newData, String ulx, String lrx) {
		Col col = null;
		if(newIndex < data.size()) {
			col = (Col) data.get(newIndex);
		} else if(newIndex != 0) {
			col = (Col) data.get(newIndex - 1);
		}
		
		if(col != null) {
			double colCoord = Double.parseDouble(col.getLrx());
			double newCoord = Double.parseDouble(ulx);
			//System.out.println("The difference between '"+col.getData()+"' and '"+newData+"' is '"+(newCoord - colCoord)+"'");
			if(Math.abs(newCoord - colCoord) < SPACE_SIZE || combineData(col.getData(), newData)) {
				col.appendData(" "+newData);
				col.setUlx(ulx);
				col.setLrx(lrx);
			} else {
				//data.add(newIndex, new Col(newData, ulx, lrx));
				addColUsingColumnSpec(newIndex, newData, ulx, lrx);
			}
		} else {
			//data.add(newIndex, new Col(newData, ulx, lrx));
			addColUsingColumnSpec(newIndex, newData, ulx, lrx);
		}
	}
	
	public void addData(String newData, String ulx, String lrx) {
		// insert the data at the index where the new ulx is less than the current Col's ulx
		int colCount = data.size();
		for(int newIndex = 0; newIndex < colCount; ++newIndex) {
			Col col = data.get(newIndex);
			double colCoord = Double.parseDouble(col.getUlx());
			double newCoord = Double.parseDouble(lrx);
			if(newCoord < colCoord) {
				addOrUpdateCol(newIndex, newData, ulx, lrx);
				break;
			}
		}
		if(data.size() == colCount) {
			addOrUpdateCol(colCount, newData, ulx, lrx);
		}
	}
	
	public boolean rowStartsNewColumnSpec() {
		//inspect data to see if this rows data is a new column spec
		//Date,Date,Transaction Description,Withdrawal,Deposit,Ending Balance
		if("Date".equalsIgnoreCase(data.get(0).getData()) &&
			"Date".equalsIgnoreCase(data.get(1).getData()) &&
			"Transaction Description".equalsIgnoreCase(data.get(2).getData()) &&
			"Withdrawal".equalsIgnoreCase(data.get(3).getData()) &&
			"Deposit".equalsIgnoreCase(data.get(4).getData()) &&
			"Ending Balance".equalsIgnoreCase(data.get(5).getData())) {
			
			return true;
		}
		
		return false;
	}

	public boolean rowEndsColumnSpec() {
		if("Total For".equalsIgnoreCase(data.get(0).getData()) &&
			"Total".equalsIgnoreCase(data.get(1).getData())) {
			
			return true;
		}
		
		return false;
	}
	
	public ColumnSpec selectColumnSpec() {
		if(rowStartsNewColumnSpec()) {
			colSpec = new ColumnSpec(data);
		} else if(rowEndsColumnSpec()) {
			colSpec = null;
		}
		
		return colSpec;
	}
}
