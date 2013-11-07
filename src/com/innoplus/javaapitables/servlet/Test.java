package com.innoplus.javaapitables.servlet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

import com.innoplus.javaapitables.core.Configuration;
import com.innoplus.javaapitables.spreadsheet.IndustrySectorsSpreadsheetManager;
import com.innoplus.javaapitables.spreadsheet.MarketIndexesSpreadsheetManager;
import com.innoplus.javaapitables.spreadsheet.PremiumCoveredCallSpreadsheetManager;
import com.innoplus.javaapitables.spreadsheet.PremiumCoveredPutSpreadsheetManager;
import com.innoplus.javaapitables.spreadsheet.PublicCoveredCallSpreadsheetManager;
import com.innoplus.javaapitables.spreadsheet.PublicCoveredPutSpreadsheetManager;

public class Test {
	private static String HOLIDAYS[] = Configuration.getInstance().getProperty("HOLIDAYS").split(",");
	private static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		System.out.println(sdf.format(new Date()));
//		for (String HOLIDAY: HOLIDAYS) {
//			if (HOLIDAY.equals(sdf.format(new Date()))) {
//				System.out.println("xxxxxx");
//				return;
//			}	
//    	}
//		
//		System.out.println("yyyyyyyyyyyyyyy");
		
//		MarketIndexesSpreadsheetManager.getInstance().updateDataInSpreadsheet();
//		System.out.println("N/A (N/A)".startsWith("N/A"));
		
//		PremiumCoveredPutSpreadsheetManager.getInstance().updateDataInSpreadsheet1();
		
//		String Email_Sub = "Error Message: PremPut, PremCall, PubPut, PubCall";
//    	String text = "Refresh program couldn't fix N/A error for:\n";
//    	text += PremiumCoveredCallSpreadsheetManager.getInstance().countErrors() + "\n";
//    	text += PremiumCoveredPutSpreadsheetManager.getInstance().countErrors() + "\n";
//    	text += PublicCoveredCallSpreadsheetManager.getInstance().countErrors() + "\n";
//    	text += PublicCoveredPutSpreadsheetManager.getInstance().countErrors() + "\n";
//    	
//    	try {
//	    	Email email = new SimpleEmail();
//	    	email.setHostName("smtp.gmail.com");
//	    	email.setSmtpPort(465);
//	    	email.setAuthenticator(new DefaultAuthenticator("testing.hanghuuhuy.testing00@gmail.com", "iogtmqxmwbvcxioc"));
//	    	email.setSSL(true);
//	    	email.setFrom("testing.hanghuuhuy.testing00@gmail.com");
//	    	email.setSubject(Email_Sub);
//	    	email.setMsg(text);
//	    	email.addTo("hanghuuhuy@yahoo.com");
//	    	email.send();
//    	} catch (Exception e) {
//    		e.printStackTrace();
//    	}

//		 MarketIndexesSpreadsheetManager.getInstance().updateDataInSpreadsheet1();
		
//		IndustrySectorsSpreadsheetManager.getInstance().updateWeekly(11);
		
		PremiumCoveredCallSpreadsheetManager.getInstance().updateDataInSpreadsheet();
	}

}
