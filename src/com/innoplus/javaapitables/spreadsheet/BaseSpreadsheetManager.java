package com.innoplus.javaapitables.spreadsheet;

import com.google.gdata.client.spreadsheet.CellQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.TextConstruct;
import com.google.gdata.data.spreadsheet.Cell;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.PreconditionFailedException;
import com.google.gdata.util.ServiceException;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class BaseSpreadsheetManager
{
  private static Log log = LogFactory.getLog(BaseSpreadsheetManager.class);
  public static final String DATE_FORMAT = "MM/dd/yyyy";
  protected String spreadsheetName;
  protected SpreadsheetEntry spreadsheet;
  protected WorksheetEntry worksheet;

  protected static int getColumnNumberFromColumnName(String paramString)
  {
    return paramString.charAt(0) - '@';
  }

  protected abstract String getSpreadsheetName();

  protected abstract void initializeSettings();

  protected BaseSpreadsheetManager() throws RuntimeException
  {
    this.spreadsheetName = getSpreadsheetName();
    init();
  }

  protected BaseSpreadsheetManager(String paramString) throws RuntimeException {
    this.spreadsheetName = paramString;
    init();
  }

  private void init() {
    log.info("Initializing [" + getClass().getName() + "]. Using spreadsheet name [" + this.spreadsheetName + "].");

    initializeSettings();

    openConnectionToSpreadsheetAndFirstWorkSheet();
  }

  protected static Object getSyncLockForSheet(String paramString) {
    return "__#__" + paramString + "__#__";
  }

  protected void openConnectionToSpreadsheetAndFirstWorkSheet() {
    log.info("Authorization and Open spreadsheet for [" + this.spreadsheetName + "]");

    SpreadsheetService localSpreadsheetService = SpreadsheetServiceWrapper.getInstance().getSpreadsheetService();
    try
    {
      URL localURL = new URL("http://spreadsheets.google.com/feeds/spreadsheets/private/full");
      SpreadsheetFeed localSpreadsheetFeed = (SpreadsheetFeed)localSpreadsheetService.getFeed(localURL, SpreadsheetFeed.class);
      List localList = localSpreadsheetFeed.getEntries();
      for (Object localObject = localList.iterator(); ((Iterator)localObject).hasNext(); ) { SpreadsheetEntry localSpreadsheetEntry = (SpreadsheetEntry)((Iterator)localObject).next();
        if (localSpreadsheetEntry.getTitle().getPlainText().equals(this.spreadsheetName)) {
          this.spreadsheet = localSpreadsheetEntry;
        }
      }
      if (this.spreadsheet == null) {
        throw new IllegalStateException("Spreadsheet with name [" + this.spreadsheetName + "] not found!");
      }

      List<WorksheetEntry> localObject = this.spreadsheet.getWorksheets();
      this.worksheet = ((WorksheetEntry)((List)localObject).get(0));
    }
    catch (Exception localException) {
      log.error("Error while obtaining Spreadsheet Entry object for spreadsheet [" + this.spreadsheetName + "]!", localException);
      throw new RuntimeException(localException);
    }
  }

  protected void insertOrUpdateCellValue(WorksheetEntry worksheetEntry, CellFeed paramCellFeed, int rowIndex, String newCellValue, int firstRow, int columnNumber)
    throws IOException, ServiceException
  {
    CellEntry localCellEntry = null;
    try
    {
      localCellEntry = (CellEntry)paramCellFeed.getEntries().get(rowIndex);
      localCellEntry.changeInputValueLocal(newCellValue);
      localCellEntry.update();
    }
    catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
    {
      insertNewCellIntoWorksheet(worksheetEntry, rowIndex + firstRow, columnNumber, newCellValue);
    }
    catch (PreconditionFailedException localPreconditionFailedException)
    {
      log.warn("Spreadsheet [" + this.spreadsheetName + "] : Exception while trying to update a cell  " + (localCellEntry != null ? "at row [" + localCellEntry.getCell().getRow() + "], col [" + localCellEntry.getCell().getCol() + "]," : "(unknown)") + "cellEntryIndex [" + rowIndex + "], columnNumber [" + columnNumber + "]" + " with value [" + newCellValue + "].", localPreconditionFailedException);
    }
    catch (Exception localException) {
      log.warn("Spreadsheet [" + this.spreadsheetName + "] : Exception while trying to update a cell " + "cellEntryIndex [" + rowIndex + "], columnNumber [" + columnNumber + "]" + " : " + localException.getMessage());
    }
  }

  protected void insertNewCellIntoWorksheet(WorksheetEntry paramWorksheetEntry, int paramInt1, int paramInt2, String paramString) throws IOException, ServiceException
  {
    try {
      CellEntry localCellEntry = new CellEntry(paramInt1, paramInt2, paramString);
      SpreadsheetServiceWrapper.getInstance().getSpreadsheetService().insert(paramWorksheetEntry.getCellFeedUrl(), localCellEntry);
    }
    catch (Exception localException) {
      log.warn("Spreadsheet [" + this.spreadsheetName + "] : Exception while trying to insert a cell at row [" + paramInt1 + "], col [" + paramInt2 + "], with value [" + paramString + "]: " + localException.getMessage());
    }
  }

  protected CellFeed getCellFeed(String paramString1, int paramInt, String paramString2, String paramString3)
    throws IOException, ServiceException
  {
    return getCellFeed(paramString1 + paramInt + ":" + paramString2 + paramString3);
  }

  protected CellFeed getCellFeed(String queryRange)
    throws IOException, ServiceException
  {
    URL localURL = this.worksheet.getCellFeedUrl();

    CellQuery cellQuery = new CellQuery(localURL);
    cellQuery.setReturnEmpty(true);
    cellQuery.setRange(queryRange);

    return (CellFeed)SpreadsheetServiceWrapper.getInstance().getSpreadsheetService().query(cellQuery, CellFeed.class);
  }

  protected CellFeed getCellFeed(String columnName, int firstRow, int lastRow) throws IOException, ServiceException {
    return getCellFeed(columnName + firstRow + ":" + columnName + lastRow);
  }

  protected String normalizeValueForSheet(Object paramObject) {
    if (paramObject == null) {
      return "N/A";
    }
    return paramObject.toString();
  }

  protected String normalizeNumericValueForSheet(Object paramObject) {
    if (paramObject == null) {
      return "0.00";
    }
    return paramObject.toString();
  }

  protected String normalizeDateValueForSheet(Date paramDate) {
    if (paramDate == null) {
      return "N/A";
    }

    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
    return localSimpleDateFormat.format(paramDate);
  }
}