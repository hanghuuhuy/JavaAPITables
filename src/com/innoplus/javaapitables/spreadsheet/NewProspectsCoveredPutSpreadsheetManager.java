package com.innoplus.javaapitables.spreadsheet;

import com.innoplus.javaapitables.core.Configuration;

public class NewProspectsCoveredPutSpreadsheetManager extends PremiumCoveredPutSpreadsheetManager {

    public static NewProspectsCoveredPutSpreadsheetManager getInstance() {
        NewProspectsCoveredPutSpreadsheetManager localPutSellingCalcNewProspectsSpreadsheetManager = new NewProspectsCoveredPutSpreadsheetManager();
        return localPutSellingCalcNewProspectsSpreadsheetManager;
    }

    protected String getSpreadsheetName() {
        return Configuration.getInstance().getProperty("spreadsheets.putsellcalc.spreadsheet.name.new_prospects");
    }
}
