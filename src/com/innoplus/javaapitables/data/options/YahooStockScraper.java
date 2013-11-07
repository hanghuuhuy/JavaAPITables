package com.innoplus.javaapitables.data.options;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class YahooStockScraper {

	private static Logger logger = Logger.getLogger(YahooStockScraper.class);

	public YahooStockScraper() throws IOException, GeneralSecurityException {

	}

	/**
	 * @param args
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException,
			GeneralSecurityException {
		// TODO Auto-generated method stub
		YahooStockScraper scraper = new YahooStockScraper();
		StockScreener stockScreener = scraper.scrap("Q");
	}

	public StockScreener scrap(String ticker) {

		logger.info("Scrape for ticker=" + ticker);
		StockScreener stockScreener = new StockScreener();
		try {
			String url = String.format("http://finance.yahoo.com/q/ae?s=%s&ql=1", ticker);

			String columnF = "";
			String columnG = "";
			String columnJ = "";
			String columnD = "";

			Document doc = Jsoup.connect(url).get();

			Elements trs = doc.getElementsByTag("tr");
			for (Element tr : trs) {

				String text = tr.text();

				if ("".equals(columnF) && text.startsWith("Year Ago EPS")) {
					columnF = text.split(" ")[5];
				}

				else if ("".equals(columnG) && "".equals(columnJ)
						&& text.startsWith("Avg. Estimate")) {
					columnG = text.split(" ")[4];
					columnJ = text.split(" ")[5];
				}

				else if ("".equals(columnD) && text.startsWith("EPS Actual")) {
					String texts[] = text.replace("EPS Actual", "").trim().split(" ");

					BigDecimal EPSActual1 = new BigDecimal("0");
					BigDecimal EPSActual2 = new BigDecimal("0");
					BigDecimal EPSActual3 = new BigDecimal("0");
					BigDecimal EPSActual4 = new BigDecimal("0");

					try {
						EPSActual1 = new BigDecimal(texts[0]);
					} catch (Exception e) {

					}

					try {
						EPSActual2 = new BigDecimal(texts[1]);
					} catch (Exception e) {

					}

					try {
						EPSActual3 = new BigDecimal(texts[2]);
					} catch (Exception e) {

					}

					try {
						EPSActual4 = new BigDecimal(texts[3]);
					} catch (Exception e) {

					}
					
					columnD = EPSActual1.add(EPSActual2).add(EPSActual3)
							.add(EPSActual4).toString();

				}
			}
			
			columnF = valueOf(columnF);
			columnG = valueOf(columnG);
			columnJ = valueOf(columnJ);
			columnD = valueOf(columnD);
			
			logger.info("columnF=" + columnF);
			logger.info("columnG=" + columnG);
			logger.info("columnJ=" + columnJ);
			logger.info("columnD=" + columnD);

			stockScreener.setTicker(ticker);
			stockScreener.setColumnF("$".concat(columnF));
			stockScreener.setColumnG("$".concat(columnG));
			stockScreener.setColumnJ("$".concat(columnJ));
			stockScreener.setColumnD("$".concat(columnD));

		} catch (Exception e) {
			logger.error(
					"Error while scraping Yahoo Analysts Estimates ticker ["
							+ ticker + "]", e);
		}

		try {

			logger.info("Scrape for Insider Transactions");

			String url = String.format(
					"http://finance.yahoo.com/q/it?s=%s+Insider+Transactions",
					ticker);
			// TODO Auto-generated method stub
			Document doc1 = Jsoup.connect(url).get();
			String columnN = "";
			String columnO = "";

			Elements trs1 = doc1.getElementsByTag("tr");
			for (Element tr : trs1) {

				String text = tr.text();

				if ("".equals(columnN)
						&& text.startsWith("% Net Shares Purchased (Sold)")) {
					columnN = text.replace("% Net Shares Purchased (Sold)", "")
							.trim().split(" ")[0].trim();
					

					if (columnN.contains("(") && columnN.contains(")")) {
						columnN = "-".concat(columnN.replace(")", "").replace("(", ""));
					}
				}

				else if ("".equals(columnO)
						&& text.startsWith("% Change in Institutional Shares Held")) {
					columnO = text.replace(
							"% Change in Institutional Shares Held", "").trim();
					
					if (columnO.contains("(") && columnO.contains(")")) {
						columnO = "-".concat(columnO.replace(")", "").replace("(", ""));
					}
				}
			}

			columnN = valueOfPercent(columnN);
			columnO = valueOfPercent(columnO);
			
			logger.info("columnN=" + columnN);
			logger.info("columnO=" + columnO);

			stockScreener.setColumnN(columnN);
			stockScreener.setColumnO(columnO);
		} catch (Exception e) {
			logger.error(
					"Error while scraping Yahoo Analysts Estimates ticker ["
							+ ticker + "]", e);
		}

		return stockScreener;
	}
	
	private String valueOf(String value) {
		if ("N/A".equals(value) || "".equals(value) || value == null) {
			return "0.00";
		}
		
		return value;
	}
	
	private String valueOfPercent(String value) {
		if ("N/A".equals(value) || "".equals(value) || value == null) {
			return "0.0%";
		}
		
		return value;
	}
}
