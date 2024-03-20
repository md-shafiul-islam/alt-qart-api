package com.altqart.helper.services;

import java.math.BigDecimal;

public interface HelperConverterServices {

	public String getRoundTwoDecPointStr(double value);

	public double decimelTwoPoint(double value);

	public String getIntToString(int value);

	public int getStringToInt(String value);

	public double getRoundTwoDecPoint(double value);

	public BigDecimal getDoubleToBigDec(double value);

	public String getDoubleToString(double value);

	public double getSubtractBetweenTwoValue(double hAmount, double lAmount);

	public double getDiscountPercentage(double discount, double amount);

	public double getDiscount(double discountPar, double amount);

	public double getAmountUsingPercentageTotal(double percentage, double amount);

	public double getRandomBetweenTwoNumbers(double min, double max);
}
