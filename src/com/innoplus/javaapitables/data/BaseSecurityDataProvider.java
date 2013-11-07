package com.innoplus.javaapitables.data;

public abstract class BaseSecurityDataProvider
{
  public static String normalizeTicker(String paramString)
  {
    return paramString.trim().toUpperCase();
  }

  protected static Object getSyncLockForTicker(String paramString) {
    return "__#__" + paramString + "__#__";
  }
}