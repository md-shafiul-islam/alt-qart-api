package com.altqart.resp.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespDailyStatistics {

	private String publicId;

	private RespStore store;

	private double stock;

	private double assetDayDebit;

	private double assetDayCredit;

	private double assetTotalDebit;

	private double assetTotalCredit;
	
	private double servicesRevenue;

	private double servicesPaid;

	private double servicesDue;
	
	private double salesRevenue;

	private double salesPaid;

	private double salesDue;

	private double salesProfit;

	private double salesLoss;

	private double purchasesAmount;

	private double purchasesDue;

	private double purchasesPaid;

	private double expensesAmount;

	private double expensesDue;

	private double expensesPaid;

	private double prodExpensesAmount;

	private double prodExpensesDue;

	private double prodExpensesPaid;

	private double totalExpensesDebit;

	private double totalExpensesCredit;

	private double dailyCashCredit;

	private double dailyCashDebit;

	private double dailyCashCreditBalance;

	private double dailyCashDebitBalance;

	private double totalCashAcCredit;

	private double totalCashAcCreditBalance;

	private double totalCashAcDebit;

	private double totalCashAcDebitBalance;

	private double totalEsCredit;

	private double totalEsCreditBalance;

	private double totalEsDebit;

	private double totalEsDebitBalance;

	private double dailyTotalEsCredit;

	private double dailyTotalEsCreditBalance;

	private double dailyTotalEsDebit;

	private double dailyTotalEsDebitBalance;

	private double totalCarriedOverDebit;

	private double carriedOverDebitBalance;

	private double totalCarriedOverCredit;

	private double carriedOverCreditBalance;

	private double totalDiscountCredit;

	private double discountCreditBalance;

	private double totalDiscountDebit;

	private double discountDebitBalance;

	private double closeingBalance;

	private double openingBalance;

	private double totalTaxCredit;

	private double totalTaxDebit;

	private double totalVatCredit;

	private double totalVatDebit;

	private double salesServiceProfit;

	private double salesServiceLoss;

	private double financialGainDebit;

	private double financialGainCredit;

	private Date date;

	private String textDate;

	private Date dateGroup;

	private boolean finalize;
}
