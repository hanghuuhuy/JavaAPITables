package com.innoplus.javaapitables.data.options.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.innoplus.javaapitables.domain.Option;

public class OptionExpDateDTO
{
  private String expMonthYear;
  private Date expirationDate;
  private List<OptionSeriesDTO> optionsSeriesList = new ArrayList();

  public Option findFirstOptionAbovePrice(Double paramDouble, Option.Type paramType)
  {
    int i = getSeriesTypeIndex(paramType);
    if ((this.optionsSeriesList == null) || (this.optionsSeriesList.get(i) == null) || (((OptionSeriesDTO)this.optionsSeriesList.get(i)).getOptions() == null))
      return null;
    for (Option localOption : ((OptionSeriesDTO)this.optionsSeriesList.get(i)).getOptions()) {
      if (localOption.getStrikePrice().doubleValue() >= paramDouble.doubleValue())
        return localOption;
    }
    return null;
  }

  public Option findFirstOptionBelowPrice(Double paramDouble, Option.Type paramType)
  {
    int i = getSeriesTypeIndex(paramType);
    if ((this.optionsSeriesList == null) || (this.optionsSeriesList.get(i) == null) || (((OptionSeriesDTO)this.optionsSeriesList.get(i)).getOptions() == null))
      return null;
    Option localObject = null;
    for (Option localOption : ((OptionSeriesDTO)this.optionsSeriesList.get(i)).getOptions()) {
      if (localOption.getStrikePrice().doubleValue() > paramDouble.doubleValue()) {
        return localObject;
      }
      localObject = localOption;
    }
    return null;
  }

  public int getSeriesTypeIndex(Option.Type paramType)
  {
    return Option.Type.CALL.equals(paramType) ? 0 : 1;
  }

  public List<OptionSeriesDTO> getOptionsSeriesList() {
    return this.optionsSeriesList;
  }

  public void setOptionsSeriesList(List<OptionSeriesDTO> paramList) {
    this.optionsSeriesList = paramList;
  }

  public Date getExpirationDate() {
    return this.expirationDate;
  }

  public void setExpirationDate(Date paramDate) {
    this.expirationDate = paramDate;
  }

  public String getExpMonthYear() {
    return this.expMonthYear;
  }

  public void setExpMonthYear(String paramString) {
    this.expMonthYear = paramString;
  }
}