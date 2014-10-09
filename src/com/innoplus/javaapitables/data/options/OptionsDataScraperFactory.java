package com.innoplus.javaapitables.data.options;

public class OptionsDataScraperFactory
{
  public static OptionsDataScraper getOptionsDataScraper()
  {
    MSNOptionDataScraper localMSNOptionDataScraper = new NASDAQOptionDataScraper();
    return localMSNOptionDataScraper;
  }
}