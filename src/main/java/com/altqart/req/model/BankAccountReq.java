package com.altqart.req.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountReq {

	private String id;
	
	private int branch;
	
	private String store;
	
	private String stakeholder;

	private String name;

	private String accountNo;
	
	private String note;

	private int accountType;

	private double monthlyInstallment;
	
	private int owner;
	
	

}
