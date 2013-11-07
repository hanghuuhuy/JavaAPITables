package com.innoplus.javaapitables.servlet;
import java.text.SimpleDateFormat;
import java.util.Date;

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
 
public class MonthlyJob implements Job {
	private static final String FIRST_DAY_OF_NEW_YEAR[] = {"01/01/2014", "01/01/2015", "01/01/2016", "01/01/2017", "01/01/2018"};
	private static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	private static Log log = LogFactory.getLog(MonthlyJob.class);
	
    public void execute(JobExecutionContext jec) throws JobExecutionException {
    	log.info("MonthlyJob starting...");
    	    	
    	// MARKET INDEXES / INDUSTRY SECTORS
    	try {
             MarketIndexesSpreadsheetManager.getInstance().updateMonthly(5);
        } catch (Exception ex) {
        	log.error("MarketIndexesSpreadsheetManager error", ex);
        }

        try {
            IndustrySectorsSpreadsheetManager.getInstance().updateMonthly(11);
        } catch (Exception ex) {
        	 log.error("IndustrySectorsSpreadsheetManager error", ex);
        }

        try {
            MarketCapSpreadsheetManager.getInstance().updateMonthly(10);
        } catch (Exception ex) {
        	 log.error("IndustrySectorsSpreadsheetManager error", ex);
        }
        
        for (String HOLIDAY: FIRST_DAY_OF_NEW_YEAR) {
    		if (HOLIDAY.equals(sdf.format(new Date()))) {
    			log.info("Today is first day of new year!");
    			
    			IndustrySectorsSpreadsheetManager.getInstance().updateYearly("J", "L", 5);
    			MarketCapSpreadsheetManager.getInstance().updateYearly("H", "G", 10);
    			
    			break;
			}
    	}
    }
    
}