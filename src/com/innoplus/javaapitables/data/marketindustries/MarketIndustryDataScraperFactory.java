package com.innoplus.javaapitables.data.marketindustries;

public class MarketIndustryDataScraperFactory
{
  public static MarketIndustryDataScraper getMarketIndustryDataScraper()
  {
    GoogleMarketIndustryDataScraper localGoogleindustryDataScraper = new GoogleMarketIndustryDataScraper();
    return localGoogleindustryDataScraper;
  }
}