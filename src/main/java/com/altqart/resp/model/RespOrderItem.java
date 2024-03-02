package com.altqart.resp.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespOrderItem {

	private String id;
	
	private RespOrder order;
	
	private RespProduct product;
	
	private double qty;
	
	private double price;
	
	private BigDecimal subTotal;
	
	private double profit;
	
	private double totalProfit;
	
	private double loss;
	
	private double totalLoss;
	
	private double totalCostOfGoods;
	
	private Date expWarranty;
	
	private Date date;
	
	private Date groupDate;
	
	private String purchaseBarCode;
	
	private int returnStatus;
	
	private double returnQty;
	
}
