package com.innoplus.javaapitables.spreadsheet;

import com.innoplus.javaapitables.core.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IndustrySectorsSpreadsheetManager extends LastWeekPriceUpdateSpreadsheetManager {

    private static Log log = LogFactory.getLog(IndustrySectorsSpreadsheetManager.class);

    public static LastWeekPriceUpdateSpreadsheetManager getInstance() {
        IndustrySectorsSpreadsheetManager localIndustrySectorsSpreadsheetManager = new IndustrySectorsSpreadsheetManager();
        return localIndustrySectorsSpreadsheetManager;
    }

    protected String getSpreadsheetName() {
        return Configuration.getInstance().getProperty("spreadsheets.industrysectors.spreadsheet.name");
    }

    protected void initializeSettings() {
        this.ETF_SYMBOL_COLUMN = Configuration.getInstance().getProperty("spreadsheets.industrysectors.columns.stock_symbol");
        this.OPENING_MONTH_PRICE_COLUMN = Configuration.getInstance().getProperty("spreadsheets.industrysectors.columns.opening_month_price");
        this.LAST_WEEK_PRICE_COLUMN = Configuration.getInstance().getProperty("spreadsheets.industrysectors.columns.last_week_price");
        this.URL_COLUMN = Configuration.getInstance().getProperty("spreadsheets.industrysectors.columns.url");
        this.CURRENT_PRICE_COLUMN = Configuration.getInstance().getProperty("spreadsheets.industrysectors.columns.current_price");
        this.FIRST_ROW = Integer.parseInt(Configuration.getInstance().getProperty("spreadsheets.industrysectors.rows.first"));
        this.LAST_ROW = Integer.parseInt(Configuration.getInstance().getProperty("spreadsheets.industrysectors.rows.last"));
    }
}
