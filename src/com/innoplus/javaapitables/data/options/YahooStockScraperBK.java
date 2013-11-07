package com.innoplus.javaapitables.data.options;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.util.List;

import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;

public class YahooStockScraperBK {

	private static Logger logger = Logger.getLogger(YahooStockScraperBK.class);

	public YahooStockScraperBK() throws IOException, GeneralSecurityException {

	}

	/**
	 * @param args
	 * @throws GeneralSecurityException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, GeneralSecurityException {
		// TODO Auto-generated method stub
		YahooStockScraperBK scraper = new YahooStockScraperBK();
		StockScreener stockScreener = scraper.scrap("UTX");
	}

	public StockScreener scrap(String ticker) {
		
		logger.info("Scrape for ticker=" + ticker);
		StockScreener stockScreener = new StockScreener();
		WebClient client = null;
		try {
		
			client = new WebClient(BrowserVersion.FIREFOX_3_6);
			client.setUseInsecureSSL(true);
			client.setCssEnabled(false);
			client.setTimeout(80000);
			client.setThrowExceptionOnScriptError(false);
			client.setThrowExceptionOnFailingStatusCode(false);			
			client.setJavaScriptEnabled(false);
			LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
			Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
			Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
			
			logger.info("Scrape for Yahoo Analysts Estimates");
			
			String url = String.format("http://finance.yahoo.com/q/ae?s=%s&ql=1", ticker);
			HtmlPage page = client.getPage(url);
			
			String columnF = "";
			String columnG = "";
			String columnJ = "";
			String columnD = "";
			///html/body/div[4]/div[4]/table[2]/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[2]/td[5]
			HtmlElement columnFe = page.getFirstByXPath("/html/body/div[4]/div[4]/table[2]/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[6]/td[4]");
			if (columnFe != null) {
				columnF = columnFe.asText();
			}
			
			HtmlElement columnGe = page.getFirstByXPath("/html/body/div[4]/div[4]/table[2]/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[2]/td[4]");
			if (columnGe != null) {
				columnG = columnGe.asText();
			}
			
			HtmlElement columnJe = page.getFirstByXPath("/html/body/div[4]/div[4]/table[2]/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[2]/td[5]");
			if (columnJe != null) {
				columnJ = columnJe.asText();
			}

			List<HtmlTableCell> cells = (List<HtmlTableCell>) page.getByXPath("/html/body/div[4]/div[4]/table[2]/tbody/tr[2]/td/table[5]/tbody/tr/td/table/tbody/tr[3]/td");
			if (cells.size() == 0) {
				cells = (List<HtmlTableCell>) page.getByXPath("/html/body/div[4]/div[4]/table[2]/tbody/tr[2]/td/table[6]/tbody/tr/td/table/tbody/tr[3]/td");
			}
			
			HtmlElement cell1 = cells.get(1);
			HtmlElement cell2 = cells.get(2);
			HtmlElement cell3 = cells.get(3);
			HtmlElement cell4 = cells.get(4);
			
			BigDecimal EPSActual1 = new BigDecimal("0");
			BigDecimal EPSActual2 = new BigDecimal("0");
			BigDecimal EPSActual3 = new BigDecimal("0");
			BigDecimal EPSActual4 = new BigDecimal("0");

			try {
				EPSActual1 = new BigDecimal(cell1.asText().trim());
			} catch (Exception e) {
				
			}
			
			try {
				EPSActual2 = new BigDecimal(cell2.asText().trim());
			} catch (Exception e) {
				
			}

			try {
				EPSActual3 = new BigDecimal(cell3.asText().trim());
			} catch (Exception e) {
				
			}
			
			try {
				EPSActual4 = new BigDecimal(cell4.asText().trim());
			} catch (Exception e) {
				
			}
			columnD = EPSActual1.add(EPSActual2).add(EPSActual3).add(EPSActual4).toString();

			logger.info("columnF=" + columnF);
			logger.info("columnG=" + columnG);
			logger.info("columnJ=" + columnJ);
			logger.info("columnD=" + columnD);
			
			stockScreener.setTicker(ticker);
			stockScreener.setColumnF("$".concat(columnF));
			stockScreener.setColumnG("$".concat(columnG));
			stockScreener.setColumnJ("$".concat(columnJ));
			stockScreener.setColumnD("$".concat(columnD));
			
			page.cleanUp();
		} catch (Exception e) {
			logger.error("Error while scraping Yahoo Analysts Estimates ticker [" + ticker + "]", e);
		}
		
		try {
			
			logger.info("Scrape for Insider Transactions");
			
			String url = String.format("http://finance.yahoo.com/q/it?s=%s+Insider+Transactions", ticker);
			HtmlPage page = client.getPage(url);
			
			String columnN = "";
			String columnO = "";
			
			HtmlElement columnNe = page.getFirstByXPath("/html/body/div[4]/div[4]/table[2]/tbody/tr[2]/td/table[2]/tbody/tr/td/table/tbody/tr[7]/td[2]");
			if (columnNe != null) {
				columnN = columnNe.asText().replace(")", "").replace("(", "");
			}
			
			HtmlElement columnOe = page.getFirstByXPath("/html/body/div[4]/div[4]/table[2]/tbody/tr[2]/td/table[3]/tbody/tr/td/table/tbody/tr[4]/td[2]");
			if (columnOe != null) {
				columnO = columnOe.asText().replace(")", "").replace("(", "");
			}

			logger.info("columnN=" + columnN);
			logger.info("columnO=" + columnO);
			
			stockScreener.setColumnN(columnN);
			stockScreener.setColumnO("-".concat(columnO));
			page.cleanUp();
		} catch (Exception e) {
			logger.error("Error while scraping Yahoo Analysts Estimates ticker [" + ticker + "]", e);
		}
		
		return stockScreener;
	}
}
