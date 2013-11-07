package com.innoplus.javaapitables.spreadsheet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.innoplus.javaapitables.core.Configuration;

public class PremiumCoveredPutSpreadsheetManager extends BaseOptionSpreadsheetManager {

    private static Log log = LogFactory.getLog(PremiumCoveredPutSpreadsheetManager.class);

    public static PremiumCoveredPutSpreadsheetManager getInstance() {
        PremiumCoveredPutSpreadsheetManager localPutSellingCalcSpreadsheetManager = new PremiumCoveredPutSpreadsheetManager();
        return localPutSellingCalcSpreadsheetManager;
    }

    protected String getSpreadsheetName() {
        return Configuration.getInstance().getProperty("spreadsheets.putsellcalc.spreadsheet.name");
    }

    protected void initializeSettings() {
        this.STOCK_TICKER_COLUMN = Configuration.getInstance().getProperty("spreadsheets.putsellcalc.columns.stock_ticker");
        this.OPTION_TICKER_COLUMN = Configuration.getInstance().getProperty("spreadsheets.putsellcalc.columns.option_ticker");
        this.OPTION_DATE_SYMBOL_COLUMN = Configuration.getInstance().getProperty("spreadsheets.putsellcalc.columns.option_date_symbol_column");
        this.OPTION_STRIKE_COLUMN = Configuration.getInstance().getProperty("spreadsheets.putsellcalc.columns.option_strike_column");
        this.OPTION_BID_AMT_COLUMN = Configuration.getInstance().getProperty("spreadsheets.putsellcalc.columns.option_bid_amt_column");
        this.OPTION_EXP_DATE_COLUMN = Configuration.getInstance().getProperty("spreadsheets.putsellcalc.columns.option_exp_date_column");
        this.DAY_UNTIL_OPT_EXP_COLUMN = Configuration.getInstance().getProperty("spreadsheets.putsellcalc.columns.day_until_opt_exp_column");
        this.DIVIDEND_VALUE_OVERRIDE_COLUMN = Configuration.getInstance().getProperty("spreadsheets.putsellcalc.columns.div_value_override");
        this.EARNINGS_SURPRISES_COLUMN = Configuration.getInstance().getProperty("spreadsheets.putsellcalc.columns.earnings_surprises");
        this.PAST_12_MNTHS_EPS_GROWTH_COLUMN = Configuration.getInstance().getProperty("spreadsheets.putsellcalc.columns.past_12_months_eps_growth");
        this.NEXT_12_MONTHS_EPS_GROWTH_COLUMN = Configuration.getInstance().getProperty("spreadsheets.putsellcalc.columns.next_12_months_eps_growth");
        this.DIVIDEND_DATE_COLUMN = Configuration.getInstance().getProperty("spreadsheets.putsellcalc.columns.dividend_date");
        this.DIVIDEND_COLUMN = Configuration.getInstance().getProperty("spreadsheets.putsellcalc.columns.dividend");
        this.FIRST_ROW = Integer.parseInt(Configuration.getInstance().getProperty("spreadsheets.putsellcalc.rows.first"));
        this.LAST_ROW = Integer.parseInt(Configuration.getInstance().getProperty("spreadsheets.putsellcalc.rows.last"));
    }
}
