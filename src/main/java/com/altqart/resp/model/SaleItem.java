package com.altqart.resp.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaleItem {

	private int id;
	
	private String name;
	
	private int product;
	
	private int item;
	
	private double vatRate, discountRate, qty, price, profit, loss;
	
	private String purchaseBarCode, productBarCode, invId;
	
	private Date date;
	
	
}
