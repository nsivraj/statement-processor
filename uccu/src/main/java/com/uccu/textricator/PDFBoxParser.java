package com.uccu.textricator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PDFBoxParser {

    public final static UccuStatement parse(File fileToParse) {
    	
    	Path parsePath = fileToParse.getPath();
    	String statementFilename = parsePath.getFileName().toString();
    	int lastIndexOfDot = statementFilename.lastIndexOf('.');
    	if(lastIndexOfDot != -1) {
    		statementFilename = statementFilename.substring(0, lastIndexOfDot);
    	}
    	
        UccuStatement statement = new UccuStatement(
        	Paths.get(parsePath.getParent().getParent().toString(),
        	statementFilename + ".processed.csv")
        );
        
        try {
            
        	Reader reader = Files.newBufferedReader(fileToParse.getPath());
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim());
        
            // page,ulx,uly,lrx,lry,width,height,content,font,fontSize,fontColor,bgcolor
            Row theRow = null;
            ColumnSpec colSpec = null;
            String page = null;
            String ulx = null;
            String uly = null;
            String lrx = null;
            String lry = null;
            //int csvRecordCount = 0;
            for (CSVRecord csvRecord : csvParser) {
                // Accessing values by Header names
                page = csvRecord.get("page");
                ulx = csvRecord.get("ulx");
                uly = csvRecord.get("uly");
                lrx = csvRecord.get("lrx");
                lry = csvRecord.get("lry");
                //csvRecordCount++;
                
                //if page and uly and lry are the same then the content is in the same row
                if(theRow == null || !theRow.matches(page, uly, lry)) {
                	if(theRow != null) {
                		statement.updateMaxCols(theRow);
                		colSpec = theRow.selectColumnSpec();
                	}
                	theRow = new Row(page, uly, lry, colSpec);
                    statement.addRow(theRow);
                }
                theRow.addData(csvRecord.get("content"), ulx, lrx);
                
//                if(csvRecordCount > 200) {
//                	break;
//                }
            }
        } catch (Exception ex) {
        	ex.printStackTrace();
        }

        return statement;
    }


}



/*

"[Account Type, Deposits, Withdrawals, Ending Balance]"

"[Balance, +, Deposits, +, Paid, -, Withdrawals, -, Charges, =, Balance]"

"[Date, Date, Transaction Description, Withdrawal, Deposit, Ending Balance]"

*/