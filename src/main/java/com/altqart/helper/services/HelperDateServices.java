package com.altqart.helper.services;

import java.util.Calendar;
import java.util.Date;


public interface HelperDateServices {

	public Calendar getToDayCalenderDate();

	public Calendar getCalenderByDate(Date date);

	public String getSimpleDateString(Date date);

	public Date getWeekFirstDate(Date date);

	public Date getWeekLastDate(Date date);

	public Date getStringCalenderByDate(String string);

	public Date getStringCalenderByDateAndTime(String strDate);

	public Date getStringCalenderByDateAndTimeIn(String string);

	public Date getStringCalenderByDateAndTimeOut(String string);

	public int getMonthWeekByDayCount(Date date, int hDay);

	public int getMonthSize(Date date);

	public Date getNextMonth(Date prevDate);

	public int getMonthWeekByDayCount(Date date);

	public String getStartAndEndDateString(Date date);

	public boolean isEqualDateOnlyMonthAndYear(Date date, Date date2);

	public Date getDateUsingDay(Date date, int i);

	public double getDefHoursBetweenTowDate(Date start, Date end);

	public boolean isEqualDate(Date date, Date date2);

	public boolean isEqualDateOrGreater(Date date, Date date2);

	public Date getNextDate(Date date);

	public boolean isPrevDay(Date date);

	public boolean isGreater(Date date, Date date2);

	public boolean isToDay(Date date);

	public boolean isGreaterOrEqual(Date first, Date secend);

	public boolean isEqualOrAfterDate(Date thisDate, Date testDate);

	public boolean isEqualOrBeforDate(Date thisDate, Date testDate);

}
