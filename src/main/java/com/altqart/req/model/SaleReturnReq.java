package com.altqart.req.model;


import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleReturnReq {

	private String stakeholder;

	private int returnType;

	private List<SaleReturnItemReq> returnItems;
	
	private double itemsTotal;
    
	private double returnFees;
	
	private double subReturnFees;
	
	private double lessAdustment;
		
	private double grandTotal;
	
	private double paidAmount;
	
	private double creditAmount;
	
	private double changeAmount;

	private String note;

	private int orderId;
	
	private Date date;

	
}
