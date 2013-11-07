package com.innoplus.javaapitables.domain;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.innoplus.javaapitables.util.DateUtil;
import com.innoplus.javaapitables.util.StringUtil;

public class Option extends PersistentEntity {

    private static Log log = LogFactory.getLog(Option.class);
    private String ticker;
    //private Stock stock;
    private Double strikePrice;
    private Type type;
    private BigDecimal priceBid;
    private BigDecimal priceAsk;
    private String expMonthYear;

    public Option() {
    }

    public Option(String paramString1, Double paramDouble, Type paramType, String paramString2) {
        this.ticker = paramString1;
        this.strikePrice = paramDouble;
        this.type = paramType;
        this.expMonthYear = paramString2;
    }

    @Override
    public String toString() {
        return "Option: id=" + this.id + "; ticker=" + this.ticker + "; type=" + this.type + "; strikePrice=" + this.strikePrice + "; expMonthYear=" + this.expMonthYear + "; priceBid=" + this.priceBid + "; priceAsk=" + this.priceAsk + ";";
    }

    public Date getExpDate() {
        if (StringUtil.isNullOrEmpty(getExpMonthYear())) {
            return null;
        }
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("MMMyyyy", Locale.ENGLISH);
        try {
            // Must be English Locale
            Date localDate = localSimpleDateFormat.parse(this.expMonthYear);
            Calendar localCalendar = Calendar.getInstance();
            localCalendar.setTime(localDate);
            DateUtil.setToBeginningOfMonth(localCalendar);
            DateUtil.setToSaturdayFollowing3rdFridayOfTheMonth(localCalendar);
            return localCalendar.getTime();
        } catch (Exception localException) {
            log.error("Error while calculating exp date from expMonthYear [" + getExpMonthYear() + "] for option [" + getTicker() + "]", localException);
        }
        return null;
    }

    public String getTicker() {
        return this.ticker;
    }

    public void setTicker(String paramString) {
        this.ticker = paramString;
    }

    public Double getStrikePrice() {
        return this.strikePrice;
    }

    public void setStrikePrice(Double paramDouble) {
        this.strikePrice = paramDouble;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type paramType) {
        this.type = paramType;
    }

    public BigDecimal getPriceBid() {
        return this.priceBid;
    }

    public void setPriceBid(BigDecimal paramBigDecimal) {
        this.priceBid = paramBigDecimal;
    }

    public BigDecimal getPriceAsk() {
        return this.priceAsk;
    }

    public void setPriceAsk(BigDecimal paramBigDecimal) {
        this.priceAsk = paramBigDecimal;
    }

    public String getExpMonthYear() {
        return this.expMonthYear;
    }

    public void setExpMonthYear(String paramString) {
        this.expMonthYear = paramString;
    }
    /*
    public Stock getStock() {
    return this.stock;
    }

    public void setStock(Stock paramStock) {
    this.stock = paramStock;
    }
     *
     */

    public static enum Type {

        CALL, PUT;
    }
}
