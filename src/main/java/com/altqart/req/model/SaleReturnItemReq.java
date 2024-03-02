package com.altqart.req.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleReturnItemReq {

	private int id;
	
	private int orderItem;
	
	private String name;
	
	private int product;
	
	private int item;
	
	private String purchaseBarCode, productBarCode, invId;
	
	private double qty;
	
	private double price;
	
	private double itemSub;
	
	private double discountRate;
	
	private double dsicount;
	
	private double subDsicount;
	
	private double vatRate;
	
	private double vat;
	
	private double subTotal;
	
	private double profit;
	
	private double loss;
	
	private Date date;
	
}
