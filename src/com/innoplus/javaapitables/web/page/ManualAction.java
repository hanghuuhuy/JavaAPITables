package com.innoplus.javaapitables.web.page;

import com.innoplus.javaapitables.core.Configuration;
import com.innoplus.javaapitables.jobs.JobsManager;
import com.innoplus.javaapitables.spreadsheet.IndustrySectorsSpreadsheetManager;
import com.innoplus.javaapitables.spreadsheet.MarketCapSpreadsheetManager;
import com.innoplus.javaapitables.spreadsheet.MarketIndexesSpreadsheetManager;
import com.innoplus.javaapitables.spreadsheet.NewProspectsCoveredCallSpreadsheetManager;
import com.innoplus.javaapitables.spreadsheet.NewProspectsCoveredPutSpreadsheetManager;
import com.innoplus.javaapitables.spreadsheet.PremiumCoveredCallSpreadsheetManager;
import com.innoplus.javaapitables.spreadsheet.PremiumCoveredPutSpreadsheetManager;
import com.innoplus.javaapitables.spreadsheet.PublicCoveredCallSpreadsheetManager;
import com.innoplus.javaapitables.spreadsheet.PublicCoveredPutSpreadsheetManager;
import com.innoplus.javaapitables.spreadsheet.SpreadsheetServiceWrapper;
import com.innoplus.javaapitables.spreadsheet.StockScreenerSpreadsheetManager;

import java.util.Timer;
import java.util.TimerTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ManualAction extends org.apache.click.Page {

    private static Log log = LogFactory.getLog(ManualAction.class);
    private boolean isUserLoggedIn;

    public ManualAction() {
        isUserLoggedIn = false;
    }

    @Override
    public boolean onSecurityCheck() {
        if (getContext().getRequest().getRemoteUser() == null) {
            isUserLoggedIn = false;
        }

        isUserLoggedIn = true;
        return true;
    }

    @Override
    public void onGet() {
        super.onGet();

        if (!isUserLoggedIn) {
            addModel("actionResponse", "Not Logged In. Please refresh page.");
            return;
        }
        
        String actionParam = getContext().getRequest().getParameter("action");

        if ((actionParam == null) || actionParam.isEmpty()) {
            addModel("actionResponse", "No Action Defined");
        }

        if (actionParam.equals("check_status")) {
            try {
                String jobsStatus = getAutoUpdateJobsStatus();
                addModel("actionResponse", jobsStatus);
            } catch (Exception ex) {
                addModel("actionResponse", "Auto Update start NOT successfull");
            }
            return;
        }

        // START ALL ACTIONS
        if (actionParam.equals("start_auto_update")) {
            try {
                startAutoUpdate();
                addModel("actionResponse", "Auto Update start successfull");
            } catch (Exception ex) {
                addModel("actionResponse", "Auto Update start NOT successfull");
            }
            return;
        }

        // PREMIUM TABLES MANUAL UPDATE
        if (Configuration.getInstance().getProperty("spreadsheets.coveredcallcall.action").equalsIgnoreCase(actionParam)) {
            try {
                PremiumCoveredCallSpreadsheetManager.getInstance().updateDataInSpreadsheet();
                addModel("actionResponse", "Update of Premium Call successfull");
            } catch (Exception ex) {
                addModel("actionResponse", "Update of Premium Call NOT successfull");
            }
            return;
        }
        if (Configuration.getInstance().getProperty("spreadsheets.putsellcalc.action").equalsIgnoreCase(actionParam)) {
            try {
                PremiumCoveredPutSpreadsheetManager.getInstance().updateDataInSpreadsheet();
                addModel("actionResponse", "Update of Premium Put successfull");
            } catch (Exception ex) {
                addModel("actionResponse", "Update of Premium Put NOT successfull");
            }
            return;
        }


        // PUBLIC TABLES MANUAL UPDATE
        if (Configuration.getInstance().getProperty("spreadsheets.coveredcallcall.actionpublic").equalsIgnoreCase(actionParam)) {
            try {
                PublicCoveredCallSpreadsheetManager.getInstance().updateDataInSpreadsheet();
                addModel("actionResponse", "Update of Public Call successfull");
            } catch (Exception ex) {
                addModel("actionResponse", "Update of Public Call NOT successfull");
            }
            return;
        }

        if (Configuration.getInstance().getProperty("spreadsheets.putsellcalc.actionpublic").equalsIgnoreCase(actionParam)) {
            try {
                PublicCoveredPutSpreadsheetManager.getInstance().updateDataInSpreadsheet();
                addModel("actionResponse", "Update of Public Put successfull");
            } catch (Exception ex) {
                addModel("actionResponse", "Update of Public Put NOT successfull");
            }
            return;
        }

        // NEW PROSPECTS MANUAL UPDATE
        if (Configuration.getInstance().getProperty("spreadsheets.coveredcallcall.actionnewprospect").equalsIgnoreCase(actionParam)) {
            try {
                NewProspectsCoveredCallSpreadsheetManager.getInstance().updateDataInSpreadsheet();
                addModel("actionResponse", "Update of New Prospects Covered Call successfull");
            } catch (Exception ex) {
                addModel("actionResponse", "Update of New Prospects Covered Call NOT successfull");
            }
            return;
        }

        if (Configuration.getInstance().getProperty("spreadsheets.putsellcalc.actionnewprospect").equalsIgnoreCase(actionParam)) {
            try {
                NewProspectsCoveredPutSpreadsheetManager.getInstance().updateDataInSpreadsheet();
                addModel("actionResponse", "Update of New Prospects Covered Put successfull");
            } catch (Exception ex) {
                addModel("actionResponse", "Update of New Prospects Covered Put NOT successfull");
            }
            return;
        }

        // MARKET INDEXES / INDUSTRY SECTORS / MARKET CAP UPDATE
        if (Configuration.getInstance().getProperty("spreadsheets.marketindexes.action").equalsIgnoreCase(actionParam)) {
            try {
                MarketIndexesSpreadsheetManager.getInstance().updateDataInSpreadsheet();
                addModel("actionResponse", "Update of Market Indexes successfull");
            } catch (Exception ex) {
                addModel("actionResponse", "Update of Market Indexes NOT successfull");
            }
            return;
        }

        if (Configuration.getInstance().getProperty("spreadsheets.industrysectors.action").equalsIgnoreCase(actionParam)) {
            try {
                IndustrySectorsSpreadsheetManager.getInstance().updateDataInSpreadsheet();
                addModel("actionResponse", "Update of Market Indexes successfull");
            } catch (Exception ex) {
                addModel("actionResponse", "Update of Market Indexes NOT successfull");
            }
            return;
        }

        if (Configuration.getInstance().getProperty("spreadsheets.marketcap.action").equalsIgnoreCase(actionParam)) {
            try {
                MarketCapSpreadsheetManager.getInstance().updateDataInSpreadsheet();
                addModel("actionResponse", "Update of Market Cap successfull");
            } catch (Exception ex) {
                addModel("actionResponse", "Update of Market Cap NOT successfull");
            }
            return;
        }
        
        if ("yahoo_stock_screener".equalsIgnoreCase(actionParam)) {
            try {
                new StockScreenerSpreadsheetManager().update();
                addModel("actionResponse", "Update of Yahoo Stocker Screeer successfull");
            } catch (Exception ex) {
                addModel("actionResponse", "Update of Yahoo Stocker Screeer NOT successfull");
            }
            return;
        }

    }

    @Override
    public void onPost() {
        super.onPost();
    }

    private void startAutoUpdate() {
        log.info("***** Starting StockAndOptionsTable application...");

        log.info("***** Loading Configuration...");
        Configuration localConfiguration = Configuration.getInstance();

        log.info("***** Initializing Spreadsheet Service Wrapper...");
        SpreadsheetServiceWrapper.getInstance();

        String str = Configuration.getInstance().getProperty("spreadsheets.all.refresh.auto_refresh_enabled");
        if ("true".equalsIgnoreCase(str)) {
            log.info("***** Starting jobs...");
            JobsManager localJobsManager = JobsManager.getInstance();
            localJobsManager.startAllJobs();
        } else {
            log.warn("***** Jobs are disabled and will not be started (check value of property [spreadsheets.all.refresh.auto_refresh_enabled]...");
        }

        log.info("***** Initialization complete.");
    }

    private String getAutoUpdateJobsStatus() {
        JobsManager localJobsManager = JobsManager.getInstance();
        return localJobsManager.getJobsStatus();
    }
}
