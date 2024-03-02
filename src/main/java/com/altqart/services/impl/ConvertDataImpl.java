package com.altqart.services.impl;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.altqart.services.ConvertData;

@Service
public class ConvertDataImpl implements ConvertData {

	@Override
	public Date getFullDate() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	@Override
	public Date getFullWeekDate() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.setFirstDayOfWeek(Calendar.SATURDAY);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		int weekYer = calendar.get(Calendar.WEEK_OF_YEAR);
		calendar.get(Calendar.DAY_OF_WEEK);
		return calendar.getTime();
	}

	@Override
	public Date getWarrantyString(String day, String mont, String year) {

		int d = getStringToInt(day);
		int m = getStringToInt(mont);
		int y = getStringToInt(year);

		return getWarranty(d, m, y);
	}

	private int getStringToInt(String numString) {

		try {

			return Integer.parseInt(numString);

		} catch (NumberFormatException e) {
			System.out.println("Provided String Not Number !! Lin 57. class.ConvertDataImpl");
			return 0;
		}

	}

	@Override
	public Date getWarranty(int day, int mont, int year) {

		Date date = new Date();
		return createDateByInput(day, mont, year, date);

	}

	private Date createDateByInput(int day, int inMonth, int inYear, Date date) {

		Calendar cal = Calendar.getInstance();

		cal.setTime(date);

		int month = cal.get(Calendar.MONTH);
		int cDay = cal.get(Calendar.DAY_OF_MONTH);

		if (day > 0 && day < 30) {
			int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

			day = (cDay + day);
			if (day > maxDay) {
				cDay = (day - maxDay);
				cal.set(Calendar.MONTH, (month + 1));
			}

			cal.set(Calendar.DAY_OF_MONTH, cDay);
		}
		

		if (inMonth > 0) {

			month = month + 1;
			inMonth = (month + inMonth);

			if (cDay < day) {
				cDay = day;
			}

			cal.set(Calendar.DAY_OF_MONTH, cDay);

			if (inMonth > 12) {

				inMonth = (inMonth - 12);
				int cYear = cal.get(Calendar.YEAR);
				cYear = (cYear + 1);
				cal.set(Calendar.YEAR, cYear);
			}

			cal.set(Calendar.MONTH, inMonth);
			
		}

		if (inYear > 0) {

			month = month + 1;

			inMonth = (month + inMonth);
			int cYear = cal.get(Calendar.YEAR);

			inYear = (cYear + inYear);

			if (cDay < day) {
				cDay = day;
			}


			if (inMonth > 12) {

				inMonth = (inMonth - 12);
				cYear = (cYear + 1);
			}

			cal.set(Calendar.MONTH, inMonth);

			cal.set(Calendar.DAY_OF_MONTH, cDay);
			cal.set(Calendar.MONTH, inMonth);
			cal.set(Calendar.YEAR, inYear);

		}
		return cal.getTime();

	}

	@Override
	public Date getExpDate(int day, int i, int exYear) {

		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		calendar.setTime(date);

		calendar.set(Calendar.MONTH, i);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.YEAR, exYear);
		date = calendar.getTime();

		return date;
	}

	@Override
	public Date localToUtilDate(LocalDate date) {

		Date uDate = new Date();

		uDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

		return uDate;

	}

}
