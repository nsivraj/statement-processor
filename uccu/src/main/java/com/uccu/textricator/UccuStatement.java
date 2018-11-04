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
	
	public UccuStatement(final Path path) {
		super(path);
		rows = new ArrayList<Row>();
		maxCols = 0;
	}
	
	public int getYear() {
		return year;
	}
	
	public int getMonth() {
		return month;
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
		List<String> colHeaders = new ArrayList<String>();
		for(int i = 0; i < maxCols; ++i) {
			colHeaders.add("col"+(i+1));
		}
		try {
            BufferedWriter writer = Files.newBufferedWriter(this.getPath());

            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                    .withHeader(colHeaders.toArray(new String[0])));
        
//            csvPrinter.printRecord("1", "Sundar Pichai ♥", "CEO", "Google");
//            csvPrinter.printRecord("2", "Satya Nadella", "CEO", "Microsoft");
//            csvPrinter.printRecord("3", "Tim cook", "CEO", "Apple");
//
//            csvPrinter.printRecord(Arrays.asList("4", "Mark Zuckerberg", "CEO", "Facebook"));

            for(Row row : rows) {
            	csvPrinter.printRecord(row.getData());
            }
            
            csvPrinter.flush();            
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}