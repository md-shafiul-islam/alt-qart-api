package com.altqart.resp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBankAccount {

	private String id;

	private RespBankBranch bankBranch;

	private RespEsUser user;
	
	private List<RespBankTransection> bankTransList = new ArrayList<>();

	private String name;

	private String accountNo;

	private RespBankAccountType type;

	private double totalDebit;

	private double totalCredit;

	private double debitBalance;
	
	private double creditBalance;
	
	private RespEsCredit credit;
	
	private RespEsDebit debit;

	private Date date;

	private Date dateGroup;
	
	private int owner;

}
