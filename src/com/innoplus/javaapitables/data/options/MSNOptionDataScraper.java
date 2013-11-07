package com.innoplus.javaapitables.data.options;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.variables.Variable;
import com.innoplus.javaapitables.data.options.dto.OptionExpDateDTO;
import com.innoplus.javaapitables.data.options.dto.OptionSeriesDTO;
import com.innoplus.javaapitables.domain.Option;
import java.util.Calendar;
import java.util.TimeZone;
import org.xml.sax.InputSource;

public class MSNOptionDataScraper
        implements OptionsDataScraper {

    private static Log log = LogFactory.getLog(MSNOptionDataScraper.class);
    Map<String, String> monthMap = new HashMap() {
    };

    public Map<String, List<OptionExpDateDTO>> scrapeOptionsDataForStockTickers(List<String> paramList) {
        HashMap localHashMap = new HashMap();
        try {
            for (String str : paramList) {
                List localList = createOptionExpDateObjects(str);
                if (localList.size() > 0) {
                    localHashMap.put(str, localList);
                }

            }

        } catch (Exception localException) {
        }

        return localHashMap;
    }

    public Option scrapeOptionData(String optionSymbol) {
        Option localOption = null;
        Option.Type localType = null;

        MonthYear localMonthYear = null;
        try {
            // CVIT
            //ScraperConfiguration localScraperConfiguration = new ScraperConfiguration(ClassLoader.getSystemClassLoader().getResource("msn_single_option.xml"));
            //ScraperConfiguration localScraperConfiguration = new ScraperConfiguration("D:/web-harvest/msn_single_option.xml");
            ScraperConfiguration localScraperConfiguration = new ScraperConfiguration(new InputSource(MSNOptionDataScraper.class.getResourceAsStream("/com/innoplus/javaapitables/resources/msn_single_option.xml")));

            Scraper localScraper = new Scraper(localScraperConfiguration, ".");
            localScraper.getLogger().setLevel(Level.WARN);
            localScraper.addVariableToContext("symbol", ".".concat(optionSymbol));
            localScraper.execute();
            
//            Object page = localScraper.getContext().get("page");
//            System.out.println(page);
            
            Variable scrappedBid = (Variable) localScraper.getContext().get("bid");
            Variable scrappedAsk = (Variable) localScraper.getContext().get("ask");
            Variable scrappedStrikePrice = (Variable) localScraper.getContext().get("strike_price");
            Variable scrappedExpDate = (Variable) localScraper.getContext().get("expDate");
            Variable scrappedTitle = (Variable) localScraper.getContext().get("title");

            log.info("Scrapped bid for option ticker [" + optionSymbol + "]: " + scrappedBid.toString());
            log.info("Scrapped ask for option ticker [" + optionSymbol + "]: " + scrappedAsk.toString());
            log.info("Scrapped strike_price for option ticker [" + optionSymbol + "]: " + scrappedStrikePrice.toString());
            log.info("Scrapped expDate for option ticker [" + optionSymbol + "]: " + scrappedExpDate.toString());
            log.info("Scrapped title for option ticker [" + optionSymbol + "]: " + scrappedTitle.toString());

            if (!scrappedStrikePrice.toString().isEmpty()) {
                if (scrappedExpDate != null) {
                    Date localDate = getExpDateFromDateString(scrappedExpDate.toString());
                    localMonthYear = createMonthYear(localDate);
                }

                if (scrappedTitle != null) {
                    if (isCall(scrappedTitle.toString())) {
                        localType = Option.Type.CALL;
                    }

                    if (isPut(scrappedTitle.toString())) {
                        localType = Option.Type.PUT;
                    }
                }

                localOption = createOption(localMonthYear, localType, scrappedStrikePrice.toString(), optionSymbol, scrappedBid.toString(), scrappedAsk.toString());
                OptionSeriesDTO localOptionSeriesDTO = new OptionSeriesDTO();
                localOptionSeriesDTO.setSeriesType(localType);

                localOptionSeriesDTO.getOptions().add(localOption);
            } else {
                log.warn("Scraper did not find data for option ticker [" + optionSymbol + "]");
            }
        } catch (Exception localException) {
            log.error("Error while scraping for option ticker [" + optionSymbol + "]", localException);
        }
        return localOption;
    }

    public List<String> scrapeOptionExpDates(String paramString) {
        ArrayList localArrayList = new ArrayList();

        List<MonthYear> localList = scrapeExpMonthAndYear(paramString);
        for (MonthYear localMonthYear : localList) {
            localArrayList.add(localMonthYear.getConCatMonthYear());
        }
        return localArrayList;
    }

    protected List<OptionExpDateDTO> createOptionExpDateObjects(String paramString)
            throws Exception {
        ArrayList localArrayList = new ArrayList();
        List<MonthYear> localList = scrapeExpMonthAndYear(paramString);
        for (MonthYear localMonthYear : localList) {
            OptionExpDateDTO localOptionExpDateDTO = createOptionExpDate(paramString, localMonthYear);
            if (localOptionExpDateDTO != null) {
                localArrayList.add(localOptionExpDateDTO);
            }
        }

        return localArrayList;
    }

    public OptionExpDateDTO scrapeOptionData(String paramString1, String paramString2) {
        try {
            MonthYear localMonthYear = new MonthYear(paramString2.substring(0, 3) + " " + paramString2.substring(3, paramString2.length()));
            return createOptionExpDate(paramString1, localMonthYear);
        } catch (Exception localException) {
            log.error("Error while scraping for options for stock ticker [" + paramString1 + "] with  ExpMonthYear [" + paramString2 + "]", localException);
        }

        return null;
    }

    protected OptionExpDateDTO createOptionExpDate(String paramString, MonthYear paramMonthYear) throws Exception {
        OptionExpDateDTO localOptionExpDateDTO = new OptionExpDateDTO();
        localOptionExpDateDTO.setExpMonthYear(paramMonthYear.getConCatMonthYear());
        localOptionExpDateDTO.setExpirationDate(scrapeOPtionExpDate(paramString, paramMonthYear));
        List localList = createOptionSeriesList(paramString, paramMonthYear);
        if (localList.size() > 0) {
            localOptionExpDateDTO.setOptionsSeriesList(localList);
        }
        return localOptionExpDateDTO;
    }

    protected List<OptionSeriesDTO> createOptionSeriesList(String paramString, MonthYear paramMonthYear) throws Exception {
        ArrayList localArrayList = new ArrayList();
        OptionSeriesDTO localOptionSeriesDTO1 = new OptionSeriesDTO();
        OptionSeriesDTO localOptionSeriesDTO2 = new OptionSeriesDTO();

        int i = 0;
        int j = 0;
        int k = 0;
        int m = 0;
        int n = 0;

        Scraper localScraper = scrapeOptions(paramString, paramMonthYear);

        Variable localVariable = (Variable) localScraper.getContext().get("option_list");

        i = localVariable.toList().size();

        if (i > 4) {
            j = 2;
            k = i / 2 - 1;
            m = i / 2 + 2;
            n = i - 1;
            localOptionSeriesDTO1 = createOptionSeries(paramString, paramMonthYear, localScraper, Option.Type.CALL, j, k);
            localOptionSeriesDTO2 = createOptionSeries(paramString, paramMonthYear, localScraper, Option.Type.PUT, m, n);
            localArrayList.add(localOptionSeriesDTO1);
            localArrayList.add(localOptionSeriesDTO2);
        } else {
            log.warn("Scraper did not find options data for stock ticker [" + paramString + "]");
        }

        return localArrayList;
    }

    protected OptionSeriesDTO createOptionSeries(String paramString, MonthYear paramMonthYear, Scraper paramScraper, Option.Type paramType, int paramInt1, int paramInt2) {
        OptionSeriesDTO localOptionSeriesDTO = new OptionSeriesDTO();
        ArrayList localArrayList = new ArrayList();
        localOptionSeriesDTO.setSeriesType(paramType);

        for (int i = paramInt1; i < paramInt2 + 2; i++) {
            String str1 = "strike_price_" + i;
            String str2 = "symbol_" + i;
            String str3 = "bid_" + i;
            String str4 = "ask_" + i;
            Variable localVariable1 = (Variable) paramScraper.getContext().get(str1);
            Variable localVariable2 = (Variable) paramScraper.getContext().get(str2);
            Variable localVariable3 = (Variable) paramScraper.getContext().get(str3);
            Variable localVariable4 = (Variable) paramScraper.getContext().get(str4);
            if (localVariable2.isEmpty()) {
                continue;
            }

            Option localOption = createOption(paramMonthYear, paramType, localVariable1.toString(), localVariable2.toString(), localVariable3.toString(), localVariable4.toString());

            localOptionSeriesDTO.getOptions().add(localOption);
            localArrayList.add(localOption);
        }

        localOptionSeriesDTO.setOptions(localArrayList);
        return localOptionSeriesDTO;
    }

    protected Scraper scrapeOptions(String paramString, MonthYear paramMonthYear)
            throws Exception {
        Scraper localScraper = null;
        try {
            ScraperConfiguration localScraperConfiguration = new ScraperConfiguration(ClassLoader.getSystemClassLoader().getResource("msn_options.xml"));

            localScraper = new Scraper(localScraperConfiguration, ".");
            localScraper.addVariableToContext("symbol", paramString);
            localScraper.addVariableToContext("month", paramMonthYear.getMonth());
            localScraper.addVariableToContext("year", paramMonthYear.getYear());
            localScraper.execute();
            Variable localVariable = (Variable) localScraper.getContext().get("option_list");
        } catch (Exception localException) {
            log.error("Error while scraping for options for stock ticker [" + paramString + "]", localException);
        }

        return localScraper;
    }

    protected Date scrapeOPtionExpDate(String paramString, MonthYear paramMonthYear) {
        Date localDate = null;
        String str1 = null;
        try {
            ScraperConfiguration localScraperConfiguration = new ScraperConfiguration(ClassLoader.getSystemClassLoader().getResource("msn_option_exp_dates.xml"));

            Scraper localScraper = new Scraper(localScraperConfiguration, ".");
            localScraper.getLogger().setLevel(Level.WARN);
            localScraper.addVariableToContext("symbol", paramString);
            localScraper.addVariableToContext("month", paramMonthYear.getMonth());
            localScraper.addVariableToContext("year", paramMonthYear.getYear());
            localScraper.execute();
            Variable localVariable = (Variable) localScraper.getContext().get("expdate_list");

            if (localVariable.toList().size() > 0) {
                str1 = ((Variable) localScraper.getContext().get("expdate_1")).toString();
                String str2 = str1.substring(0, str1.lastIndexOf(" "));

                localDate = getExpDateFromDateString(str2);
            } else {
                log.warn("Scraper did not find  option exp date  for stock ticker [" + paramString + "]");
            }
        } catch (Exception localException) {
            log.error("Error while scraping for option exp date  for stock ticker [" + paramString + "]", localException);
        }
        return localDate;
    }

    protected List<MonthYear> scrapeExpMonthAndYear(String paramString) {
        int i = 0;
        ArrayList localArrayList = new ArrayList();
        try {
            ScraperConfiguration localScraperConfiguration = new ScraperConfiguration(ClassLoader.getSystemClassLoader().getResource("msn_option_exp_month_year.xml"));

            Scraper localScraper = new Scraper(localScraperConfiguration, ".");
            localScraper.getLogger().setLevel(Level.WARN);
            localScraper.addVariableToContext("symbol", paramString);

            localScraper.execute();

            Variable localVariable1 = (Variable) localScraper.getContext().get("expdate_list");

            if ((localVariable1 != null) && (localVariable1.toList().size() > 0)) {
                i = localVariable1.toList().size();
            } else {
                log.warn("Scraper did not find  option exp month and year for stock tickerr [" + paramString + "]");
            }

            for (int j = 1; j < i + 1; j++) {
                String str = "expdate_" + j;
                Variable localVariable2 = (Variable) localScraper.getContext().get(str);

                localArrayList.add(new MonthYear(localVariable2.toString()));
            }

        } catch (Exception localException) {
            log.error("Error while scraping for option exp month and year for stock ticker [" + paramString + "]", localException);
        }

        return localArrayList;
    }

    protected Option createOption(MonthYear paramMonthYear, Option.Type paramType, String paramString1, String paramString2, String paramString3, String paramString4) {
        Option localOption = new Option();
        localOption.setExpMonthYear(paramMonthYear.getConCatMonthYear());
        localOption.setTicker(paramString2);
        localOption.setType(paramType);
        try {
            localOption.setStrikePrice(Double.valueOf(Double.parseDouble(paramString1)));
        } catch (Exception localException1) {
        }
        if (paramString4.equals("NA")) {
            localOption.setPriceAsk(null);
        } else {
            try {
                localOption.setPriceAsk(new BigDecimal(paramString4));
            } catch (Exception localException2) {
            }
        }
        if (paramString3.equals("NA")) {
            localOption.setPriceBid(null);
        } else {
            try {
                localOption.setPriceBid(new BigDecimal(paramString3));
            } catch (Exception localException3) {
            }
        }

        return localOption;
    }

    protected Date getExpDateFromDateString(String paramString) {
        String str = "MMMMM dd, yyyy";
        Date localDate = null;

        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(str, Locale.ENGLISH);
        try {
            localDate = localSimpleDateFormat.parse(paramString);
        } catch (Exception localException) {
            // TODO Error parsing date
        }

        return localDate;
    }

    protected boolean isCall(String paramString) {
        return paramString.toLowerCase().contains("call");
    }

    protected boolean isPut(String paramString) {
        return paramString.toLowerCase().contains("put");
    }

    protected MonthYear createMonthYear(Date paramDate) {
        MonthYear localMonthYear = new MonthYear(new SimpleDateFormat("MMM", Locale.ENGLISH).format(paramDate) + " " + new SimpleDateFormat("yyyy", Locale.ENGLISH).format(paramDate));

        return localMonthYear;
    }

    public HashMap<String, String> scrapEarningsSurprises(String optionSymbol) {
        HashMap<String, String> earningsSurprisesMap = new HashMap<String, String>();

        try {
            // CVIT
            //ScraperConfiguration localScraperConfiguration = new ScraperConfiguration(ClassLoader.getSystemClassLoader().getResource("msn_single_option.xml"));
            //ScraperConfiguration localScraperConfiguration = new ScraperConfiguration("D:/web-harvest/msn_single_option.xml");
            ScraperConfiguration localScraperConfiguration = new ScraperConfiguration(new InputSource(MSNOptionDataScraper.class.getResourceAsStream("/com/innoplus/javaapitables/resources/yahoo_earning_surprises.xml")));

            Scraper localScraper = new Scraper(localScraperConfiguration, ".");
            localScraper.getLogger().setLevel(Level.WARN);
            localScraper.addVariableToContext("symbol", optionSymbol);
            localScraper.execute();
            Variable date1 = (Variable) localScraper.getContext().get("date1");
            Variable date2 = (Variable) localScraper.getContext().get("date2");
            Variable date3 = (Variable) localScraper.getContext().get("date3");
            Variable date4 = (Variable) localScraper.getContext().get("date4");
            Variable surprise1 = (Variable) localScraper.getContext().get("surprise1");
            Variable surprise2 = (Variable) localScraper.getContext().get("surprise2");
            Variable surprise3 = (Variable) localScraper.getContext().get("surprise3");
            Variable surprise4 = (Variable) localScraper.getContext().get("surprise4");

            Variable epsGrowthThisYear = (Variable) localScraper.getContext().get("growth_est_this_year");
            Variable epsGrowthNextYear = (Variable) localScraper.getContext().get("growth_est_next_year");

            log.info("Scrapped date1 for option ticker [" + optionSymbol + "]: " + date1.toString());
            log.info("Scrapped date2 for option ticker [" + optionSymbol + "]: " + date2.toString());
            log.info("Scrapped date3 for option ticker [" + optionSymbol + "]: " + date3.toString());
            log.info("Scrapped date4 for option ticker [" + optionSymbol + "]: " + date4.toString());
            log.info("Scrapped surprise1 for option ticker [" + optionSymbol + "]: " + surprise1.toString());
            log.info("Scrapped surprise2 for option ticker [" + optionSymbol + "]: " + surprise2.toString());
            log.info("Scrapped surprise3 for option ticker [" + optionSymbol + "]: " + surprise3.toString());
            log.info("Scrapped surprise4 for option ticker [" + optionSymbol + "]: " + surprise4.toString());

            log.info("Scrapped epsGrowthThisYear for option ticker [" + optionSymbol + "]: " + epsGrowthThisYear.toString());
            log.info("Scrapped epsGrowthNextYear for option ticker [" + optionSymbol + "]: " + epsGrowthNextYear.toString());

            earningsSurprisesMap.put("date1", date1.toString());
            earningsSurprisesMap.put("date2", date2.toString());
            earningsSurprisesMap.put("date3", date3.toString());
            earningsSurprisesMap.put("date4", date4.toString());

            earningsSurprisesMap.put("surprise1", surprise1.toString());
            earningsSurprisesMap.put("surprise2", surprise2.toString());
            earningsSurprisesMap.put("surprise3", surprise3.toString());
            earningsSurprisesMap.put("surprise4", surprise4.toString());

            earningsSurprisesMap.put("epsGrowthThisYear", epsGrowthThisYear.toString());
            earningsSurprisesMap.put("epsGrowthNextYear", epsGrowthNextYear.toString());
        } catch (Exception localException) {
            log.error("Error while scraping earning surprises for option ticker [" + optionSymbol + "]", localException);
        }

        return earningsSurprisesMap;
    }

    public HashMap<String, String> scrapLastDividend(String optionSymbol) {
        HashMap<String, String> lastDividendMap = new HashMap<String, String>();
        Calendar localCalendar = Calendar.getInstance(TimeZone.getTimeZone("US/Eastern"), Locale.ENGLISH);

        try {
            // CVIT
            //ScraperConfiguration localScraperConfiguration = new ScraperConfiguration(ClassLoader.getSystemClassLoader().getResource("msn_single_option.xml"));
            //ScraperConfiguration localScraperConfiguration = new ScraperConfiguration("D:/web-harvest/msn_single_option.xml");
            ScraperConfiguration localScraperConfiguration = new ScraperConfiguration(new InputSource(MSNOptionDataScraper.class.getResourceAsStream("/com/innoplus/javaapitables/resources/yahoo_dividends.xml")));

            Scraper localScraper = new Scraper(localScraperConfiguration, ".");
            localScraper.getLogger().setLevel(Level.WARN);
            localScraper.addVariableToContext("symbol", optionSymbol);
            localScraper.addVariableToContext("from_month", "04");
            localScraper.addVariableToContext("from_day", "4");
            localScraper.addVariableToContext("from_year", "1999");
            localScraper.addVariableToContext("to_month", localCalendar.get(Calendar.MONTH));
            localScraper.addVariableToContext("to_day", localCalendar.get(Calendar.DAY_OF_MONTH));
            localScraper.addVariableToContext("to_year", localCalendar.get(Calendar.YEAR));
            localScraper.execute();
            Variable dividendDate = (Variable) localScraper.getContext().get("last_dividend_date");
            Variable dividend = (Variable) localScraper.getContext().get("last_dividend_value");

            log.info("Scrapped dividendDate for option ticker [" + optionSymbol + "]: " + dividendDate.toString());
            log.info("Scrapped dividend for option ticker [" + optionSymbol + "]: " + dividend.toString());


            lastDividendMap.put("dividendDate", dividendDate.toString());
            lastDividendMap.put("dividend", dividend.toString());
        } catch (Exception localException) {
            log.error("Error while scraping dividend for option ticker [" + optionSymbol + "]", localException);
        }

        return lastDividendMap;
    }

    class MonthYear {

        String monthYearString;
        String year;
        String month;
        String monthStr;

        public MonthYear(String arg2) {
            this.monthYearString = arg2;
        }

        public String getMonth() {
            return (String) MSNOptionDataScraper.this.monthMap.get(this.monthYearString.substring(0, this.monthYearString.indexOf(" ")));
        }

        public String getYear() {
            return this.monthYearString.substring(this.monthYearString.indexOf(" ") + 1);
        }

        public String getConCatMonthYear() {
            return this.monthYearString.replace(" ", "");
        }
    }
    
}
