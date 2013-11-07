package com.innoplus.javaapitables.data.marketindustries;

import com.innoplus.javaapitables.domain.MarketIndustry;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Level;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.variables.Variable;
import org.xml.sax.InputSource;

public class GoogleMarketIndustryDataScraper
        implements MarketIndustryDataScraper {

    private static Log log = LogFactory.getLog(GoogleMarketIndustryDataScraper.class);
    Map<String, String> monthMap = new HashMap() {
    };

    public MarketIndustry scrapeMarketIndustry(String etfSymbol, String url) {
        MarketIndustry marketIndustry = null;

        String weekUrl = url + "&histperiod=weekly";
        Date currentDate = new Date();
        SimpleDateFormat sdfMonth = new SimpleDateFormat("MMM", Locale.ENGLISH);
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        String monthUrl = url + "&startdate=" + sdfMonth.format(currentDate) + "+1,+" + sdfYear.format(currentDate);

        try {
            // CVIT
            //ScraperConfiguration localScraperConfiguration = new ScraperConfiguration(ClassLoader.getSystemClassLoader().getResource("msn_single_option.xml"));
            //ScraperConfiguration localScraperConfiguration = new ScraperConfiguration("D:/web-harvest/msn_single_option.xml");
            ScraperConfiguration localScraperConfiguration = new ScraperConfiguration(new InputSource(GoogleMarketIndustryDataScraper.class.getResourceAsStream("/com/innoplus/javaapitables/resources/google_market_industry.xml")));

            Scraper localScraper = new Scraper(localScraperConfiguration, ".");
            localScraper.getLogger().setLevel(Level.WARN);
            localScraper.addVariableToContext("month_url", monthUrl);
            localScraper.addVariableToContext("week_url", weekUrl);
            localScraper.execute();
            Variable scrappedCurrentMonthOpeningPrice = (Variable) localScraper.getContext().get("curr_month_opening_price");
            Variable scrappedCurrentMonthOpeningDate = (Variable) localScraper.getContext().get("curr_month_opening_date");
            Variable scrappedPrevWeekClosePrice = (Variable) localScraper.getContext().get("prev_week_close_price");
            Variable scrappedPrevWeekCloseDate = (Variable) localScraper.getContext().get("prev_week_close_date");

            log.info("Scrapped CurrentMonthOpeningPrice for etfSymbol [" + etfSymbol + "]: " + scrappedCurrentMonthOpeningPrice.toString());
            log.info("Scrapped CurrentMonthOpeningDate for etfSymbol [" + etfSymbol + "]: " + scrappedCurrentMonthOpeningDate.toString());
            log.info("Scrapped PrevWeekClosePrice for etfSymbol [" + etfSymbol + "]: " + scrappedPrevWeekClosePrice.toString());
            log.info("Scrapped PrevWeekCloseDate for etfSymbol [" + etfSymbol + "]: " + scrappedPrevWeekCloseDate.toString());


            marketIndustry = createMarketIndustry(scrappedCurrentMonthOpeningPrice.toString(), scrappedPrevWeekClosePrice.toString());

        } catch (Exception localException) {
            log.error("Error while scraping for option ticker [" + etfSymbol + "]", localException);
        }
        return marketIndustry;
    }

    private MarketIndustry createMarketIndustry(String currentMonthOpeningPriceParam, String prevWeekClosePriceParam) {
        currentMonthOpeningPriceParam = currentMonthOpeningPriceParam.replace(",", "");
        prevWeekClosePriceParam = prevWeekClosePriceParam.replace(",", "");
        Double currentMonthOpeningPrice = Double.valueOf(Double.parseDouble(currentMonthOpeningPriceParam));
        Double prevWeekClosePrice = Double.valueOf(Double.parseDouble(prevWeekClosePriceParam));

        return new MarketIndustry(currentMonthOpeningPrice, prevWeekClosePrice);
    }
}
