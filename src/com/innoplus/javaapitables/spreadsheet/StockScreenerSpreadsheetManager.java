package com.innoplus.javaapitables.spreadsheet;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.util.ServiceException;
import com.innoplus.javaapitables.data.options.StockScreener;
import com.innoplus.javaapitables.data.options.YahooStockScraper;

public class StockScreenerSpreadsheetManager extends BaseSpreadsheetManager {
	private static Logger logger = Logger.getLogger(StockScreenerSpreadsheetManager.class);
	static final String TableName = "STOCK-SCREENER";
	private static final int FIRST_ROW = 2;
	private static final int LAST_ROW = 51;
	private static final String TICKER = "B";
	
	@Override
	protected String getSpreadsheetName() {
		// TODO Auto-generated method stub
		return TableName;
	}

	@Override
	protected void initializeSettings() {
		// TODO Auto-generated method stub
	}

	public static void main(String[] args) {
		new StockScreenerSpreadsheetManager().update();
	}
	
	public void update() {
		Object object = getSyncLockForSheet(TableName);
		synchronized (object) {

			try {
        		CellFeed tickerCellFeed = getCellFeed(TICKER, FIRST_ROW, LAST_ROW);

        		CellFeed columnFCellFeed = getCellFeed("F", FIRST_ROW, LAST_ROW);
        		CellFeed columnGCellFeed = getCellFeed("G", FIRST_ROW, LAST_ROW);
        		CellFeed columnJCellFeed = getCellFeed("J", FIRST_ROW, LAST_ROW);
        		CellFeed columnDCellFeed = getCellFeed("D", FIRST_ROW, LAST_ROW);
        		CellFeed columnNCellFeed = getCellFeed("N", FIRST_ROW, LAST_ROW);
        		CellFeed columnOCellFeed = getCellFeed("O", FIRST_ROW, LAST_ROW);
        		
        		int i = 0;
        		for (CellEntry localCellEntry : tickerCellFeed.getEntries()) {
        			String ticker = localCellEntry.getCell().getValue();
        			try {
	        				        			
	        			if (ticker == null || "".equals(ticker)) {
	        				i++;
	        				continue;
	        			}
	
	        			StockScreener stockScreener = new YahooStockScraper().scrap(ticker);
	        			
	        			CellEntry localCellEntry1 = (CellEntry)columnFCellFeed.getEntries().get(i);	        			
	        			localCellEntry1.changeInputValueLocal(stockScreener.getColumnF());	        			
	   			      	localCellEntry1.update();

	   			      	CellEntry localCellEntry2 = (CellEntry)columnGCellFeed.getEntries().get(i);
	   			      	localCellEntry2.changeInputValueLocal(stockScreener.getColumnG());	        			
	   			      	localCellEntry2.update();
	   			      	
	   			      	CellEntry localCellEntry3 = (CellEntry)columnJCellFeed.getEntries().get(i);
	        			localCellEntry3.changeInputValueLocal(stockScreener.getColumnJ());	        			
	   			      	localCellEntry3.update();
	   			      	
	   			      	CellEntry localCellEntry4 = (CellEntry)columnDCellFeed.getEntries().get(i);
	        			localCellEntry4.changeInputValueLocal(stockScreener.getColumnD());	        			
	   			      	localCellEntry4.update();
	   			      	
	   			      	CellEntry localCellEntry5 = (CellEntry)columnNCellFeed.getEntries().get(i);
	        			localCellEntry5.changeInputValueLocal(stockScreener.getColumnN());	        			
	   			      	localCellEntry5.update();
	   			      	
		   			 	CellEntry localCellEntry6 = (CellEntry)columnOCellFeed.getEntries().get(i);
	        			localCellEntry6.changeInputValueLocal(stockScreener.getColumnO());	        			
	   			      	localCellEntry6.update();
   			      	
	        			i++;
        			} catch (Exception e) {
        				logger.error("Error while updating ticker [" + ticker + "]", e);
        				continue;
        			}
                }
        		 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}

}
