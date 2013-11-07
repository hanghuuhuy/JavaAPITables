package com.innoplus.javaapitables.jobs.datarefresh;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.innoplus.javaapitables.core.Configuration;
import com.innoplus.javaapitables.spreadsheet.NewProspectsCoveredCallSpreadsheetManager;
import com.innoplus.javaapitables.spreadsheet.PremiumCoveredCallSpreadsheetManager;
import com.innoplus.javaapitables.spreadsheet.PublicCoveredCallSpreadsheetManager;

public class PremiumCallSpreadsheetRefreshTask extends BaseSpreadsheetRefreshTask {

    private static Log log = LogFactory.getLog(PremiumCallSpreadsheetRefreshTask.class);
    public static List<String> spreadsheets = new ArrayList();

    public PremiumCallSpreadsheetRefreshTask() {
        super(spreadsheets);
    }

    public void refreshSpreadsheet(String paramString) {
        try {
            PremiumCoveredCallSpreadsheetManager.getInstance().updateDataInSpreadsheet();
        } catch (Exception localException1) {
            log.error("Error while refresing Premium Covered Call spreadsheet", localException1);
        }
        try {
            PublicCoveredCallSpreadsheetManager.getInstance().updateDataInSpreadsheet();
        } catch (Exception localException2) {
            log.error("Error while refresing Public Covered Call spreadsheet", localException2);
        }
        try {
            NewProspectsCoveredCallSpreadsheetManager.getInstance().updateDataInSpreadsheet();
        } catch (Exception localException3) {
            log.error("Error while refresing New Prospects Covered Call spreadsheet", localException3);
        }
    }

    static {
        spreadsheets.add(Configuration.getInstance().getProperty("spreadsheets.coveredcallcall.spreadsheet.name"));
    }
}
