package com.altqart.helper.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperConverterServices;
import com.altqart.helper.services.HelperServices;

@Service
public class HelperConverterServicesImpl implements HelperConverterServices {

	@Autowired
	private HelperServices helperServices;

	@Override
	public String getRoundTwoDecPointStr(double value) {
		double d = getRoundTwoDecPoint(value);
		return getDoubleToString(d);
	}

	@Override
	public String getDoubleToString(double value) {

		return Double.toString(value);
	}

	@Override
	public int getStringToInt(String value) {

		if (!helperServices.isNullOrEmpty(value)) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		return 0;
	}

	@Override
	public double decimelTwoPoint(double value) {

		DecimalFormat df = new DecimalFormat("#.####");
		df.setRoundingMode(RoundingMode.CEILING);

		String decimelValue = df.format(value);

		return Double.parseDouble(decimelValue);
	}

	@Override
	public String getIntToString(int value) {
		if (value > 0) {

			return Integer.toString(value);
		}
		return "0";
	}

	@Override
	public double getRoundTwoDecPoint(double value) {

		BigDecimal bd = new BigDecimal(Double.toString(value));
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd.doubleValue();

	}

	@Override
	public BigDecimal getDoubleToBigDec(double dVal) {

		return new BigDecimal(dVal);
	}

	@Override
	public double getSubtractBetweenTwoValue(double hAmount, double lAmount) {
		if (hAmount > lAmount) {
			double nAmount = hAmount - lAmount;
			return nAmount > 0 ? getRoundTwoDecPoint(nAmount) : 0;
		}

		return 0;
	}

	@Override
	public double getDiscount(double discountPar, double amount) {
		double discount = (amount * discountPar);
		return discount / 100;
	}

	@Override
	public double getDiscountPercentage(double discount, double amount) {
		double percentage = discount / amount;
		return percentage * 100;
	}

	@Override
	public double getAmountUsingPercentageTotal(double percentage, double amount) {
		double percentageAmount = (amount * percentage);
		return percentageAmount / 100;
	}
}
