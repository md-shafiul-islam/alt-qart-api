package com.altqart.resp.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RespSaleReturn {

	private String id;

	private RespStakeholder stakeholder;

	private RespEsUser user;

	private RespReturnType returnType;

	private List<RespSaleReturnItem> returnItems;

	private List<RespMethodAndTransaction> methodAndTransactions;

	private double itemsTotal;

	private double returnFees;

	private double subReturnFees;

	private double lessAdustment;

	private double grandTotal;

	private double paidAmount;

	private double creditAmount;

	private double changeAmount;

	private double totalProfit;

	private double totalLoss;

	private String note;

	private int approve;

	private Date date;

	private Date groupDate;

	private double saleRevenue;

	private double totalDiscount;

	private double totalVat;

	private double prevCredit;

	private double prevDebit;
}
