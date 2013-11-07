package com.innoplus.javaapitables.spreadsheet;

import com.innoplus.javaapitables.core.Configuration;

public class PublicCoveredPutSpreadsheetManager extends PremiumCoveredPutSpreadsheetManager {

    public static PublicCoveredPutSpreadsheetManager getInstance() {
        PublicCoveredPutSpreadsheetManager localPutSellingCalcPublicCopySpreadsheetManager = new PublicCoveredPutSpreadsheetManager();
        return localPutSellingCalcPublicCopySpreadsheetManager;
    }

    @Override
    protected String getSpreadsheetName() {
        return Configuration.getInstance().getProperty("spreadsheets.putsellcalc.spreadsheet.name.public_version");
    }
}
