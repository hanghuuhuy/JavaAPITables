package com.innoplus.javaapitables.spreadsheet;

import com.innoplus.javaapitables.core.Configuration;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.util.AuthenticationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SpreadsheetServiceWrapper {

    private static Log log = LogFactory.getLog(SpreadsheetServiceWrapper.class);
    private static SpreadsheetServiceWrapper ourInstance = new SpreadsheetServiceWrapper();
    private SpreadsheetService spreadsheetService;

    public static SpreadsheetServiceWrapper getInstance() {
        return ourInstance;
    }

    private SpreadsheetServiceWrapper() {
        this.spreadsheetService = new SpreadsheetService("InnoPlus-StockAndOptionsTables-2.0");

        Configuration localConfiguration = Configuration.getInstance();
        String str1 = localConfiguration.getProperty("google.username");
        String str2 = localConfiguration.getProperty("google.password");
        try {
            this.spreadsheetService.setUserCredentials(str1, str2);
            log.info("Google Docs authentication to account [" + str1 + "] - successful.");
        } catch (AuthenticationException localAuthenticationException) {
            log.error("Error authenticating to Google Docs, account [" + str1 + "] : " + localAuthenticationException.getMessage(), localAuthenticationException);
        }
    }

    public SpreadsheetService getSpreadsheetService() {
        return this.spreadsheetService;
    }
}
