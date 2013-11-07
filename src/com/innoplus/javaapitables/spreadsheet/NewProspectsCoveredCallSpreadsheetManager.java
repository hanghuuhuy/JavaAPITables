package com.innoplus.javaapitables.spreadsheet;

import com.innoplus.javaapitables.core.Configuration;

public class NewProspectsCoveredCallSpreadsheetManager extends PremiumCoveredCallSpreadsheetManager {

    public static NewProspectsCoveredCallSpreadsheetManager getInstance() {
        NewProspectsCoveredCallSpreadsheetManager localCoveredCallCalcNewProspectsSpreadsheetManager = new NewProspectsCoveredCallSpreadsheetManager();
        return localCoveredCallCalcNewProspectsSpreadsheetManager;
    }

    protected String getSpreadsheetName() {
        return Configuration.getInstance().getProperty("spreadsheets.coveredcallcall.spreadsheet.name.new_prospects");
    }
}
