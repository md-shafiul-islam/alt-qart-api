package com.altqart.req.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPlaceReq {

	private String cart;
			
	private String address;

	private double paidAmount;
	
	private double shipping;
	
	private List<MethodAndTransactionReq> methodTransactions;
	
	private String note;
	

}
