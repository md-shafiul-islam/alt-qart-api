package com.altqart.resp.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespReceive {

	private String id;
	
	private String invId;

	private List<RespMethodAndTransaction> methodAndTransactions;
	
	private RespEsStakeholder esStakeholder;

	private RespEsUser user;
	
	private String description;
	
	private double prevDebit;
	
	private double amount;

	private double discount;
	
	private double grandTotal;
	
	private double presentDebit;
	
	private int approve;
	
	private boolean accountStatus;
	
	private Date date;

	private Date dateGroup;
}
