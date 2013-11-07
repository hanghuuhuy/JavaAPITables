package com.innoplus.javaapitables.jobs.datarefresh;

import com.innoplus.javaapitables.spreadsheet.IndustrySectorsSpreadsheetManager;
import com.innoplus.javaapitables.spreadsheet.MarketCapSpreadsheetManager;
import com.innoplus.javaapitables.spreadsheet.MarketIndexesSpreadsheetManager;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.TimerTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MarketIndustryRefreshTask extends TimerTask {

    private static Log log = LogFactory.getLog(MarketIndustryRefreshTask.class);

    public static MarketIndustryRefreshTask getInstance() {
        MarketIndustryRefreshTask localMarketIndustryRefreshTask = new MarketIndustryRefreshTask();
        return localMarketIndustryRefreshTask;
    }

    public void run() {
        try {
            log.info("--- Starting MarketIndexes Manager refresh...");

            if (shouldRunNow()) {
                MarketIndexesSpreadsheetManager.getInstance().updateDataInSpreadsheet();
            }
            log.info("--- Finished MarketIndexes Manager refresh.");
        } catch (Exception ex) {
            log.error("Error while refreshing MarketIndexes Manager!");
        }

        try {
            log.info("--- Starting Industry Sectors Manager refresh...");
            if (shouldRunNow()) {
                IndustrySectorsSpreadsheetManager.getInstance().updateDataInSpreadsheet();
            }
            log.info("--- Finished Industry Sectors Manager refresh.");
        } catch (Exception ex) {
            log.error("Error while refreshing Industry Sectors Manager!");
        }

        try {
            log.info("--- Starting MarketCap Manager refresh...");
            if (shouldRunNow()) {
                MarketCapSpreadsheetManager.getInstance().updateDataInSpreadsheet();
            }
            log.info("--- Finished MarketCap Manager refresh.");
        } catch (Exception ex) {
            log.error("Error while refreshing MarketCap Manager!");
        }
    }

    protected boolean shouldRunNow() {
        // CVIT test only
        //return true;

        Calendar localCalendar = Calendar.getInstance(TimeZone.getTimeZone("US/Eastern"), Locale.ENGLISH);
        String str = "time now: [" + localCalendar.getTime() + "]";

        if ((localCalendar.get(Calendar.DAY_OF_MONTH) == 1) || (localCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)) {
            log.info(str);
            return true;
        }
        
        log.info(str + " today is not the first day of month or Monday, shouldRunNow returning false");
        return false;
    }
}
