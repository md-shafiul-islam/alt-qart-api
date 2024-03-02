package com.altqart.req.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaidReq {

	private String id;
	
	private List<MethodAndTransactionReq> transactionMethods;

	private double amount;
	
	private double discount;
	
	private double grandTotal;

	private String description;
	
	private Date date;
		


}
