package com.innoplus.javaapitables.data.marketindustries;

import com.innoplus.javaapitables.data.BaseSecurityDataProvider;
import com.innoplus.javaapitables.domain.MarketIndustry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MarketIndustryDataProvider extends BaseSecurityDataProvider {

    private static Log log = LogFactory.getLog(MarketIndustryDataProvider.class);

    public static MarketIndustry getMarketIndustryByUrl(String etfSymbol, String url) {
        etfSymbol = normalizeTicker(etfSymbol);

        Object localObject1 = getSyncLockForTicker(etfSymbol);

        MarketIndustryDataScraper marketIndustryDataScraper = MarketIndustryDataScraperFactory.getMarketIndustryDataScraper();

        MarketIndustry marketIndustry = marketIndustryDataScraper.scrapeMarketIndustry(etfSymbol, url);

        return marketIndustry;
    }
}
