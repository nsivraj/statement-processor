package com.uccu.textricator;

import java.util.ArrayList;
import java.util.List;

public class ColumnLayout {
	
	private List<Col> columns;
	
	public ColumnLayout() {
		columns = new ArrayList<>();
	}
	
	public ColumnLayout(List<Col> columns) {
		this.columns = columns;
	}
	
	public void addColumn(String data, String ulx, String lrx) {
		columns.add(new Col(data, ulx, lrx));
	}
	
	public void addColumn(Col column) {
		columns.add(column);
	}
	
	public List<Col> getColumns() {
		return columns;
	}

	public int size() {
		return columns.size();
	}

	public Col get(int index) {
		return columns.get(index);
	}

	public void add(int index, Col column) {
		columns.add(index, column);
	}
	
	@Override
	public String toString() {
		String strCol = columns.toString();
		return strCol.substring(1, strCol.length() - 1);
	}

	public void adjustColumnLayout(ColumnLayout layout) {
		// make my columns variable match the columns variable
		// of layout except for the actual content which needs
		// to remain the same content as in my columns
		
		if(columns.size() > layout.size()) {
			throw new CannotAdjustColumnLayoutException("The size of '"+columns+"' is greater than '"+layout.columns+"'");
		}
		
		List<Col> newColumns = new ArrayList<>(layout.size());
		for(int columnsIndex = 0; columnsIndex < columns.size(); ++columnsIndex) {
			Col columnsCol = columns.get(columnsIndex);
			for(int layoutIndex = newColumns.size(); layoutIndex < layout.size(); ++layoutIndex) {
				Col layoutCol = layout.columns.get(layoutIndex);
				if(columnsCol.matchesBoundary(layoutCol)) {
					if(Double.parseDouble(columnsCol.ulx) > Double.parseDouble(layoutCol.ulx)) {
						columnsCol.setUlx(layoutCol.ulx);
					}
					if(Double.parseDouble(columnsCol.lrx) < Double.parseDouble(layoutCol.lrx)) {
						columnsCol.setLrx(layoutCol.lrx);
					}
					if(Double.parseDouble(columnsCol.ulx) < Double.parseDouble(layoutCol.ulx)) {
						layoutCol.setUlx(columnsCol.ulx);
					}
					if(Double.parseDouble(columnsCol.lrx) > Double.parseDouble(layoutCol.lrx)) {
						layoutCol.setLrx(columnsCol.lrx);
					}
					newColumns.add(layoutIndex, columnsCol);
					break;
				} else {
					newColumns.add(layoutIndex, new Col("", layoutCol.ulx, layoutCol.lrx));
				}
			}
		}
		columns = newColumns;
	}
}
