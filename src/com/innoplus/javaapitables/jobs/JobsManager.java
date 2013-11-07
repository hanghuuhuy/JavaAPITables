package com.innoplus.javaapitables.jobs;

/*
import demar.tables.jobs.datarefresh.DividendYieldTableRefreshTask;
import demar.tables.jobs.datarefresh.IndustrySectorsRefreshTask;
import demar.tables.jobs.datarefresh.MarketIndexesRefreshTask;
import demar.tables.jobs.datarefresh.PortfolioManagerRefreshTask;
import demar.tables.jobs.datarefresh.PutSellingCalcRefreshTask;
import demar.tables.jobs.datarefresh.StockDividendsRefreshTask;
import demar.tables.util.CollectionsUtil;
import java.util.Arrays;
import java.util.List;
 * 
 */
import java.util.Timer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.innoplus.javaapitables.core.Configuration;
import com.innoplus.javaapitables.jobs.datarefresh.MarketIndustryRefreshTask;
import com.innoplus.javaapitables.jobs.datarefresh.PremiumCallSpreadsheetRefreshTask;
import com.innoplus.javaapitables.jobs.datarefresh.PremiumPutSpreadsheetRefreshTask;

public class JobsManager {

    private static Log log = LogFactory.getLog(JobsManager.class);
    private static JobsManager ourInstance = new JobsManager();
    //public Timer premiumCallTimer = new Timer("premium_call-refresh-timer-job", true);
    //public Timer premiumPutTimer = new Timer("premium_put-refresh-timer-job", true);

    public static JobsManager getInstance() {
        return ourInstance;
    }

    public void startAllJobs() {
        /*
        try
        {
        startDividendYieldTableRefreshJobs();
        } catch (Exception localException1) {
        log.error("Error while starting dividend yield table refresh jobs, some or all jobs have not started!", localException1);
        }
         *
         */
        
        try {
            startPremiumCallRefreshJob();
        } catch (Exception ex) {
            log.error("Error starting Premium Call refresh job, some or all jobs have not started!", ex);
        }

        try {
            startPremiumPutRefreshJob();
        } catch (Exception ex) {
            log.error("Error starting Premium Put refresh job, some or all jobs have not started!", ex);
        }

        try {
            startMarketIndustryRefreshJob();
        } catch (Exception ex) {
            log.error("Error starting Market Industry refresh job, some or all jobs have not started!", ex);
        }

        /*
        try
        {
        startDividendsRefreshJob();
        } catch (Exception localException4) {
        log.error("Error starting dividends refresh job, some or all jobs have not started!", localException4);
        }

        try
        {
        startPortfolioMonitorRefreshJob();
        } catch (Exception localException5) {
        log.error("Error starting Portfolio Monitor refresh job, some or all jobs have not started!", localException5);
        }
         *
         */
    }

    /*
    private void startPortfolioMonitorRefreshJob() {
    log.info("Starting : startPortfolioMonitorRefreshJob");

    int i = Integer.parseInt(Configuration.getInstance().getProperty("spreadsheets.portfoliomonitor.refresh.start_delay_millis"));
    int j = Integer.parseInt(Configuration.getInstance().getProperty("spreadsheets.portfoliomonitor.refresh.repeat_millis"));

    log.info("starting PortfolioManagerRefreshTask : delay [" + i + "], repeatMillis [" + j + "]");

    Timer localTimer = new Timer("portfolio-manager-refresh-timer-job", true);
    localTimer.schedule(new PortfolioManagerRefreshTask(), i, j);
    }

    private void startDividendsRefreshJob() {
    log.info("Starting : startDividendsRefreshJob");

    int i = Integer.parseInt(Configuration.getInstance().getProperty("dividends.refresh.start_delay_millis"));
    int j = Integer.parseInt(Configuration.getInstance().getProperty("dividends.refresh.repeat_millis"));

    log.info("starting StockDividendsRefreshTask : delay [" + i + "], repeatMillis [" + j + "]");

    Timer localTimer = new Timer("div-refresh-timer-job", true);
    localTimer.schedule(new StockDividendsRefreshTask(), i, j);
    }
     *
     */
    private void startPremiumCallRefreshJob() {
        log.info("Starting : startPremiumCallRefreshJob");

        int i = Integer.parseInt(Configuration.getInstance().getProperty("spreadsheets.coveredcallcalc.refresh.start_delay_millis"));
        int j = Integer.parseInt(Configuration.getInstance().getProperty("spreadsheets.coveredcallcalc.refresh.repeat_millis"));

        log.info("starting CoveredCallCalcRefreshTask : delay [" + i + "], repeatMillis [" + j + "]");

        // CVIT Original scheduler
        // The timer must always be a new instance, otherwise it will be scheduled only once!!!
        Timer localTimer = new Timer("premium_call-refresh-timer-job", true);
        //premiumCallTimer.schedule(new PremiumCallSpreadsheetRefreshTask(), i, j);
        localTimer.schedule(new PremiumCallSpreadsheetRefreshTask(), i, j);
    }

    private void startPremiumPutRefreshJob() {
        log.info("Starting : startPremiumPutRefreshJob");

        int i = Integer.parseInt(Configuration.getInstance().getProperty("spreadsheets.putsellingcalc.refresh.start_delay_millis"));
        int j = Integer.parseInt(Configuration.getInstance().getProperty("spreadsheets.putsellingcalc.refresh.repeat_millis"));

        log.info("starting PutSellingCalcRefreshTask : delay [" + i + "], repeatMillis [" + j + "]");

        // The timer must always be a new instance, otherwise it will be scheduled only once!!!
        Timer localTimer = new Timer("premium_put-refresh-timer-job", true);
        //premiumPutTimer.schedule(new PremiumPutSpreadsheetRefreshTask(), i, j);
        localTimer.schedule(new PremiumPutSpreadsheetRefreshTask(), i, j);
    }

    protected void startMarketIndustryRefreshJob() {
        log.info("Starting : startMarketIndexesRefreshJob");

        int i = Integer.parseInt(Configuration.getInstance().getProperty("spreadsheets.marketindustry.refresh.start_delay_millis"));
        int j = Integer.parseInt(Configuration.getInstance().getProperty("spreadsheets.marketindustry.refresh.repeat_millis"));

        log.info("starting MarketIndustryRefreshTask : delay [" + i + "], repeatMillis [" + j + "]");

        // The timer must always be a new instance, otherwise it will be scheduled only once!!!
        Timer localTimer = new Timer("market-industry-timer-job", true);
        localTimer.schedule(new MarketIndustryRefreshTask(), i, j);
    }

    public String getJobsStatus() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /*
    private void startDividendYieldTableRefreshJobs()
    {
    log.info("Starting : startDividendYieldTableRefreshJobs");
    String str = Configuration.getInstance().getProperty("spreadsheets.divyieldtables.refresh.spreadsheets.names");
    log.info("divYieldSheetNames=" + str);

    String[] arrayOfString = str.split(",");
    List localList1 = Arrays.asList(arrayOfString);

    int i = Integer.parseInt(Configuration.getInstance().getProperty("spreadsheets.divyieldtables.refresh.threads"));
    List localList2 = CollectionsUtil.splitListIntoSublists(localList1, i);

    int j = Integer.parseInt(Configuration.getInstance().getProperty("spreadsheets.divyieldtables.refresh.start_delay_millis"));

    int m = Integer.parseInt(Configuration.getInstance().getProperty("spreadsheets.divyieldtables.refresh.next_thread_add_delay"));
    int n = Integer.parseInt(Configuration.getInstance().getProperty("spreadsheets.divyieldtables.refresh.repeat_millis"));

    log.info("about to start [" + localList2.size() + "] dividend yield refresh jobs (configured num of threads is [" + i + "]), with initial delay of [" + j + "] millis, next tread additional delay [" + m + "] millis, and repeat every [" + n + "] millis");

    int i1 = 1;
    for (List localList3 : localList2) {
    if ((localList3 == null) || (localList3.isEmpty())) {
    log.warn("sublist with index [" + i1 + "] is empty, job will not be started!");
    i1++;
    continue;
    }
    Timer localTimer = new Timer("div-yield-refresh-job-" + i1, true);
    log.info("starting DividendYieldTableRefreshTask [" + i1 + "]: delay [" + j + "], repeatMillis [" + n + "]");
    localTimer.schedule(new DividendYieldTableRefreshTask(localList3), j, n);
    int k = m * i1;
    i1++;
    }
    }
     *
     */
}
