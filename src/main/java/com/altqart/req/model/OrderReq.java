package com.altqart.req.model;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderReq {

	private String business;
	
	private StakeholderReq customer;
	
	private List<OrderItemReq> products;
	
	private double itemsTotal;
	
	private double vatRate;
	
	private double totalVat;
	
	private double discountRate;
	
	private double totalDiscount;
	
	private double lessAdustment;
	
	private double aftDiscount;
	
	private double aftToVatWithAmount;
	
	private double shippingAndHandlingAmount;
	
	private double otherAmount;
	
	private double totalCost;
	
	private double totalAmntWithCost;
	
	private double aftLessAdjAmnt;
	
	private double grandTotal;
	
	private List<MethodAndTransactionReq> methodTransactions;
	
	private double cstPayAmnt;
	
	private double creditAmount;
	
	private double changeAmount;
	
	private boolean adjStatus;
	
	private String narration;
	
	private Date date;
}
