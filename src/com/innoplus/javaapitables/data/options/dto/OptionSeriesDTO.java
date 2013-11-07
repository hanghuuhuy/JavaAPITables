package com.innoplus.javaapitables.data.options.dto;

import java.util.ArrayList;
import java.util.List;
import com.innoplus.javaapitables.domain.Option;

public class OptionSeriesDTO
{
  private Option.Type seriesType;
  private List<Option> options = new ArrayList();

  public Option.Type getSeriesType() {
    return this.seriesType;
  }

  public void setSeriesType(Option.Type paramType) {
    this.seriesType = paramType;
  }

  public List<Option> getOptions() {
    return this.options;
  }

  public void setOptions(List<Option> paramList) {
    this.options = paramList;
  }
}