package com.altqart.resp.model;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespOrder {

	private String id;

	private String invNo;

	private RespStakeholder customer;

	private RespEsUser user;

	private List<RespOrderItem> orderItems = new ArrayList<>();

	private RespStatus status;

	private List<RespMethodAndTransaction> methodTransactions;

	private double netSaleAmount;

	private double netSaleRevenueAmount;

	private double itemTotal;

	private double vatRate;

	private double totalVat;

	private double totalAmountVat;

	private double discount;

	private double shippingAndHandlingAmount;

	private double otherAmount;

	private double totalCost;

	private double amountAfterDiscount;

	private BigDecimal orderTotalProfit;

	private BigDecimal orderTotalLoss;
	
	private double grandTotal;

	private double netGrandTotal;
	
	private double totalCredit;

	private double totalDebit;

	private double prevCredit;

	private double prevDebit;

	private double paidAmount;

	private double creditAmount;

	private LocalTime nextPayDate;

	private String narration;

	private double lsessAdjustment;

	private double discountPercent;

	private double cullectedAmount;

	private double changeAmount;

	private double totalProductCost;

	private double totalSaleAmountWithTotalCost;

	private Date date;

	private Date grupeDate;

}
