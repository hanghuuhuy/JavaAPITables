package com.innoplus.javaapitables.spreadsheet;

import com.innoplus.javaapitables.core.Configuration;

public class PublicCoveredCallSpreadsheetManager extends PremiumCoveredCallSpreadsheetManager {

    public static PublicCoveredCallSpreadsheetManager getInstance() {
        PublicCoveredCallSpreadsheetManager localPublicCoveredCallSpreadsheetManager = new PublicCoveredCallSpreadsheetManager();
        return localPublicCoveredCallSpreadsheetManager;
    }

    @Override
    protected String getSpreadsheetName() {
        return Configuration.getInstance().getProperty("spreadsheets.coveredcallcall.spreadsheet.name.public_version");
    }
}
