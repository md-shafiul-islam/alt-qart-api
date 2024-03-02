package com.altqart.helper.services.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperConverterServices;
import com.altqart.helper.services.HelperDateServices;
import com.altqart.helper.services.HelperServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HelperDateServicesImpl implements HelperDateServices {

	@Autowired
	private HelperServices helperServices;

	@Autowired
	private HelperConverterServices converterServices;

	@Override
	public Calendar getToDayCalenderDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		return calendar;
	}

	@Override
	public Calendar getCalenderByDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		if (date != null) {

			calendar.setTime(date);
		} else {

			calendar.setTime(new Date());
		}

		return calendar;
	}

	@Override
	public String getSimpleDateString(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return format.format(date);
	}

	@Override
	public Date getWeekFirstDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		cal.setFirstDayOfWeek(Calendar.FRIDAY);
		// get start of this week in milliseconds
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

		return cal.getTime();
	}

	@Override
	public Date getWeekLastDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);

		cal.setFirstDayOfWeek(Calendar.FRIDAY);
		// get start of this week in milliseconds
		cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());

		// start of the next week
		cal.add(Calendar.WEEK_OF_YEAR, 1);

		cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 1);

		return cal.getTime();
	}

	/**
	 * @param strDate dd/MM/yyyy
	 */

	@Override
	public Date getStringCalenderByDate(String strDate) {

		if (strDate != null && strDate != "") {

			try {
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				return formatter.parse(strDate);
			} catch (ParseException e) {

				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * @param strDate dd/MM/yyyy
	 */

	@Override
	public Date getStringCalenderByDateAndTime(String strDate) {

		if (strDate != null && strDate != "") {

			try {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				return formatter.parse(strDate);
			} catch (ParseException e) {

				e.printStackTrace();
			}
		}

		return null;
	}

	@Override
	public Date getStringCalenderByDateAndTimeIn(String string) {
		Date date = getStringCalenderByDate(string);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR, 8);
		calendar.set(Calendar.AM_PM, Calendar.AM);
		return calendar.getTime();
	}

	@Override
	public Date getStringCalenderByDateAndTimeOut(String string) {
		Date date = getStringCalenderByDate(string);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR, 5);
		calendar.set(Calendar.AM_PM, Calendar.PM);
		return calendar.getTime();
	}

	@Override
	public int getMonthWeekByDayCount(Date date, int hDay) {

		// get calendar object
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// set current month date form starting
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		int month = cal.get(Calendar.MONTH);
		int countWeek = 0;
		do {
			int day = cal.get(Calendar.DAY_OF_WEEK);
			if (hDay > 0) {
				if (day == hDay) {
					countWeek++;
				}
			} else {
				if (day == Calendar.FRIDAY) {
					countWeek++;
				}
			}
			cal.add(Calendar.DAY_OF_WEEK, 1);
		} while (cal.get(Calendar.MONTH) == month);

		return countWeek;
	}

	@Override
	public int getMonthSize(Date date) {
		// get calendar object
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// set current month date form starting

		int mSize = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

		return mSize;
	}

	@Override
	public Date getNextMonth(Date prevDate) {

		if (prevDate != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(prevDate);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			if (calendar.get(Calendar.MONTH) == 11) {
				calendar.set(Calendar.MONTH, Calendar.JANUARY);
			} else {
				calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
			}

			return calendar.getTime();
		}

		return null;
	}

	@Override
	public int getMonthWeekByDayCount(Date date) {
		return getMonthWeekByDayCount(date, 0);
	}

	@Override
	public String getStartAndEndDateString(Date date) {

		String twoDate = "";
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.AM_PM, Calendar.AM);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		twoDate = calendar.getTime().toString();

		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

		twoDate += " To " + calendar.getTime().toString();
		return twoDate;
	}

	@Override
	public boolean isEqualDateOnlyMonthAndYear(Date date, Date date2) {

		Calendar calendar = getCalenderByDate(date);
		Calendar calendar2 = getCalenderByDate(date2);

		if (calendar.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)) {

			if (calendar.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)) {
				return true;
			}

		}

		return false;
	}

	@Override
	public Date getDateUsingDay(Date date, int day) {

		Calendar calendar = getCalenderByDate(date);
		calendar.set(Calendar.DATE, day);

		return calendar.getTime();
	}

	@Override
	public double getDefHoursBetweenTowDate(Date start, Date end) {
		if (start != null && end != null) {
			double duration = end.getTime() - start.getTime();

			double secInMilli = 1000;
			double minInMilli = secInMilli * 60;
			double hours2 = 0.0;

			double mins = duration / minInMilli;
			hours2 = mins / 60;

			return converterServices.getRoundTwoDecPoint(hours2);

		}
		return 0;
	}

	@Override
	public Date getNextDate(Date date) {

		Calendar calendar = getCalenderByDate(date);
		calendar.add(Calendar.DATE, 1);

		return calendar.getTime();

	}

	@Override
	public boolean isEqualDate(Date date, Date date2) {

		Calendar calendar = getCalenderByDate(date);
		Calendar calendar2 = getCalenderByDate(date2);

		if (calendar.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)
				&& calendar.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)) {

			if (calendar.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)) {
				return true;
			}

		}

		return false;
	}

	@Override
	public boolean isEqualDateOrGreater(Date date, Date date2) {

		Calendar calendar = getCalenderByDate(date);
		Calendar calendar2 = getCalenderByDate(date2);

		if (calendar.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)) {

			if (calendar.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)) {

				if (calendar.get(Calendar.DAY_OF_MONTH) <= calendar2.get(Calendar.DAY_OF_MONTH)) {
					return true;
				}
			}

		}

		return false;
	}

	@Override
	public boolean isPrevDay(Date date) {
		Calendar calendar = getCalenderByDate(date);
		Calendar cCalendar = getCalenderByDate(new Date());
		if (calendar.get(Calendar.MONTH) == cCalendar.get(Calendar.MONTH)) {

			if (calendar.get(Calendar.YEAR) == cCalendar.get(Calendar.YEAR)) {
				int thisiDay = cCalendar.get(Calendar.DATE) - 1;
				int givenDay = calendar.get(Calendar.DATE);
				if (thisiDay == givenDay) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean isGreater(Date date, Date date2) {
		Calendar calendar = getCalenderByDate(date);
		Calendar calendar2 = getCalenderByDate(date2);

		if (calendar.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)) {

			if (calendar.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)) {

				if (calendar.get(Calendar.DAY_OF_MONTH) < calendar2.get(Calendar.DAY_OF_MONTH)) {
					return true;
				}
			}

		}

		return false;
	}

	@Override
	public boolean isToDay(Date date) {
		Calendar calendar = getCalenderByDate(date);
		Calendar calendar2 = getCalenderByDate(new Date());

		if (calendar.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)) {

			if (calendar.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)) {

				if (calendar.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)) {
					return true;
				}
			}

		}

		return false;
	}

	@Override
	public boolean isGreaterOrEqual(Date first, Date secend) {
		Calendar calendar = getCalenderByDate(first);
		Calendar calendar2 = getCalenderByDate(secend);

		if (calendar != null) {
			if (calendar.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH)) {

				if (calendar.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR)) {

					if (calendar.get(Calendar.DAY_OF_MONTH) <= calendar2.get(Calendar.DAY_OF_MONTH)) {
						return true;
					}
				}

			}
		}

		return false;
	}

	@Override
	public boolean isEqualOrAfterDate(Date thisDate, Date testDate) {

		if (isEqualDate(thisDate, testDate)) {
			return true;
		} else {
			if (thisDate.after(testDate)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean isEqualOrBeforDate(Date thisDate, Date testDate) {

		if (isEqualDate(thisDate, testDate)) {
			return true;
		} else {
			if (thisDate.before(testDate)) {
				return true;
			}
		}

		return false;
	}
}