package com.altqart.req.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StakeholderReq {

	
	private String id;
	
	private String business;

	private String name;

	private String phoneNo;

	private String email;

	private String description;

	private double initCreditAmount;

	private double initDebitAmount;
	
	private String note;
	
	private String key;


}
