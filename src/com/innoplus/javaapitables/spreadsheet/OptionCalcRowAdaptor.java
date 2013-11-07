package com.innoplus.javaapitables.spreadsheet;

import com.google.gdata.util.ServiceException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.innoplus.javaapitables.domain.Option;

public abstract interface OptionCalcRowAdaptor
{
  public abstract List<String> getStockTickers()
    throws IOException, ServiceException;

  public abstract Option getOption(String stockTicker, int rowIndex);

  public abstract void writeSpreadsheetRow(int rowIndex, String stockTicker, String scrappedStockTicker, Date expireDate, Option scrappedOption, String earningsSurprises, String epsGrowthThisYear, String epsGrowthNextYear, String dividend, String dividendDate)
    throws IOException, ServiceException;

  public abstract boolean isDividendValueOverrideOn(String stockTicker, int paramInt);

    public HashMap<String, String> getEarningsSurprisesMap(String stockTicker);

    public Date getDividendDate(String stockTicker, int rowIndex);

    public Double getDividendValue(String stockTicker, int rowIndex);

    public String getOptionDateSymbolValue(String stockTicker, int rowIndex);
    public String getOptionTickeValue(String stockTicker, int rowIndex);
    
    public HashMap<String, String> getNewDividendMap(String stockTicker);
}