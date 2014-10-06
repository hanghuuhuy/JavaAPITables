package com.innoplus.javaapitables.data.options;

import java.math.BigDecimal;

import org.apache.log4j.Level;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.variables.Variable;
import org.xml.sax.InputSource;

import com.innoplus.javaapitables.domain.Option;

public class NASDAQOptionDataScraper {

	protected Option createOption(String url) {
		Option localOption = new Option();

		try {
			ScraperConfiguration localScraperConfiguration = new ScraperConfiguration(new InputSource(NASDAQOptionDataScraper.class.getResourceAsStream("/com/innoplus/javaapitables/resources/nasdaq_single_option.xml")));

			Scraper localScraper = new Scraper(localScraperConfiguration, ".");
			localScraper.getLogger().setLevel(Level.WARN);
			localScraper.addVariableToContext("startUrl", url);
			localScraper.execute();

			Variable scrappedBid = (Variable) localScraper.getContext().get("bid");
			Variable scrappedAsk = (Variable) localScraper.getContext().get("ask");

			try {
				localOption.setPriceBid(new BigDecimal(scrappedBid.toString()));
			} catch (Exception localException) {
				// Do nothing
			}

			try {
				localOption.setPriceAsk(new BigDecimal(scrappedAsk.toString()));
			} catch (Exception localException) {
				// Do nothing
			}
		} catch (Exception localException) {
			// Do nothing
		}

		return localOption;
	}

	public static void main(String[] args) {
		String url = "http://www.nasdaq.com/symbol/apl/option-chain/141122P00036000-apl-put";
		NASDAQOptionDataScraper nASDAQOptionDataScraper = new NASDAQOptionDataScraper();
		Option option = nASDAQOptionDataScraper.createOption(url);
		System.out.println(option);
	}
}
