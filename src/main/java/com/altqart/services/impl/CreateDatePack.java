package com.altqart.services.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.springframework.stereotype.Service;

@Service
public class CreateDatePack {

	private DayOfWeek firstDayOfWeek;
	private DayOfWeek lastDayOfWeek;

	public HashMap<String, Date> getWeekEndStrat() {
		// TODO WeekEndStrat
		this.firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
		this.lastDayOfWeek = DayOfWeek
				.of(((((DayOfWeek) this.firstDayOfWeek).getValue() + 5) % DayOfWeek.values().length) + 1);

		LocalDate fisrstDayWeekUtil = LocalDate.now()
				.with(TemporalAdjusters.previousOrSame((DayOfWeek) this.firstDayOfWeek));
		Date sDate = Date.from(fisrstDayWeekUtil.atStartOfDay(ZoneId.systemDefault()).toInstant());

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sDate);

		sDate = calendar.getTime();

		LocalDate lWDate = LocalDate.now().with(TemporalAdjusters.nextOrSame((DayOfWeek) this.lastDayOfWeek));
		Date eDate = Date.from(lWDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

		Calendar calendarLastDate = Calendar.getInstance();

		calendarLastDate.setTime(eDate);
		calendarLastDate.set(Calendar.HOUR_OF_DAY, 23);
		calendarLastDate.set(Calendar.MINUTE, 59);
		calendarLastDate.set(Calendar.SECOND, 59);
		calendarLastDate.set(Calendar.MILLISECOND, 999);

		eDate = calendarLastDate.getTime();

		HashMap<String, Date> dateSet = new HashMap<>();

		dateSet.put("sDate", sDate);
		dateSet.put("eDate", eDate);

		return dateSet;

	}

	public HashMap<String, Date> getMonthStartEndDate() {

		HashMap<String, Date> setDate = new HashMap<>();

		Date date = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		Date sDate = calendar.getTime();

		calendar.setTime(date);

		int totalDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		calendar.set(Calendar.DAY_OF_MONTH, totalDay);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);

		Date eDate = calendar.getTime();

		setDate.put("sDate", sDate);
		setDate.put("eDate", eDate);

		return setDate;
	}

	public HashMap<String, Date> getCurrentDate() {

		HashMap<String, Date> setDate = new HashMap<>();

		Date date = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		int cDay = calendar.get(Calendar.DAY_OF_MONTH);

		calendar.set(Calendar.DAY_OF_MONTH, (cDay - 1));

		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);

		Date sDate = calendar.getTime();

		calendar.set(Calendar.DAY_OF_MONTH, cDay);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);

		Date eDate = calendar.getTime();

		setDate.put("sDate", sDate);
		setDate.put("eDate", eDate);

		return setDate;
	}

	public Calendar getCalenderByDate(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar;
	}
}
