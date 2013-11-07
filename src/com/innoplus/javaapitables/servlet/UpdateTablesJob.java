package com.innoplus.javaapitables.servlet;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.innoplus.javaapitables.core.Configuration;
import com.innoplus.javaapitables.spreadsheet.PremiumCoveredCallSpreadsheetManager;
import com.innoplus.javaapitables.spreadsheet.PremiumCoveredPutSpreadsheetManager;
import com.innoplus.javaapitables.spreadsheet.PublicCoveredCallSpreadsheetManager;
import com.innoplus.javaapitables.spreadsheet.PublicCoveredPutSpreadsheetManager;
 
public class UpdateTablesJob implements Job {
	private static Log log = LogFactory.getLog(UpdateTablesJob.class);
	private static String HOLIDAYS[] = Configuration.getInstance().getProperty("HOLIDAYS").split(",");
	private static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    public void execute(JobExecutionContext jec) throws JobExecutionException {
    	log.info("doing update tables");
    	
    	for (String HOLIDAY: HOLIDAYS) {
    		if (HOLIDAY.equals(sdf.format(new Date()))) {
    			log.info(HOLIDAY + " is HOLIDAY!");
				return;
			}
    	}
    	
    	try {
    		log.info("doing update PremiumCoveredCallSpreadsheet");
            PremiumCoveredCallSpreadsheetManager.getInstance().updateDataInSpreadsheet();
        } catch (Exception ex) {
        	 log.error("update PremiumCoveredCallSpreadsheet error", ex);
        }
    	
    	try {
    		log.info("doing update PremiumCoveredPutSpreadsheet");
    		PremiumCoveredPutSpreadsheetManager.getInstance().updateDataInSpreadsheet();
        } catch (Exception ex) {
        	 log.error("update PremiumCoveredPutSpreadsheet error", ex);
        }
    	
    	try {
    		log.info("doing update PublicCoveredCallSpreadsheet");
    		PublicCoveredCallSpreadsheetManager.getInstance().updateDataInSpreadsheet();
        } catch (Exception ex) {
        	 log.error("update PublicCoveredCallSpreadsheet error", ex);
        }
    	
    	try {
    		log.info("doing update PublicCoveredPutSpreadsheet");
    		PublicCoveredPutSpreadsheetManager.getInstance().updateDataInSpreadsheet();
        } catch (Exception ex) {
        	 log.error("update PublicCoveredPutSpreadsheet error", ex);
        }
    	
    	// do second time for NA value
    	
    	log.info("refresh tables for NA values");
    	
    	for (int i=0; i< 2; i++) {
    		
    		log.info("refresh times: " + i);
    		
	    	try {
	    		log.info("doing update PremiumCoveredCallSpreadsheet for NA values");
	            PremiumCoveredCallSpreadsheetManager.getInstance().updateDataInSpreadsheet1();
	        } catch (Exception ex) {
	        	 log.error("update PremiumCoveredCallSpreadsheet error", ex);
	        }
	    	
	    	try {
	    		log.info("doing update PremiumCoveredPutSpreadsheet");
	    		PremiumCoveredPutSpreadsheetManager.getInstance().updateDataInSpreadsheet1();
	        } catch (Exception ex) {
	        	 log.error("update PremiumCoveredPutSpreadsheet error", ex);
	        }
	    	
	    	try {
	    		log.info("doing update PublicCoveredCallSpreadsheet");
	    		PublicCoveredCallSpreadsheetManager.getInstance().updateDataInSpreadsheet1();
	        } catch (Exception ex) {
	        	 log.error("update PublicCoveredCallSpreadsheet error", ex);
	        }
	    	
	    	try {
	    		log.info("doing update PublicCoveredPutSpreadsheet");
	    		PublicCoveredPutSpreadsheetManager.getInstance().updateDataInSpreadsheet1();
	        } catch (Exception ex) {
	        	 log.error("update PublicCoveredPutSpreadsheet error", ex);
	        }
	    	
	    	try {
	    		
	    		log.info("wait for 1 minute");
	    		
				Thread.sleep(60*1000);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	String Email_Sub = "Error Message: PremPut, PremCall, PubPut, PubCall";
	    	String text = "Refresh program couldn't fix N/A error for:\n";
	    	text += PremiumCoveredCallSpreadsheetManager.getInstance().countErrors() + "\n";
	    	text += PremiumCoveredPutSpreadsheetManager.getInstance().countErrors() + "\n";
	    	text += PublicCoveredCallSpreadsheetManager.getInstance().countErrors() + "\n";
	    	text += PublicCoveredPutSpreadsheetManager.getInstance().countErrors() + "\n";
	    	
	    	try {
	    		
	    		Configuration localConfiguration = Configuration.getInstance();
	    	    String username = localConfiguration.getProperty("google.username");
	    	    String password = localConfiguration.getProperty("google.password");
	    	        
		    	Email email = new SimpleEmail();
		    	email.setHostName("smtp.gmail.com");
		    	email.setSmtpPort(465);
		    	email.setAuthenticator(new DefaultAuthenticator(username, password));
		    	email.setSSL(true);
		    	email.setFrom(password);
		    	email.setSubject(Email_Sub);
		    	email.setMsg(text);
		    	email.addTo("rh@doubledividendstocks.com");
		    	email.send();
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}

	    	
    	}
    }
}