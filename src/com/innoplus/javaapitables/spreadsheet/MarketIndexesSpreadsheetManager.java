package com.innoplus.javaapitables.spreadsheet;

import com.innoplus.javaapitables.core.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MarketIndexesSpreadsheetManager extends LastWeekPriceUpdateSpreadsheetManager {

    private static Log log = LogFactory.getLog(MarketIndexesSpreadsheetManager.class);

    public static LastWeekPriceUpdateSpreadsheetManager getInstance() {
        MarketIndexesSpreadsheetManager localMarketIndexesSpreadsheetManager = new MarketIndexesSpreadsheetManager();
        return localMarketIndexesSpreadsheetManager;
    }

    protected String getSpreadsheetName() {
        return Configuration.getInstance().getProperty("spreadsheets.marketindexes.spreadsheet.name");
    }

    protected void initializeSettings() {
        this.ETF_SYMBOL_COLUMN = Configuration.getInstance().getProperty("spreadsheets.marketindexes.columns.stock_symbol");
        this.OPENING_MONTH_PRICE_COLUMN = Configuration.getInstance().getProperty("spreadsheets.marketindexes.columns.opening_month_price");
        this.LAST_WEEK_PRICE_COLUMN = Configuration.getInstance().getProperty("spreadsheets.marketindexes.columns.last_week_price");
        this.URL_COLUMN = Configuration.getInstance().getProperty("spreadsheets.marketindexes.columns.url");
        this.CURRENT_PRICE_COLUMN = Configuration.getInstance().getProperty("spreadsheets.marketindexes.columns.current_price");
        this.FIRST_ROW = Integer.parseInt(Configuration.getInstance().getProperty("spreadsheets.marketindexes.rows.first"));
        this.LAST_ROW = Integer.parseInt(Configuration.getInstance().getProperty("spreadsheets.marketindexes.rows.last"));
    }
}
