package com.innoplus.javaapitables.data.options;

import java.util.List;
import java.util.Map;
import com.innoplus.javaapitables.data.options.dto.OptionExpDateDTO;
import com.innoplus.javaapitables.domain.Option;
import java.util.HashMap;

public abstract interface OptionsDataScraper
{
  public abstract Map<String, List<OptionExpDateDTO>> scrapeOptionsDataForStockTickers(List<String> paramList);

  public abstract Option scrapeOptionData(String paramString);

  public abstract List<String> scrapeOptionExpDates(String paramString);

  public abstract OptionExpDateDTO scrapeOptionData(String paramString1, String paramString2);

  public abstract HashMap<String, String> scrapEarningsSurprises (String optionSymbol);

    public HashMap<String, String> scrapLastDividend(String stockTicker);
}