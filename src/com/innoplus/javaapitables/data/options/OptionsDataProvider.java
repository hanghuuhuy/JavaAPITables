package com.innoplus.javaapitables.data.options;

import java.util.HashMap;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.innoplus.javaapitables.data.BaseSecurityDataProvider;
import com.innoplus.javaapitables.data.RetrieveMode;
import com.innoplus.javaapitables.domain.Option;

public class OptionsDataProvider extends BaseSecurityDataProvider {

    private static Log log = LogFactory.getLog(OptionsDataProvider.class);

    /*
    public static OptionExpDateDTO getAllOptionsForNthExpMonthOut(String paramString, int paramInt) {
        log.info("Looking for [" + paramInt + "]'th options expiration month for stock [" + paramString + "]");
        OptionsDataScraper localOptionsDataScraper = OptionsDataScraperFactory.getOptionsDataScraper();
        List localList = localOptionsDataScraper.scrapeOptionExpDates(paramString);
        log.info("found optExpDates: " + localList);
        if ((localList == null) || (localList.size() < paramInt)) {
            log.info(paramInt + "'th opt exp month not available");
            return null;
        }
        log.info("using month [" + (String) localList.get(paramInt - 1) + "]");

        return localOptionsDataScraper.scrapeOptionData(paramString, (String) localList.get(paramInt - 1));
    }

    public static OptionExpDateDTO getAllOptionsForGivenExpMonthOrFront(String paramString1, String paramString2) {
        log.info("Looking for [" + paramString2 + "] options expiration month for stock [" + paramString1 + "]");
        OptionsDataScraper localOptionsDataScraper = OptionsDataScraperFactory.getOptionsDataScraper();
        List localList = localOptionsDataScraper.scrapeOptionExpDates(paramString1);
        log.info("found optExpDates: " + localList);
        int i = 0;
        if ((localList == null) || (localList.isEmpty())) {
            log.info("opt exp month [" + paramString2 + "] or front exp month not available");
            return null;
        }
        if (!localList.contains(paramString2)) {
            log.info("opt exp month [" + paramString2 + "] not available");
            log.info("using month [" + (String) localList.get(0) + "]");
        } else {
            i = localList.indexOf(paramString2);
        }
        return localOptionsDataScraper.scrapeOptionData(paramString1, (String) localList.get(i));
    }

    public static OptionExpDateDTO getAllOptionsForGivenExpMonthOrClosest(String paramString1, String paramString2) {
        log.info("Looking for [" + paramString2 + "] options expiration month for stock [" + paramString1 + "]");
        OptionsDataScraper localOptionsDataScraper = OptionsDataScraperFactory.getOptionsDataScraper();
        List localList = localOptionsDataScraper.scrapeOptionExpDates(paramString1);
        log.info("found optExpDates: " + localList);
        if ((localList == null) || (localList.isEmpty())) {
            log.info("opt exp month [" + paramString2 + "] or closest exp month not available");
            return null;
        }
        String str = ClosestMonthAlgo.findClosestMonthYearAmongGiven(paramString2, localList);
        log.info("using month [" + str + "]");
        return localOptionsDataScraper.scrapeOptionData(paramString1, str);
    }
     *
     */

    public static Option getOptionByTicker(String stockTicker, String optionSymbol, RetrieveMode paramRetrieveMode) {
        stockTicker = normalizeTicker(stockTicker);
        optionSymbol = normalizeTicker(optionSymbol);

        Object localObject1 = getSyncLockForTicker(optionSymbol);
        synchronized (localObject1) {
            if (RetrieveMode.DATABASE_THEN_WEB.equals(paramRetrieveMode)) {
                // TODO vidi ova
                // CVIT return getOptionByTickerFromDbThenWeb(paramString1, paramString2);
                return null;
            }
            if (RetrieveMode.WEB_UPDATE_DATABASE.equals(paramRetrieveMode)) {
                return getOptionByTickerFromWebUpdateDb(stockTicker, optionSymbol);
            }
            throw new IllegalArgumentException(RetrieveMode.class.getName() + "[" + paramRetrieveMode.name() + "] is not supported!");
        }
    }

    /*
    protected static Option getOptionByTickerFromDbThenWeb(String paramString1, String paramString2) {
        Option localOption = null;

        paramString2 = normalizeTicker(paramString2);

        OptionDao localOptionDao = OptionDao.getInstance();

        localOption = localOptionDao.findOptionByTicker(paramString2);
        if (localOption != null) {
            return localOption;
        }

        OptionsDataScraper localOptionsDataScraper = OptionsDataScraperFactory.getOptionsDataScraper();
        localOption = localOptionsDataScraper.scrapeOptionData(paramString2);

        if (localOption == null) {
            log.error("Unable to scrape option for option ticker [" + paramString2 + "]");
            return null;
        }

        localOptionDao.saveOption(paramString1, localOption);

        return localOption;
    }
     * 
     */

    protected static Option getOptionByTickerFromWebUpdateDb(String stockTicker, String optionSymbol) {
        OptionsDataScraper optionsDataScraper = OptionsDataScraperFactory.getOptionsDataScraper();

        Option localOption = optionsDataScraper.scrapeOptionData(optionSymbol);

        // TODO Ova pravi proverka vo baza
        /* CVIT
        OptionDao localOptionDao = OptionDao.getInstance();
        if (localOption == null) {
            localOption = localOptionDao.findOptionByTicker(paramString2);
            if (localOption == null) {
                log.warn("Data for option [" + paramString2 + "] (stock [" + stockTicker + "]) cannot be found on Web (scraped) or in DB!");
                return null;
            }
            return localOption;
        }

        localOptionDao.createOrUpdateOption(stockTicker, localOption);
         * 
         */

        return localOption;
    }

    public static HashMap<String, String> getEarningsSurprisesByTicker(String stockTicker) {
        OptionsDataScraper optionsDataScraper = OptionsDataScraperFactory.getOptionsDataScraper();

        HashMap<String, String> earningsSurprisesMap = optionsDataScraper.scrapEarningsSurprises(stockTicker);

        return earningsSurprisesMap;
    }

    public static HashMap<String, String> getLastDividendByTicker(String stockTicker) {
        OptionsDataScraper optionsDataScraper = OptionsDataScraperFactory.getOptionsDataScraper();

        HashMap<String, String> lastDividendMap = optionsDataScraper.scrapLastDividend(stockTicker);

        return lastDividendMap;
    }
}
