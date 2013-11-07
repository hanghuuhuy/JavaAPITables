package com.innoplus.javaapitables.spreadsheet;

import com.google.gdata.util.ServiceException;
import com.innoplus.javaapitables.domain.MarketIndustry;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Cvitan
 */
public abstract interface MarketIndustryRowAdaptor {

    public abstract List<String> getEtfSymbols()
    throws IOException, ServiceException;

    public abstract MarketIndustry getMarketIndustry(String etfSymbol, int rowIndex);

    public abstract void writeSpreadsheetRow(int rowIndex, String etfSymbol, MarketIndustry scrappedMarketIndustry)
    throws IOException, ServiceException;

}
