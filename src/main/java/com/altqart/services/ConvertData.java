package com.altqart.services;

import java.time.LocalDate;
import java.util.Date;

public interface ConvertData {

	public Date getFullDate();
	
	public Date getFullWeekDate();
	
	public Date getWarranty(int day, int mont, int year);
	
	public Date getWarrantyString(String day, String mont, String year);
	
	public Date getExpDate(int day, int i, int exYear);
	
	public Date localToUtilDate(LocalDate date);
	
}
