package com.uccu.textricator;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class UccuStatement extends File {
	
	private int year;
	private int month;
	private List<Row> rows;
	private int maxCols;
	private Row currentTransactionHeaderRow;
	
	public UccuStatement(final Path path) {
		super(path);
		rows = new ArrayList<Row>();
		maxCols = 0;
		currentTransactionHeaderRow = null;
	}
	
	public int getYear() {
		return year;
	}
	
	public int getMonth() {
		return month;
	}

	public boolean isEndingBalanceEquation(Row row) {
		// "Balance +,Deposits +,Paid -,Withdrawals -,Charges =,Balance"
		String csvRow = row.toString();
		if(csvRow.startsWith("Balance") && csvRow.contains("Deposits") &&
				csvRow.contains("Paid") && csvRow.contains("Withdrawals") &&
				csvRow.contains("Charges") && csvRow.endsWith("Balance")) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isTransactionHeader(Row row) {
		// "Date,Date,Transaction Description,Withdrawal,Deposit,Ending Balance"
		String csvRow = row.toString();
		if(csvRow.startsWith("Date") && csvRow.contains("Date") &&
				csvRow.contains("Transaction Description") && csvRow.contains("Withdrawal") &&
				csvRow.contains("Deposit") && csvRow.endsWith("Ending Balance")) {
			currentTransactionHeaderRow = row;
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isEndOfTransactions(Row row) {
		// """,,Total For,Total,,"
		// "The average daily balance during this period was"
		String csvRow = row.toString();
		if((csvRow.contains("Total For") && csvRow.contains(", Total, ")) ||
				csvRow.startsWith("The average daily balance during this period was")) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isPageNumberHeader(Row row) {
		// 614224,01-01-2018 to 01-31-2018,2 of 5,,,
		String csvRow = row.toString();
		if(csvRow.startsWith("614224") && csvRow.contains(" to ") &&
				csvRow.contains(" of ")) {
			return true;
		} else {
			return false;
		}
	}
	
	public Row doesColumnLayoutNeedAdjusting(Row row) {
		if(isPageNumberHeader(row)) {
			return null;
		}
		
		if(isEndOfTransactions(row)) {
			currentTransactionHeaderRow = null;
		}
		
		Row rowToAdjust = null;
		if(isEndingBalanceEquation(row) || isTransactionHeader(row)) {
			rowToAdjust = rows.get(rows.size() - 2);
		} else if (currentTransactionHeaderRow != null) {
			rowToAdjust = row;
		}
		return rowToAdjust;
	}
	
	public Row getRowContainingCurrentColumnLayout() {
		return currentTransactionHeaderRow;
	}
	
	public void addRow(Row row) {
		rows.add(row);
	}

	public void updateMaxCols(Row row) {
		if(row != null) {
			//System.out.println(row.numberOfCols());
			if(maxCols < row.numberOfCols()) {
				maxCols = row.numberOfCols();
			}
		}
	}
	
	public void writeCSV() {
		System.out.println("statement has '"+rows.size()+"' rows and '"+maxCols+"' columns!");
		List<Object> csvData = new ArrayList<Object>(maxCols);
		for(int i = 0; i < maxCols; ++i) {
			csvData.add("col"+(i+1));
		}
		try {
            BufferedWriter writer = Files.newBufferedWriter(this.getPath());

            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                    .withHeader(csvData.toArray(new String[0])));
        
//            csvPrinter.printRecord("1", "Sundar Pichai ♥", "CEO", "Google");
//            csvPrinter.printRecord("2", "Satya Nadella", "CEO", "Microsoft");
//            csvPrinter.printRecord("3", "Tim cook", "CEO", "Apple");
//
//            csvPrinter.printRecord(Arrays.asList("4", "Mark Zuckerberg", "CEO", "Facebook"));

            List<Col> rowData;
            int colIndex;
            for(Row row : rows) {
            	rowData = row.getData();
            	colIndex = 0;
            	for(Col col : rowData) {
            		csvData.set(colIndex, col);
            		++colIndex;
            	}
            	while(colIndex < maxCols) {
            		csvData.set(colIndex, "");
            		++colIndex;
            	}
            	csvPrinter.printRecord(csvData);
            }
            
            csvPrinter.flush();
            csvPrinter.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}