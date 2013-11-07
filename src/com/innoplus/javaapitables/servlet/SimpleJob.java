package com.innoplus.javaapitables.servlet;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.innoplus.javaapitables.spreadsheet.PremiumCoveredCallSpreadsheetManager;
import com.innoplus.javaapitables.spreadsheet.PremiumCoveredPutSpreadsheetManager;
import com.innoplus.javaapitables.spreadsheet.PublicCoveredCallSpreadsheetManager;
import com.innoplus.javaapitables.spreadsheet.PublicCoveredPutSpreadsheetManager;
 
public class SimpleJob implements Job {
	private static Log log = LogFactory.getLog(SimpleJob.class);
    public void execute(JobExecutionContext jec) throws JobExecutionException {
    	System.out.println("doing update tables at " + new Date());
    	
    }
}