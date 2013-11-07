package com.innoplus.javaapitables.data.marketindustries;

import com.innoplus.javaapitables.domain.MarketIndustry;

public abstract interface MarketIndustryDataScraper
{

  public abstract MarketIndustry scrapeMarketIndustry(String etfSymbol, String url);
}