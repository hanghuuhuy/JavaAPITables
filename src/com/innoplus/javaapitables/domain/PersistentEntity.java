package com.innoplus.javaapitables.domain;

import java.io.Serializable;
import java.math.RoundingMode;

public abstract class PersistentEntity
  implements Serializable
{
  public static final int CALCULATION_DIVIDE_SCALE = 10;
  public static final RoundingMode CALCULATION_ROUNDING_MODE = RoundingMode.HALF_UP;
  public static final long UNSAVED_ID = -1L;
  protected Long id = new Long(-1L);

  public boolean isPersisted() {
    return (getId() != null) && (!getId().equals(Long.valueOf(-1L)));
  }

  public String toString() {
    String str = getClass().getName();
    str = str.substring(str.lastIndexOf('.') + 1);
    StringBuffer localStringBuffer = new StringBuffer(str);
    localStringBuffer = localStringBuffer.append(": id=").append(this.id).append("; ");
    return localStringBuffer.toString();
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long paramLong) {
    this.id = paramLong;
  }
}