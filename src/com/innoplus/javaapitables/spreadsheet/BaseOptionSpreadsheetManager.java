package com.innoplus.javaapitables.spreadsheet;

import com.google.gdata.data.spreadsheet.Cell;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.util.ServiceException;
import com.innoplus.javaapitables.core.Configuration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.innoplus.javaapitables.data.RetrieveMode;
import com.innoplus.javaapitables.data.options.OptionsDataProvider;
import com.innoplus.javaapitables.domain.Option;
import com.innoplus.javaapitables.util.DateUtil;
import com.innoplus.javaapitables.util.StringUtil;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

public abstract class BaseOptionSpreadsheetManager extends BaseSpreadsheetManager {

    private static Log log = LogFactory.getLog(BaseOptionSpreadsheetManager.class);
    protected String STOCK_TICKER_COLUMN;
    protected String OPTION_TICKER_COLUMN;
    protected String OPTION_DATE_SYMBOL_COLUMN;
    protected String OPTION_STRIKE_COLUMN;
    protected String OPTION_BID_AMT_COLUMN;
    protected String OPTION_EXP_DATE_COLUMN;
    protected String DAY_UNTIL_OPT_EXP_COLUMN;
    protected String DIVIDEND_COLUMN;
    protected String DIVIDEND_VALUE_OVERRIDE_COLUMN;
    protected String EARNINGS_SURPRISES_COLUMN;
    protected String PAST_12_MNTHS_EPS_GROWTH_COLUMN;
    protected String NEXT_12_MONTHS_EPS_GROWTH_COLUMN;
    protected int FIRST_ROW;
    protected int LAST_ROW;
    protected String DIVIDEND_DATE_COLUMN;

    public void updateDataInSpreadsheet() {
        Object localObject1 = getSyncLockForSheet(this.spreadsheetName);
        synchronized (localObject1) {
            updateDataInSpreadsheet(this.FIRST_ROW, this.LAST_ROW, null, false);
        }
    }

    public void updateDataInSpreadsheet1() {
        Object localObject1 = getSyncLockForSheet(this.spreadsheetName);
        synchronized (localObject1) {
            updateDataInSpreadsheet(this.FIRST_ROW, this.LAST_ROW, null, true);
        }
    }
    
    public String countErrors() {
        Object localObject1 = getSyncLockForSheet(this.spreadsheetName);
        
        String count = "Spreadsheet [" + this.spreadsheetName + "] : \n";
        synchronized (localObject1) {
        	
        	try {
	        	 final OptionCalcRowAdaptor localOptionCalcRowAdaptor = getOptionCalRowAdaptor(this.FIRST_ROW, this.LAST_ROW, null);
	
	             
	             int i = 0;
	             List<String> stockTickers = localOptionCalcRowAdaptor.getStockTickers();
	
	             ArrayList threadArrayList = new ArrayList();
	             for (Iterator localIterator = stockTickers.iterator(); localIterator.hasNext();) {
	                 final String stockTicker = (String) localIterator.next();
	                 String optValue = localOptionCalcRowAdaptor.getOptionDateSymbolValue(stockTicker, i);
	                 if (optValue != null && optValue.startsWith("N/A")) {
	                	 
	                	 count += localOptionCalcRowAdaptor.getOptionTickeValue(stockTicker, i) + "\n";
	                 }
	                 i++;        	 
	             }
        	} catch (Exception e) {
        		log.error("Spreadsheet [" + this.spreadsheetName + "] : error while Counting data in spreadsheet [" + this.spreadsheetName + "]!", e);
                throw new RuntimeException(e);
        	}
        }
        
        return count;
    }
    
    protected final void updateDataInSpreadsheet(int firstRow, int lastRow, Object paramObject, boolean isSencondTime) {
        try {
            final OptionCalcRowAdaptor localOptionCalcRowAdaptor = getOptionCalRowAdaptor(firstRow, lastRow, paramObject);

           
            int i = 0;
            List<String> stockTickers = localOptionCalcRowAdaptor.getStockTickers();

            ArrayList threadArrayList = new ArrayList();
            for (Iterator localIterator = stockTickers.iterator(); localIterator.hasNext();) {
                final String stockTicker = (String) localIterator.next();


           	 	if (isSencondTime) {
           	 		
           	 		String optValue = localOptionCalcRowAdaptor.getOptionDateSymbolValue(stockTicker, i);
           	 	 
           	 		if (optValue != null && !optValue.startsWith("N/A")) {
           	 			i++;
           	 			continue;
           	 		}
                	
                }           	 

                // CVIT production
                if ((this instanceof MultiThreadable)) {
                    int[] arrayOfInt = {i};

                    // CVIT
                    Thread localThread = new Thread(new RunnableImplementation(this, stockTicker, arrayOfInt, localOptionCalcRowAdaptor));
                    threadArrayList.add(localThread);
                    localThread.start();

                } else {
                	
                    internalUpdateSpreadsheetRowForStock(stockTicker, i, localOptionCalcRowAdaptor);
                }

                // CVIT test
                /*
                internalUpdateSpreadsheetRowForStock(stockTicker, i, localOptionCalcRowAdaptor);
                 * 
                 */

                i++;
            }
            Object localObject;
            if ((this instanceof MultiThreadable)) {
                log.info("joining on [" + threadArrayList.size() + "] spreadsheet updater threads");
                for (Iterator localIterator = threadArrayList.iterator(); localIterator.hasNext();) {
                    localObject = localIterator.next();
                    ((Thread) localObject).join();
                }
                log.info("all spreadsheet updater threads finished");
            }
        } catch (Exception localException) {
            log.error("Spreadsheet [" + this.spreadsheetName + "] : error while updating data in spreadsheet [" + this.spreadsheetName + "]!", localException);
            throw new RuntimeException(localException);
        }
    }

    public void internalUpdateSpreadsheetRowForStock(String stockTicker, int rowIndex, OptionCalcRowAdaptor paramOptionCalcRowAdaptor) {
        log.info("Updating spreadsheet values for stock ticker [" + stockTicker + "]");
        try {
            if (StringUtil.isNullOrEmpty(stockTicker)) {
                log.warn("stockTicker is null, skipping row, index [" + rowIndex + "]");
                return;
            }

            // Get Option from Google spreadsheet
            Option scrappedOption = paramOptionCalcRowAdaptor.getOption(stockTicker, rowIndex);
            if (scrappedOption == null) {
                log.warn("stockTicker [" + stockTicker + "] option data cannot be obtained, skipping row, index [" + rowIndex + "]");
                writeErrorDataSpreadsheetRow(paramOptionCalcRowAdaptor, stockTicker, rowIndex);
                return;
            }

            log.info("found option  [" + scrappedOption.getTicker() + "] for stock [" + stockTicker + "]");

            Date expireDate = scrappedOption.getExpDate();
            String str1 = "-1";

            str1 = String.valueOf(DateUtil.getNumOfDaysBetween(DateUtil.getToday(), expireDate));

            // Scrape EarningsSurprises and epsGrowth for New Prospects tables
            String earningsSurprises = "";
            String epsGrowthThisYear = "";
            String epsGrowthNextYear = "";
            if (spreadsheetName.equals(Configuration.getInstance().getProperty("spreadsheets.coveredcallcall.spreadsheet.name.new_prospects"))
                    || spreadsheetName.equals(Configuration.getInstance().getProperty("spreadsheets.putsellcalc.spreadsheet.name.new_prospects"))) {
                HashMap<String, String> earningsSurprisesMap = new HashMap<String, String>();
                earningsSurprisesMap = paramOptionCalcRowAdaptor.getEarningsSurprisesMap(stockTicker);
                earningsSurprises = getEarningsSurprisesFromMap(earningsSurprisesMap);

                epsGrowthThisYear = earningsSurprisesMap.get("epsGrowthThisYear");
                epsGrowthNextYear = earningsSurprisesMap.get("epsGrowthNextYear");
            }

            // TODO Write Dividends
            String dividend = "";
            String dividendDate = "";
            if (!paramOptionCalcRowAdaptor.isDividendValueOverrideOn(stockTicker, rowIndex)) {
                Date spreadsheetDividendDate = paramOptionCalcRowAdaptor.getDividendDate(stockTicker, rowIndex);
                Double spreadSheetDividendValue = paramOptionCalcRowAdaptor.getDividendValue(stockTicker, rowIndex);
                HashMap<String, String> scrappedDividendMap = paramOptionCalcRowAdaptor.getNewDividendMap(stockTicker);
                dividend = scrappedDividendMap.get("dividend");
                String[] dividendParts = dividend.split(" ");
                dividend = dividendParts[0];
                dividendDate = scrappedDividendMap.get("dividendDate");

                // Parse the scrapped dividend Date
                Date newDividendDate = null;
                Double newDividendValue = null;
                boolean newDividendShouldBeWritten = false;
                if ((dividendDate != null) && (dividend != null)) {

                    DateFormat df = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
                    try {
                        newDividendDate = df.parse(dividendDate);
                    } catch (ParseException e) {
                        BaseOptionSpreadsheetManager.log.warn("New Dividend date for stock [" + stockTicker + "] is in uncorrect format, index [" + rowIndex + "], returning null dividend date");
                    }

                    try {
                        newDividendValue = Double.parseDouble(dividend);
                    } catch (NumberFormatException e) {
                        BaseOptionSpreadsheetManager.log.warn("New Dividend date for stock [" + stockTicker + "] is in uncorrect format, index [" + rowIndex + "], returning null dividend date");
                    }

                    // If the parsing is ok,
                    //Check if the last scrapped Dividend is newer than the one in the cpreadsheet
                    if ((newDividendDate != null) && (newDividendValue != null)) {

                        // If there is a newer dividend than the one in the spreadsheet, subtract the new amount from the one in the spreadsheet
                        if ((newDividendValue != null) && spreadsheetDividendDate.before(newDividendDate)) {
                            double newDividendToWrite = spreadSheetDividendValue.doubleValue() - newDividendValue.doubleValue();
                            if (newDividendToWrite >= 0) {
                                dividend = String.valueOf(newDividendToWrite);
                            } else {
                                dividend = "0";
                            }

                            SimpleDateFormat sdfToWrite = new SimpleDateFormat("MM/dd/yyyy");
                            dividendDate = sdfToWrite.format(newDividendDate);

                            newDividendShouldBeWritten = true;
                        }
                    }
                }

                // Make sure that no new dividends are written if there was a problem with scrapping a new dividend, or there is no newer dividend
                if (!newDividendShouldBeWritten) {
                    dividend = "";
                    dividendDate = "";
                }
            }


            log.info("about to write values for stock [" + stockTicker + "]");
            paramOptionCalcRowAdaptor.writeSpreadsheetRow(rowIndex, stockTicker, scrappedOption.getTicker(), expireDate, scrappedOption, earningsSurprises, epsGrowthThisYear, epsGrowthNextYear, dividend, dividendDate);
            log.info("values written successfully for stock [" + stockTicker + "] !");
        } catch (Exception localException) {
            log.error("Spreadsheet [" + this.spreadsheetName + "] : error while trying to update value of cell row [" + rowIndex + "]", localException);
        }
    }

    private void writeErrorDataSpreadsheetRow(OptionCalcRowAdaptor paramOptionCalcRowAdaptor, String stockTicker, int rowIndex) throws IOException, ServiceException {
        paramOptionCalcRowAdaptor.writeSpreadsheetRow(rowIndex, stockTicker, "NOT FOUND", null, new Option(), null, null, null, null, null);
    }

    protected OptionCalcRowAdaptor getOptionCalRowAdaptor(int firstRow, int lastRow, Object paramObject)
            throws IOException, ServiceException {
        return new OptionCalcRowAdaptorPrivate(firstRow, lastRow, paramObject);
    }

    private String getEarningsSurprisesFromMap(HashMap<String, String> earningsSurprisesMap) {
        String earningsSurprises = "";

        earningsSurprises += earningsSurprisesMap.get("date1") + ": " + earningsSurprisesMap.get("surprise1") + ",\n";
        earningsSurprises += earningsSurprisesMap.get("date2") + ": " + earningsSurprisesMap.get("surprise2") + ",\n";
        earningsSurprises += earningsSurprisesMap.get("date3") + ": " + earningsSurprisesMap.get("surprise3") + ",\n";
        earningsSurprises += earningsSurprisesMap.get("date4") + ": " + earningsSurprisesMap.get("surprise4");

        return earningsSurprises;
    }

    protected class OptionCalcRowAdaptorPrivate implements OptionCalcRowAdaptor {

        protected int firstRow;
        protected int lastRow;
        protected CellFeed stockTickersCellFeed;
        protected CellFeed optionTickersCellFeed;
        protected CellFeed optionDateSymbolCellFeed;
        protected CellFeed optionStrikeCellFeed;
        protected CellFeed optionBidAmtCellFeed;
        protected CellFeed optionExpDateCellFeed;
        protected CellFeed dividendCellFeed;
        protected CellFeed dividendValOverrideCellFeed;
        protected CellFeed earningsSurprisesCellFeed;
        protected CellFeed epsGrowthThisYearCellFeed;
        protected CellFeed epsGrowthNextYearCellFeed;
        protected CellFeed dividendDateCellFeed;

        public OptionCalcRowAdaptorPrivate(int firstRow, int lastRow, Object arg4) throws IOException, ServiceException {
            this.firstRow = firstRow;
            this.lastRow = lastRow;
            // TODO Isfrli firstRow i lastRow vo slednive komandi za da ne bide fiksen brojot na redovi vo google tabelite
            this.stockTickersCellFeed = BaseOptionSpreadsheetManager.this.getCellFeed(BaseOptionSpreadsheetManager.this.STOCK_TICKER_COLUMN, firstRow, lastRow);
            this.optionTickersCellFeed = BaseOptionSpreadsheetManager.this.getCellFeed(BaseOptionSpreadsheetManager.this.OPTION_TICKER_COLUMN, firstRow, lastRow);
            this.optionDateSymbolCellFeed = BaseOptionSpreadsheetManager.this.getCellFeed(BaseOptionSpreadsheetManager.this.OPTION_DATE_SYMBOL_COLUMN, firstRow, lastRow);
            this.optionStrikeCellFeed = BaseOptionSpreadsheetManager.this.getCellFeed(BaseOptionSpreadsheetManager.this.OPTION_STRIKE_COLUMN, firstRow, lastRow);
            this.optionBidAmtCellFeed = BaseOptionSpreadsheetManager.this.getCellFeed(BaseOptionSpreadsheetManager.this.OPTION_BID_AMT_COLUMN, firstRow, lastRow);
            this.optionExpDateCellFeed = BaseOptionSpreadsheetManager.this.getCellFeed(BaseOptionSpreadsheetManager.this.OPTION_EXP_DATE_COLUMN, firstRow, lastRow);
            this.dividendCellFeed = BaseOptionSpreadsheetManager.this.getCellFeed(BaseOptionSpreadsheetManager.this.DIVIDEND_COLUMN, firstRow, lastRow);
            this.dividendValOverrideCellFeed = BaseOptionSpreadsheetManager.this.getCellFeed(BaseOptionSpreadsheetManager.this.DIVIDEND_VALUE_OVERRIDE_COLUMN, firstRow, lastRow);
            this.dividendDateCellFeed = BaseOptionSpreadsheetManager.this.getCellFeed(BaseOptionSpreadsheetManager.this.DIVIDEND_DATE_COLUMN, firstRow, lastRow);

            // For New Prospects, add Earnings Surprises and EPS Growth columns
            if (spreadsheetName.equals(Configuration.getInstance().getProperty("spreadsheets.coveredcallcall.spreadsheet.name.new_prospects"))
                    || spreadsheetName.equals(Configuration.getInstance().getProperty("spreadsheets.putsellcalc.spreadsheet.name.new_prospects"))) {
                this.earningsSurprisesCellFeed = BaseOptionSpreadsheetManager.this.getCellFeed(BaseOptionSpreadsheetManager.this.EARNINGS_SURPRISES_COLUMN, firstRow, lastRow);
                this.epsGrowthThisYearCellFeed = BaseOptionSpreadsheetManager.this.getCellFeed(BaseOptionSpreadsheetManager.this.PAST_12_MNTHS_EPS_GROWTH_COLUMN, firstRow, lastRow);
                this.epsGrowthNextYearCellFeed = BaseOptionSpreadsheetManager.this.getCellFeed(BaseOptionSpreadsheetManager.this.NEXT_12_MONTHS_EPS_GROWTH_COLUMN, firstRow, lastRow);
            }

        }

        @Override
		public List<String> getStockTickers() throws IOException, ServiceException {
            ArrayList localArrayList = new ArrayList();
            try {
                for (CellEntry localCellEntry : this.stockTickersCellFeed.getEntries()) {
                    if ((localCellEntry != null) && (localCellEntry.getCell() != null)) {
                        localArrayList.add(localCellEntry.getCell().getValue());
                    } else {
                        localArrayList.add("");
                    }
                }
            } catch (Exception localException) {
                BaseOptionSpreadsheetManager.log.error("Spreadsheet [" + BaseOptionSpreadsheetManager.this.spreadsheetName + "] : error while retrieving tickers, returning empty ticker list!", localException);
            }
            BaseOptionSpreadsheetManager.log.info("found ticker list:" + localArrayList);
            return localArrayList;
        }

        @Override
		public Option getOption(String stockTicker, int rowIndex) {
            String str = this.optionTickersCellFeed.getEntries().get(rowIndex).getCell().getValue();
            if (StringUtil.isNullOrEmpty(str)) {
                BaseOptionSpreadsheetManager.log.warn("Option ticker for stock [" + stockTicker + "] is null or empty, index [" + rowIndex + "], returning null option");
                return null;
            }
            Option localOption = OptionsDataProvider.getOptionByTicker(stockTicker, str, RetrieveMode.WEB_UPDATE_DATABASE);
            if (localOption == null) {
                BaseOptionSpreadsheetManager.log.warn("unable to find option for option ticker [" + str + "]");
            }
            return localOption;
        }
        
        @Override
		public String getOptionDateSymbolValue(String stockTicker, int rowIndex) {
            return this.optionDateSymbolCellFeed.getEntries().get(rowIndex).getCell().getValue();
        }
        
        @Override
		public String getOptionTickeValue(String stockTicker, int rowIndex) {
            return this.optionTickersCellFeed.getEntries().get(rowIndex).getCell().getValue();
        }
        
        @Override
		public boolean isDividendValueOverrideOn(String stockTicker, int rowIndex) {
            boolean i = false;
            try {
                String str = this.dividendValOverrideCellFeed.getEntries().get(rowIndex).getCell().getValue();
                if (!StringUtil.isNullOrEmpty(str)) {
                    i = (str.trim().equals("1")) || (str.trim().equalsIgnoreCase("true")) ? true : false;
                }
            } catch (Exception localException) {
                BaseOptionSpreadsheetManager.log.warn("Spreadsheet [" + BaseOptionSpreadsheetManager.this.spreadsheetName + "]: error while getting div override value for stock [" + stockTicker + "], index [" + rowIndex + "] :" + localException.getMessage());
            }

            return i;
        }

        @Override
		public void writeSpreadsheetRow(int rowIndex, String stockTicker, String scrappedStockTicker, Date expireDate, Option scrappedOption, String earningsSurprises, String epsGrowthThisYear, String epsGrowthNextYear, String dividend, String dividendDate) throws IOException, ServiceException {
            // Write option values
            BaseOptionSpreadsheetManager.this.insertOrUpdateCellValue(BaseOptionSpreadsheetManager.this.worksheet, this.optionDateSymbolCellFeed, rowIndex, BaseOptionSpreadsheetManager.this.normalizeDateValueForSheet(expireDate) + "   (" + BaseOptionSpreadsheetManager.this.normalizeValueForSheet(scrappedOption.getTicker()) + ")", this.firstRow, BaseSpreadsheetManager.getColumnNumberFromColumnName(BaseOptionSpreadsheetManager.this.OPTION_DATE_SYMBOL_COLUMN));
            BaseOptionSpreadsheetManager.this.insertOrUpdateCellValue(BaseOptionSpreadsheetManager.this.worksheet, this.optionStrikeCellFeed, rowIndex, BaseOptionSpreadsheetManager.this.normalizeNumericValueForSheet(scrappedOption.getStrikePrice()), this.firstRow, BaseSpreadsheetManager.getColumnNumberFromColumnName(BaseOptionSpreadsheetManager.this.OPTION_STRIKE_COLUMN));
            BaseOptionSpreadsheetManager.this.insertOrUpdateCellValue(BaseOptionSpreadsheetManager.this.worksheet, this.optionBidAmtCellFeed, rowIndex, BaseOptionSpreadsheetManager.this.normalizeNumericValueForSheet(scrappedOption.getPriceBid()), this.firstRow, BaseSpreadsheetManager.getColumnNumberFromColumnName(BaseOptionSpreadsheetManager.this.OPTION_BID_AMT_COLUMN));
            BaseOptionSpreadsheetManager.this.insertOrUpdateCellValue(BaseOptionSpreadsheetManager.this.worksheet, this.optionExpDateCellFeed, rowIndex, BaseOptionSpreadsheetManager.this.normalizeDateValueForSheet(expireDate), this.firstRow, BaseSpreadsheetManager.getColumnNumberFromColumnName(BaseOptionSpreadsheetManager.this.OPTION_EXP_DATE_COLUMN));

            // For New Prospects tables, Write Earnings Surprises and ESP Growth columns
            if (spreadsheetName.equals(Configuration.getInstance().getProperty("spreadsheets.coveredcallcall.spreadsheet.name.new_prospects"))
                    || spreadsheetName.equals(Configuration.getInstance().getProperty("spreadsheets.putsellcalc.spreadsheet.name.new_prospects"))) {
                BaseOptionSpreadsheetManager.this.insertOrUpdateCellValue(BaseOptionSpreadsheetManager.this.worksheet, this.earningsSurprisesCellFeed, rowIndex, BaseOptionSpreadsheetManager.this.normalizeValueForSheet(earningsSurprises), this.firstRow, BaseSpreadsheetManager.getColumnNumberFromColumnName(BaseOptionSpreadsheetManager.this.EARNINGS_SURPRISES_COLUMN));

                BaseOptionSpreadsheetManager.this.insertOrUpdateCellValue(BaseOptionSpreadsheetManager.this.worksheet, this.epsGrowthThisYearCellFeed, rowIndex, BaseOptionSpreadsheetManager.this.normalizeValueForSheet(epsGrowthThisYear), this.firstRow, BaseSpreadsheetManager.getColumnNumberFromColumnName(BaseOptionSpreadsheetManager.this.PAST_12_MNTHS_EPS_GROWTH_COLUMN));
                BaseOptionSpreadsheetManager.this.insertOrUpdateCellValue(BaseOptionSpreadsheetManager.this.worksheet, this.epsGrowthNextYearCellFeed, rowIndex, BaseOptionSpreadsheetManager.this.normalizeValueForSheet(epsGrowthNextYear), this.firstRow, BaseSpreadsheetManager.getColumnNumberFromColumnName(BaseOptionSpreadsheetManager.this.NEXT_12_MONTHS_EPS_GROWTH_COLUMN));
            }

            // Write dividends if there is no override, and there are values for dividend and dividendDate
            if (!isDividendValueOverrideOn(stockTicker, rowIndex) && !StringUtil.isNullOrEmpty(dividend) && !StringUtil.isNullOrEmpty(dividendDate)) {
                BaseOptionSpreadsheetManager.this.insertOrUpdateCellValue(BaseOptionSpreadsheetManager.this.worksheet, this.dividendCellFeed, rowIndex, BaseOptionSpreadsheetManager.this.normalizeNumericValueForSheet(dividend), this.firstRow, BaseSpreadsheetManager.getColumnNumberFromColumnName(BaseOptionSpreadsheetManager.this.DIVIDEND_COLUMN));
                BaseOptionSpreadsheetManager.this.insertOrUpdateCellValue(BaseOptionSpreadsheetManager.this.worksheet, this.dividendDateCellFeed, rowIndex, BaseOptionSpreadsheetManager.this.normalizeValueForSheet(dividendDate), this.firstRow, BaseSpreadsheetManager.getColumnNumberFromColumnName(BaseOptionSpreadsheetManager.this.DIVIDEND_DATE_COLUMN));
            }
        }

        @Override
		public HashMap<String, String> getEarningsSurprisesMap(String stockTicker) {
            if (StringUtil.isNullOrEmpty(stockTicker)) {
                BaseOptionSpreadsheetManager.log.warn("Option ticker for stock [" + stockTicker + "] is null or empty, returning null option");
                return null;
            }

            HashMap<String, String> earningsSurprisesMap = new HashMap<String, String>();
            earningsSurprisesMap = OptionsDataProvider.getEarningsSurprisesByTicker(stockTicker);
            if (earningsSurprisesMap.isEmpty()) {
                BaseOptionSpreadsheetManager.log.warn("unable to find earnings surprises for option ticker [" + stockTicker + "]");
            }
            return earningsSurprisesMap;
        }

        @Override
		public Date getDividendDate(String stockTicker, int rowIndex) {
            String dividendDateCellValue = this.dividendDateCellFeed.getEntries().get(rowIndex).getCell().getValue();
            if (StringUtil.isNullOrEmpty(dividendDateCellValue)) {
                BaseOptionSpreadsheetManager.log.warn("Dividend date for stock [" + stockTicker + "] is null or empty, index [" + rowIndex + "], returning Datemin dividend date");
                return new Date(Long.MIN_VALUE);
                //return null;
            }
            Date dividendDate = null;
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            try {
                dividendDate = df.parse(dividendDateCellValue);
            } catch (ParseException e) {
                BaseOptionSpreadsheetManager.log.warn("Dividend date for stock [" + stockTicker + "] is in uncorrect format, index [" + rowIndex + "], returning null dividend date");
                return null;
            }

            return dividendDate;
        }

        @Override
		public Double getDividendValue(String stockTicker, int rowIndex) {
            String dividendCellValue = this.dividendCellFeed.getEntries().get(rowIndex).getCell().getValue();
            if (StringUtil.isNullOrEmpty(dividendCellValue)) {
                BaseOptionSpreadsheetManager.log.warn("Dividend date for stock [" + stockTicker + "] is null or empty, index [" + rowIndex + "], returning null dividend amount");
                return null;
            }
            Double dividend = null;
            try {
                dividend = Double.valueOf(dividendCellValue.replace("$", ""));
            } catch (NumberFormatException e) {
                BaseOptionSpreadsheetManager.log.warn("Dividend amount for stock [" + stockTicker + "] is in uncorrect format, index [" + rowIndex + "], returning null dividend amount");
                return null;
            }

            return dividend;
        }

        @Override
		public HashMap<String, String> getNewDividendMap(String stockTicker) {
            if (StringUtil.isNullOrEmpty(stockTicker)) {
                BaseOptionSpreadsheetManager.log.warn("Option ticker for stock [" + stockTicker + "] is null or empty, returning null dividendMap");
                return null;
            }

            HashMap<String, String> newDividendMap = new HashMap<String, String>();
            newDividendMap = OptionsDataProvider.getLastDividendByTicker(stockTicker);
            if (newDividendMap.isEmpty()) {
                BaseOptionSpreadsheetManager.log.warn("unable to find earnings surprises for option ticker [" + stockTicker + "]");
            }
            return newDividendMap;
        }
    }

    static class RunnableImplementation implements Runnable {

        private final String stockTicker;
        private final int[] arrayOfInt;
        private final OptionCalcRowAdaptor localOptionCalcRowAdaptor;
        public BaseOptionSpreadsheetManager spreadsheetManager;

        public RunnableImplementation(BaseOptionSpreadsheetManager spreadsheetManager, String stockTicker, int[] arrayOfInt, OptionCalcRowAdaptor localOptionCalcRowAdaptor) {
            this.stockTicker = stockTicker;
            this.arrayOfInt = arrayOfInt;
            this.localOptionCalcRowAdaptor = localOptionCalcRowAdaptor;
            this.spreadsheetManager = spreadsheetManager;
        }

        @Override
		public void run() {
            //BaseOptionSpreadsheetManager.this.internalUpdateSpreadsheetRowForStock(this.val$stockTicker, this.val$arr[0], this.val$rowAdaptor);
            spreadsheetManager.internalUpdateSpreadsheetRowForStock(stockTicker, arrayOfInt[0], localOptionCalcRowAdaptor);
        }
    }
}
