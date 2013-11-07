package com.innoplus.javaapitables.servlet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.google.gdata.data.spreadsheet.CellFeed;
import com.innoplus.javaapitables.spreadsheet.IndustrySectorsSpreadsheetManager;
import com.innoplus.javaapitables.spreadsheet.LastWeekPriceUpdateSpreadsheetManager;
import com.innoplus.javaapitables.spreadsheet.MarketCapSpreadsheetManager;
import com.innoplus.javaapitables.spreadsheet.MarketIndexesSpreadsheetManager;
 
public class WeeklyJob implements Job {
	private static Log log = LogFactory.getLog(WeeklyJob.class);
	
    public void execute(JobExecutionContext jec) throws JobExecutionException {
    	log.info("WeeklyJob starting...");
    	    	
    	// MARKET INDEXES / INDUSTRY SECTORS
    	try {
             MarketIndexesSpreadsheetManager.getInstance().updateWeekly(5);
        } catch (Exception ex) {
        	log.error("MarketIndexesSpreadsheetManager error", ex);
        }

        try {
            IndustrySectorsSpreadsheetManager.getInstance().updateWeekly(11);
        } catch (Exception ex) {
        	 log.error("IndustrySectorsSpreadsheetManager error", ex);
        }

    }
    
}