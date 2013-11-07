package com.innoplus.javaapitables.jobs.datarefresh;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.innoplus.javaapitables.core.Configuration;
import com.innoplus.javaapitables.spreadsheet.NewProspectsCoveredPutSpreadsheetManager;
import com.innoplus.javaapitables.spreadsheet.PremiumCoveredPutSpreadsheetManager;
import com.innoplus.javaapitables.spreadsheet.PublicCoveredPutSpreadsheetManager;

public class PremiumPutSpreadsheetRefreshTask extends BaseSpreadsheetRefreshTask {

    private static Log log = LogFactory.getLog(PremiumPutSpreadsheetRefreshTask.class);
    public static List<String> spreadsheets = new ArrayList();

    public PremiumPutSpreadsheetRefreshTask() {
        super(spreadsheets);
    }

    public void refreshSpreadsheet(String paramString) {
        try {
            PremiumCoveredPutSpreadsheetManager.getInstance().updateDataInSpreadsheet();
        } catch (Exception localException1) {
            log.error("Error while refresing Premium Covered Put spreadsheet", localException1);
        }
        try {
            PublicCoveredPutSpreadsheetManager.getInstance().updateDataInSpreadsheet();
        } catch (Exception localException2) {
            log.error("Error while refresing Public Covered Put spreadsheet", localException2);
        }
        try {
            NewProspectsCoveredPutSpreadsheetManager.getInstance().updateDataInSpreadsheet();
        } catch (Exception localException3) {
            log.error("Error while refresing New Prospects Covered Put spreadsheet", localException3);
        }
    }

    static {
        spreadsheets.add(Configuration.getInstance().getProperty("spreadsheets.putsellcalc.spreadsheet.name"));
    }
}
