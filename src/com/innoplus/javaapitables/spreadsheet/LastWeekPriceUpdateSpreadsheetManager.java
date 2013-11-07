package com.innoplus.javaapitables.spreadsheet;

import com.google.gdata.data.spreadsheet.Cell;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.util.ServiceException;
import com.innoplus.javaapitables.core.Configuration;
import com.innoplus.javaapitables.data.marketindustries.MarketIndustryDataProvider;
import com.innoplus.javaapitables.domain.MarketIndustry;
import com.innoplus.javaapitables.util.StringUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class LastWeekPriceUpdateSpreadsheetManager extends BaseSpreadsheetManager {

    private static Log log = LogFactory.getLog(LastWeekPriceUpdateSpreadsheetManager.class);
    protected String ETF_SYMBOL_COLUMN;
    protected String OPENING_MONTH_PRICE_COLUMN;
    protected String LAST_WEEK_PRICE_COLUMN;
    protected String URL_COLUMN;
    protected String CURRENT_PRICE_COLUMN;
    protected int FIRST_ROW;
    protected int LAST_ROW;
    
    public void updateDataInSpreadsheet() {
        Object localObject1 = getSyncLockForSheet(this.spreadsheetName);
        synchronized (localObject1) {
            updateDataInSpreadsheet(this.FIRST_ROW, this.LAST_ROW, null);
        }
    }

    public void updateWeekly(int lastRow) {
        Object localObject1 = getSyncLockForSheet(this.spreadsheetName);
        synchronized (localObject1) {
            
        	try {
        		CellFeed currentPricePriceCellFeed = LastWeekPriceUpdateSpreadsheetManager.this.getCellFeed(LastWeekPriceUpdateSpreadsheetManager.this.CURRENT_PRICE_COLUMN, 2, lastRow);
        		CellFeed lastWeekPricePriceCellFeed = LastWeekPriceUpdateSpreadsheetManager.this.getCellFeed(LastWeekPriceUpdateSpreadsheetManager.this.LAST_WEEK_PRICE_COLUMN, 2, lastRow);
        		
        		int i = 0;
        		for (CellEntry localCellEntry : currentPricePriceCellFeed.getEntries()) {
        			String currentPrice = localCellEntry.getCell().getValue();
        			
        			CellEntry localCellEntry1 = (CellEntry)lastWeekPricePriceCellFeed.getEntries().get(i);
   			      	localCellEntry1.changeInputValueLocal(LastWeekPriceUpdateSpreadsheetManager.this.normalizeNumericValueForSheet(currentPrice));
   			      	localCellEntry1.update();
   			      
        			i++;
                }
        		 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        }
    }
    
    public void updateMonthly(int lastRow) {
        Object localObject1 = getSyncLockForSheet(this.spreadsheetName);
        synchronized (localObject1) {
            
        	try {
        		CellFeed currentPricePriceCellFeed = LastWeekPriceUpdateSpreadsheetManager.this.getCellFeed(LastWeekPriceUpdateSpreadsheetManager.this.CURRENT_PRICE_COLUMN, 2, lastRow);
        		CellFeed lastWeekPricePriceCellFeed = LastWeekPriceUpdateSpreadsheetManager.this.getCellFeed(LastWeekPriceUpdateSpreadsheetManager.this.OPENING_MONTH_PRICE_COLUMN, 2, lastRow);
        		
        		int i = 0;
        		for (CellEntry localCellEntry : currentPricePriceCellFeed.getEntries()) {
        			String currentPrice = localCellEntry.getCell().getValue();
        			
        			CellEntry localCellEntry1 = (CellEntry)lastWeekPricePriceCellFeed.getEntries().get(i);
   			      	localCellEntry1.changeInputValueLocal(LastWeekPriceUpdateSpreadsheetManager.this.normalizeNumericValueForSheet(currentPrice));
   			      	localCellEntry1.update();
   			      
        			i++;
                }
        		 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        }
    }
    
    public void updateYearly(String fromColumn, String toColumn, int lastRow) {
        Object localObject1 = getSyncLockForSheet(this.spreadsheetName);
        synchronized (localObject1) {
            
        	try {
        		
        		CellFeed currentPricePriceCellFeed = LastWeekPriceUpdateSpreadsheetManager.this.getCellFeed(fromColumn, 2, lastRow);
        		CellFeed lastWeekPricePriceCellFeed = LastWeekPriceUpdateSpreadsheetManager.this.getCellFeed(toColumn, 2, lastRow);
        		
        		int i = 0;
        		for (CellEntry localCellEntry : currentPricePriceCellFeed.getEntries()) {
        			String currentPrice = localCellEntry.getCell().getValue();
        			
        			CellEntry localCellEntry1 = (CellEntry)lastWeekPricePriceCellFeed.getEntries().get(i);
   			      	localCellEntry1.changeInputValueLocal(LastWeekPriceUpdateSpreadsheetManager.this.normalizeNumericValueForSheet(currentPrice));
   			      	localCellEntry1.update();
   			      
        			i++;
                }
        		 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        }
    }
    
    protected final void updateDataInSpreadsheet(int firstRow, int lastRow, Object paramObject) {
        try {
            final MarketIndustryRowAdaptor localMarketIndustryRowAdaptor = getMarketIndustryRowAdaptor(firstRow, lastRow, paramObject);

            int i = 0;
            List<String> etfSymbols = localMarketIndustryRowAdaptor.getEtfSymbols();

            ArrayList threadArrayList = new ArrayList();

            for (Iterator localIterator = etfSymbols.iterator(); localIterator.hasNext();) {
                final String etfSymbol = (String) localIterator.next();


                // CVIT production
                if ((this instanceof MultiThreadable)) {
                int[] arrayOfInt = {i};

                // CVIT
                Thread localThread = new Thread(new RunnableImplementation(this, etfSymbol, arrayOfInt, localMarketIndustryRowAdaptor));
                threadArrayList.add(localThread);
                localThread.start();

                } else {
                internalUpdateSpreadsheetRowForUrl(etfSymbol, i, localMarketIndustryRowAdaptor);
                }

                // CVIT test
                //internalUpdateSpreadsheetRowForUrl(etfSymbol, i, localMarketIndustryRowAdaptor);

                i++;
            }
        } catch (Exception localException) {
            log.error("Spreadsheet [" + this.spreadsheetName + "] : error while updating data in spreadsheet [" + this.spreadsheetName + "]!", localException);
            throw new RuntimeException(localException);
        }
    }

    public void internalUpdateSpreadsheetRowForUrl(String etfSymbol, int rowIndex, MarketIndustryRowAdaptor paramMarketIndustryRowAdaptor) {
        log.info("Updating spreadsheet values for url [" + etfSymbol + "]");

        try {
            if (StringUtil.isNullOrEmpty(etfSymbol)) {
                log.warn("url is null, skipping row, index [" + rowIndex + "]");
                return;
            }

            // Get Option from Google spreadsheet
            MarketIndustry scrappedMarketIndustry = paramMarketIndustryRowAdaptor.getMarketIndustry(etfSymbol, rowIndex);
            if (scrappedMarketIndustry == null) {
                log.warn("etf Symbol [" + etfSymbol + "] prices data cannot be obtained, skipping row, index [" + rowIndex + "]");
                writeErrorDataSpreadsheetRow(paramMarketIndustryRowAdaptor, etfSymbol, rowIndex);
                return;
            }

            log.info("found market industry  for etf symbol [" + etfSymbol + "]");

            log.info("about to write values for etf symbol [" + etfSymbol + "]");
            paramMarketIndustryRowAdaptor.writeSpreadsheetRow(rowIndex, etfSymbol, scrappedMarketIndustry);
            log.info("values written successfully for etf symbol [" + etfSymbol + "] !");
        } catch (Exception localException) {
            log.error("Spreadsheet [" + this.spreadsheetName + "] : error while trying to update value of cell row [" + rowIndex + "]", localException);
        }

    }

    protected MarketIndustryRowAdaptor getMarketIndustryRowAdaptor(int firstRow, int lastRow, Object paramObject)
            throws IOException, ServiceException {
        return new MarketIndustryRowAdaptorPrivate(firstRow, lastRow, paramObject);
    }

    private void writeErrorDataSpreadsheetRow(MarketIndustryRowAdaptor paramOptionCalcRowAdaptor, String etfSymbol, int rowIndex) throws IOException, ServiceException {
        paramOptionCalcRowAdaptor.writeSpreadsheetRow(rowIndex, etfSymbol, new MarketIndustry());
    }

    protected class MarketIndustryRowAdaptorPrivate implements MarketIndustryRowAdaptor {

        protected int firstRow;
        protected int lastRow;
        protected CellFeed etfSymbolCellFeed;
        protected CellFeed urlsCellFeed;
        protected CellFeed lastWeekPriceCellFeed;
        protected CellFeed openingMonthPriceCellFeed;

        public MarketIndustryRowAdaptorPrivate(int firstRow, int lastRow, Object arg4) throws IOException, ServiceException {
            this.firstRow = firstRow;
            this.lastRow = lastRow;
            this.etfSymbolCellFeed = LastWeekPriceUpdateSpreadsheetManager.this.getCellFeed(LastWeekPriceUpdateSpreadsheetManager.this.ETF_SYMBOL_COLUMN, firstRow, lastRow);
            this.urlsCellFeed = LastWeekPriceUpdateSpreadsheetManager.this.getCellFeed(LastWeekPriceUpdateSpreadsheetManager.this.URL_COLUMN, firstRow, lastRow);
            this.lastWeekPriceCellFeed = LastWeekPriceUpdateSpreadsheetManager.this.getCellFeed(LastWeekPriceUpdateSpreadsheetManager.this.LAST_WEEK_PRICE_COLUMN, firstRow, lastRow);
            this.openingMonthPriceCellFeed = LastWeekPriceUpdateSpreadsheetManager.this.getCellFeed(LastWeekPriceUpdateSpreadsheetManager.this.OPENING_MONTH_PRICE_COLUMN, firstRow, lastRow);
        }

        public List<String> getEtfSymbols() throws IOException, ServiceException {
            ArrayList localArrayList = new ArrayList();
            try {
                for (CellEntry localCellEntry : this.etfSymbolCellFeed.getEntries()) {
                    if ((localCellEntry != null) && (localCellEntry.getCell() != null)) {
                        localArrayList.add(localCellEntry.getCell().getValue());
                    } else {
                        localArrayList.add("");
                    }
                }
            } catch (Exception localException) {
                LastWeekPriceUpdateSpreadsheetManager.log.error("Spreadsheet [" + LastWeekPriceUpdateSpreadsheetManager.this.spreadsheetName + "] : error while retrieving urls, returning empty url list!", localException);
            }
            LastWeekPriceUpdateSpreadsheetManager.log.info("found urls list:" + localArrayList);
            return localArrayList;
        }

        public MarketIndustry getMarketIndustry(String etfSymbol, int rowIndex) {
            String url = ((CellEntry) this.urlsCellFeed.getEntries().get(rowIndex)).getCell().getValue();
            if (StringUtil.isNullOrEmpty(url)) {
                LastWeekPriceUpdateSpreadsheetManager.log.warn("MarketIndustry for etf symbol [" + etfSymbol + "] is null or empty, index [" + rowIndex + "], returning null market industry");
                return null;
            }
            MarketIndustry marketIndustry = MarketIndustryDataProvider.getMarketIndustryByUrl(etfSymbol, url);
            if (marketIndustry == null) {
                LastWeekPriceUpdateSpreadsheetManager.log.warn("unable to find market industry prices for etf symbol [" + etfSymbol + "]");
            }
            return marketIndustry;
        }

        public void writeSpreadsheetRow(int rowIndex, String etfSymbol, MarketIndustry scrappedMarketIndustry) throws IOException, ServiceException {

            // Write Opening months price for all tables
            LastWeekPriceUpdateSpreadsheetManager.this.insertOrUpdateCellValue(LastWeekPriceUpdateSpreadsheetManager.this.worksheet, this.openingMonthPriceCellFeed, rowIndex, LastWeekPriceUpdateSpreadsheetManager.this.normalizeNumericValueForSheet(scrappedMarketIndustry.getCurrentMonthsOpeningPrice()), this.firstRow, LastWeekPriceUpdateSpreadsheetManager.getColumnNumberFromColumnName(LastWeekPriceUpdateSpreadsheetManager.this.OPENING_MONTH_PRICE_COLUMN));

            // Write previous weeks end price for tables Market Indexes and Industry Sectors
            if (!spreadsheetName.equals(Configuration.getInstance().getProperty("spreadsheets.marketcap.spreadsheet.name"))) {
                LastWeekPriceUpdateSpreadsheetManager.this.insertOrUpdateCellValue(LastWeekPriceUpdateSpreadsheetManager.this.worksheet, this.lastWeekPriceCellFeed, rowIndex, LastWeekPriceUpdateSpreadsheetManager.this.normalizeNumericValueForSheet(scrappedMarketIndustry.getLastWeeksPrice()), this.firstRow, LastWeekPriceUpdateSpreadsheetManager.getColumnNumberFromColumnName(LastWeekPriceUpdateSpreadsheetManager.this.LAST_WEEK_PRICE_COLUMN));
            }

        }
    }

    static class RunnableImplementation implements Runnable {

        private final String stockTicker;
        private final int[] arrayOfInt;
        private final MarketIndustryRowAdaptor localMarketIndustryRowAdaptor;
        public LastWeekPriceUpdateSpreadsheetManager spreadsheetManager;

        public RunnableImplementation(LastWeekPriceUpdateSpreadsheetManager spreadsheetManager, String stockTicker, int[] arrayOfInt, MarketIndustryRowAdaptor localMarketIndustryRowAdaptor) {
            this.stockTicker = stockTicker;
            this.arrayOfInt = arrayOfInt;
            this.localMarketIndustryRowAdaptor = localMarketIndustryRowAdaptor;
            this.spreadsheetManager = spreadsheetManager;
        }

        public void run() {
            //BaseOptionSpreadsheetManager.this.internalUpdateSpreadsheetRowForStock(this.val$stockTicker, this.val$arr[0], this.val$rowAdaptor);
            spreadsheetManager.internalUpdateSpreadsheetRowForUrl(stockTicker, arrayOfInt[0], localMarketIndustryRowAdaptor);
        }
    }
}
