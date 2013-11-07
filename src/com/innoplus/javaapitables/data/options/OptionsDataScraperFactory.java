package com.innoplus.javaapitables.data.options;

public class OptionsDataScraperFactory
{
  public static OptionsDataScraper getOptionsDataScraper()
  {
    MSNOptionDataScraper localMSNOptionDataScraper = new MSNOptionDataScraper();
    return localMSNOptionDataScraper;
  }
}