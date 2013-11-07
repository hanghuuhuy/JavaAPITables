package com.innoplus.javaapitables.data.options;

import java.io.IOException;
import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.innoplus.javaapitables.spreadsheet.BaseSpreadsheetManager;

public class Test {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		String columnF = "";
		String columnG = "";
		String columnJ = "";
		String columnD = "";
		
		String url = "http://finance.yahoo.com/q/ae?s=UTX&ql=1";
	
		Document doc = Jsoup.connect(url ).get();

		Elements trs = doc.getElementsByTag("tr");
		for (Element tr : trs) {

		  String text = tr.text();
		  
		  if ("".equals(columnF) && text.startsWith("Year Ago EPS")) {
			  columnF = text.split(" ")[5];
		  }
		  
		  else if ("".equals(columnG) && "".equals(columnJ) && text.startsWith("Avg. Estimate")) {
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

				System.out.println("columnF=" + columnF);
				System.out.println("columnG=" + columnG);
				System.out.println("columnJ=" + columnJ);
				System.out.println("columnD=" + columnD);
			  
		  }
		}
		  
		String url1 = "http://finance.yahoo.com/q/it?s=UTX+Insider+Transactions";
		// TODO Auto-generated method stub
		Document doc1 = Jsoup.connect(url1).get();
		String columnN = "";
		String columnO = "";
		
		Elements trs1 = doc1.getElementsByTag("tr");
		for (Element tr : trs1) {

		  String text = tr.text();
		  
		  if ("".equals(columnN) && text.startsWith("% Net Shares Purchased (Sold)")) {
			  columnN = text.replace("% Net Shares Purchased (Sold)", "").trim().split(" ")[0].trim();
			  columnN = columnN.replace(")", "").replace("(", "");
		  }
		  
		  else if ("".equals(columnO) &&  text.startsWith("% Change in Institutional Shares Held")) {
			  columnO = text.replace("% Change in Institutional Shares Held", "").trim();
			  columnO = columnO.replace(")", "").replace("(", "");
		  }
		}
		
	}
	
	

}
