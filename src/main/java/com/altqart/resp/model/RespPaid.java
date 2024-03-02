package com.altqart.resp.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespPaid {

	private String id;
	
	private String invId;

	private RespEsStakeholder esStakeholder;

	private RespEsUser user;

	private List<RespMethodAndTransaction> methodAndTransactions;

	private double prevCredit;

	private double presentCredit;

	private double amount;
	
	private double grandTotal;

	private double discount;

	private String description;

	private int approve;

	private boolean accountStatus;

	private Date date;

	private Date dateGroup;

}
