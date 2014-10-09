package com.innoplus.javaapitables.data.options;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.variables.Variable;
import org.xml.sax.InputSource;

import com.innoplus.javaapitables.domain.Option;

public class NASDAQOptionDataScraper extends MSNOptionDataScraper {

	private static Log log = LogFactory.getLog(NASDAQOptionDataScraper.class);

	private static SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyMM");
	private static SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMMyyyy");

	@Override
	public Option scrapeOptionData(String optionSymbol) {
		return createOption(optionSymbol);
	}

	private Option createOption(String inputString) {
		Option localOption = new Option();

		inputString = inputString.trim();

		// Begin get code
		String url = null;
		String code = null;
		int indexOfLastSlash = inputString.lastIndexOf("/");
		if (indexOfLastSlash >= 0) {
			url = inputString;
			code = inputString.substring(indexOfLastSlash + "/".length()).trim();
		} else {
			code = inputString.trim();

			// Begin rebuild url from input string
			// Begin create stock symbol
			String stockSymbol = code;
			int indexOfDash = stockSymbol.indexOf("-");
			if (indexOfDash >= 0) {
				stockSymbol = stockSymbol.substring(indexOfDash + "-".length());
				indexOfDash = stockSymbol.indexOf("-");
				if (indexOfDash >= 0) {
					stockSymbol = stockSymbol.substring(0, indexOfDash);
					url = "http://www.nasdaq.com/symbol/" + stockSymbol + "/option-chain/" + code;
				}
			}
			// End create stock symbol

			// Begin rebuild url from input string
		}
		// End get code

		// Begin get and set Expiration Date
		try {
			String expirationDateString = code.substring(0, 4);
			expirationDateString = outputDateFormat.format(inputDateFormat.parse(expirationDateString));
			localOption.setExpMonthYear(expirationDateString);
		} catch (Exception e) {
			// Do nothing
		}
		// End get and set Expiration Date

		// Begin get and set Put Strike Price
		try {
			String strikePriceString = code.substring(7, 15);
			double strikePrice = Double.parseDouble(strikePriceString) / 1000;
			localOption.setStrikePrice(strikePrice);
		} catch (Exception e) {
			// Do nothing
		}
		// End get and set Put Strike Price

		// Begin scrape and set price bid value
		try {
			// Begin remove double prefix http:// from url
			url = url.toLowerCase();
			while (true) {
				if (url.startsWith("http://http://")) {
					url = url.substring("http://".length());
				} else if (url.startsWith("http://https://")) {
					url = url.substring("http://".length());
				} else if (url.startsWith("https://http://")) {
					url = url.substring("https://".length());
				} else if (url.startsWith("https://https://")) {
					url = url.substring("https://".length());
				} else {
					break;
				}
			}
			// End remove double prefix http:// from url

			// Begin add prefix http:// if there ara not
			if (!url.startsWith("http://") && !url.startsWith("https://")) {
				url = "http://" + url;
			}
			// End add prefix http:// if there ara not

			ScraperConfiguration localScraperConfiguration = new ScraperConfiguration(new InputSource(NASDAQOptionDataScraper.class.getResourceAsStream("/com/innoplus/javaapitables/resources/nasdaq_single_option.xml")));

			Scraper localScraper = new Scraper(localScraperConfiguration, ".");
			localScraper.getLogger().setLevel(Level.WARN);
			localScraper.addVariableToContext("startUrl", url);
			localScraper.execute();

			Variable scrappedBid = (Variable) localScraper.getContext().get("bid");

			localOption.setPriceBid(new BigDecimal(scrappedBid.toString()));
		} catch (Exception e) {
			// Do nothing
		}
		// End scrape and set price bid value
		System.out.println(localOption);
		return localOption;
	}

	public static void main(String[] args) {
		String url = "http://www.nasdaq.com/symbol/apl/option-chain/141122P00036000-apl-put";
		//String url = "141122P00036000-apl-put";
		NASDAQOptionDataScraper nASDAQOptionDataScraper = new NASDAQOptionDataScraper();
		Option option = nASDAQOptionDataScraper.createOption(url);
		System.out.println(option);
	}
}
