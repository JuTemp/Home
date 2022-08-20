package com.JuTemp.Home.util;

public class CurrentTimeJuTemp
{
	public static String getCurrentTimeSecond()
	{
		return String.valueOf(System.currentTimeMillis() / 1000);
	}
	public static String getCurrentTimeMilliSecond()
	{
		return String.valueOf(System.currentTimeMillis());
	}
}
