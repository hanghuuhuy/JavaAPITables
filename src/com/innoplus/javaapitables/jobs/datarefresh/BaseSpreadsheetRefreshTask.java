package com.innoplus.javaapitables.jobs.datarefresh;

import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.innoplus.javaapitables.core.Configuration;
import com.innoplus.javaapitables.util.DateUtil;
import java.util.Locale;
import java.util.TimeZone;

public abstract class BaseSpreadsheetRefreshTask extends TimerTask {

    private static Log log = LogFactory.getLog(BaseSpreadsheetRefreshTask.class);
    protected List<String> spreadsheetNames;

    public abstract void refreshSpreadsheet(String paramString);

    public BaseSpreadsheetRefreshTask(List<String> paramList) {
        log.info("starting refresh task for spreadsheets: " + paramList);
        this.spreadsheetNames = paramList;
    }

    public void run() {
        for (String str : this.spreadsheetNames) {
            try {

                //CVIT samo za production
                if (shouldRunNow()) {
                    log.info("--- starting refresh of spreadsheet [" + str + "]");
                    refreshSpreadsheet(str);
                    log.info("--- finished refresh of spreadsheet [" + str + "]");
                } else {
                    log.info("---! refresh of spreadsheet [" + str + "] is blocked due to scheduling rules (off market hours?)");
                }
                /*
                 // CVIT samo za test
                log.info("--- starting refresh of spreadsheet [" + str + "]");
                refreshSpreadsheet(str);
                log.info("--- finished refresh of spreadsheet [" + str + "]");
                 * 
                 */
            } catch (Exception ex) {
                log.error("--- !!! --- Error while refreshing spreadsheet [" + str + "]");
            }
        }
    }

    protected boolean shouldRunNow() {
        Calendar localCalendar = Calendar.getInstance(TimeZone.getTimeZone("US/Eastern"), Locale.ENGLISH);
        String str = "time now: [" + localCalendar.getTime() + "]";
        /*
        if (DateUtil.isHoliday(localCalendar)) {
            log.info(str + " today is a holiday, shouldRunNow returning false");
            return false;
        }
        if (DateUtil.isWeekend(localCalendar)) {
            log.info(str + " today is a weekend, shouldRunNow returning false");
            return false;
        }
         * 
         */
        if ((localCalendar.get(11) < Integer.parseInt(Configuration.getInstance().getProperty("spreadsheets.all.refresh.business_day.hour.start"))) || (localCalendar.get(11) > Integer.parseInt(Configuration.getInstance().getProperty("spreadsheets.all.refresh.business_day.hour.end")))) {
            log.info(str + " time of day does not qualify, shouldRunNow returning false");
            return false;
        }
        log.info(str);
        return true;
    }
}
