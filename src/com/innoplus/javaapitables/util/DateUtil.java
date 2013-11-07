package com.innoplus.javaapitables.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil
{
  public static final long MILLISECS_IN_DAY = 86400000L;

  public static Date getDate(int paramInt1, int paramInt2, int paramInt3)
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.clear();
    localCalendar.set(paramInt3, paramInt1, paramInt2);

    return localCalendar.getTime();
  }

  public static Date getNow()
  {
    Calendar localCalendar1 = Calendar.getInstance();
    Calendar localCalendar2 = Calendar.getInstance();
    localCalendar2.clear();
    localCalendar2.set(localCalendar1.get(1), localCalendar1.get(2), localCalendar1.get(5), localCalendar1.get(11), localCalendar1.get(12), localCalendar1.get(13));

    return localCalendar2.getTime();
  }

  public static Date getTodayPlusX(int paramInt, String paramString)
  {
    Calendar localCalendar = Calendar.getInstance();
    Date localDate = getToday();
    localCalendar.setTime(localDate);
    localCalendar.add(5, paramInt);
    return localCalendar.getTime();
  }

  public static Date getDatePlusX(Date paramDate, int paramInt)
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTime(paramDate);
    localCalendar.add(5, paramInt);
    return localCalendar.getTime();
  }

  public static Date getDatePlusTime(Date paramDate1, Date paramDate2)
  {
    Calendar localCalendar1 = Calendar.getInstance();
    localCalendar1.setTime(paramDate1);

    Calendar localCalendar2 = Calendar.getInstance();
    localCalendar2.setTime(paramDate2);

    Calendar localCalendar3 = Calendar.getInstance();
    localCalendar3.clear();
    localCalendar3.set(localCalendar1.get(1), localCalendar1.get(2), localCalendar1.get(5), localCalendar2.get(11), localCalendar2.get(12), localCalendar2.get(13));

    return localCalendar3.getTime();
  }

  public static Date getDatePlusTime(Date paramDate, int paramInt1, int paramInt2, int paramInt3)
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTime(paramDate);
    localCalendar.set(11, paramInt1);
    localCalendar.set(12, paramInt2);
    localCalendar.set(13, paramInt3);
    return localCalendar.getTime();
  }

  public static Date getToday()
  {
    Calendar localCalendar = Calendar.getInstance();
    setToBeginningOfDay(localCalendar);

    return localCalendar.getTime();
  }

  public static Calendar setToBeginningOfDay(Calendar paramCalendar)
  {
    if (paramCalendar == null) {
      return null;
    }
    paramCalendar.set(11, 0);
    paramCalendar.set(12, 0);
    paramCalendar.set(13, 0);
    paramCalendar.set(14, 0);

    return paramCalendar;
  }

  public static Date setToBeginningOfDay(Date paramDate)
  {
    if (paramDate == null) {
      return null;
    }
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTime(paramDate);
    return setToBeginningOfDay(localCalendar).getTime();
  }

  public static void moveToNearestBusinessDay(Calendar paramCalendar, boolean paramBoolean)
  {
    if (isBusinessDay(paramCalendar)) {
      return;
    }
    Calendar localCalendar = (Calendar)paramCalendar.clone();
    int i = paramCalendar.get(7);

    if (i == 1)
    {
      paramCalendar.add(5, 1);
      if (!isHoliday(paramCalendar)) {
        return;
      }

      paramCalendar.add(5, -1);
    }

    if (i == 7)
    {
      paramCalendar.add(5, -1);
      if (!isHoliday(paramCalendar)) {
        return;
      }

      paramCalendar.add(5, 1);
    }

    boolean[][] arrayOfBoolean = new boolean[4][2];

    for (int j = 0; j < arrayOfBoolean.length; j++) {
      paramCalendar.add(5, -1);
      arrayOfBoolean[j][0] = isBusinessDay(paramCalendar);
    }

    paramCalendar.add(5, arrayOfBoolean.length);

    //for (j = 0; j < arrayOfBoolean.length; j++) { //CVIT comment
    for (int j = 0; j < arrayOfBoolean.length; j++) {
      paramCalendar.add(5, 1);
      arrayOfBoolean[j][1] = isBusinessDay(paramCalendar);
    }

    paramCalendar.add(5, arrayOfBoolean.length * -1);

    //for (j = 0; j < arrayOfBoolean.length; j++) { //CVIT comment
    for (int j = 0; j < arrayOfBoolean.length; j++) {
      //int k = arrayOfBoolean[j][0]; //CVIT comment
      boolean k = arrayOfBoolean[j][0];
      //int m = arrayOfBoolean[j][1]; //CVIT comment
      boolean m = arrayOfBoolean[j][1];
      //if ((k != 0) && (m != 0)) { //CVIT comment
      if ((k != false) && (m != false)) {
        if (paramBoolean) {
          paramCalendar.add(5, j + 1);
          return;
        }
        paramCalendar.add(5, (j + 1) * -1);
        return;
      }
      //if (k != 0) { //CVIT comment
      if (k != false) {
        paramCalendar.add(5, (j + 1) * -1);
        return;
      //}if (m != 0) { //CVIT comment
      }if (m != false) {
        paramCalendar.add(5, j + 1);
        return;
      }
    }

    throw new RuntimeException("Unable to find nearest business day for [" + paramCalendar.getTime() + "]");
  }

  public static Date moveToNearestBusinessDay(Date paramDate, boolean paramBoolean)
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTime(paramDate);
    moveToNearestBusinessDay(localCalendar, paramBoolean);
    return localCalendar.getTime();
  }

  public static boolean isHoliday(Calendar paramCalendar)
  {
    int i = paramCalendar.get(2);
    int j = paramCalendar.get(5);
    if ((i == 6) && (j == 4))
    {
      return true;
    }if ((i == 11) && (j == 25))
    {
      return true;
    }

    return (i == 0) && (j == 1);
  }

  public static boolean isBusinessDay(Calendar paramCalendar)
  {
    return (!isWeekend(paramCalendar)) && (!isHoliday(paramCalendar));
  }

  public static boolean isWeekend(Calendar paramCalendar)
  {
    int i = paramCalendar.get(7);

    return (i == 7) || (i == 1);
  }

  public static long getNumOfDaysBetween(Date paramDate1, Date paramDate2)
  {
    paramDate1 = moveDateToBeginOfDay(paramDate1, null);
    paramDate2 = moveDateToBeginOfDay(paramDate2, null);
    return (paramDate2.getTime() - paramDate1.getTime()) / 86400000L;
  }

  public static Date moveDateToBeginOfDay(Date paramDate, TimeZone paramTimeZone)
  {
    Calendar localCalendar = Calendar.getInstance(paramTimeZone != null ? paramTimeZone : TimeZone.getDefault());
    localCalendar.setTime(paramDate);
    return setToBeginningOfDay(localCalendar).getTime();
  }

  public static Date moveDateToEndOfDay(Date paramDate, TimeZone paramTimeZone)
  {
    Calendar localCalendar = Calendar.getInstance(paramTimeZone != null ? paramTimeZone : TimeZone.getDefault());
    localCalendar.setTime(paramDate);
    return setToEndOfDay(localCalendar).getTime();
  }

  public static Calendar setToEndOfDay(Calendar paramCalendar)
  {
    paramCalendar.set(11, 23);
    paramCalendar.set(12, 59);
    paramCalendar.set(13, 59);
    paramCalendar.set(14, 999);

    return paramCalendar;
  }

  public static Calendar setToBeginningOfMonth(Calendar paramCalendar)
  {
    paramCalendar.set(11, 0);
    paramCalendar.set(12, 0);
    paramCalendar.set(13, 0);
    paramCalendar.set(14, 0);
    paramCalendar.set(5, 1);

    return paramCalendar;
  }

  public static void setTo3rdFridayOfTheMonth(Calendar paramCalendar)
  {
    setToBeginningOfMonth(paramCalendar);
    int i = paramCalendar.get(7);

    paramCalendar.add(5, 14);

    int j = 6 - i + (i > 6 ? 7 : 0);
    paramCalendar.add(5, j);
    setToBeginningOfDay(paramCalendar);
  }

  public static void setToSaturdayFollowing3rdFridayOfTheMonth(Calendar paramCalendar)
  {
    setTo3rdFridayOfTheMonth(paramCalendar);
    paramCalendar.add(5, 1);
  }
}