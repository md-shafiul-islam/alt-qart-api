package com.altqart.resp.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespStakeholder {

	private String id;

	private String genId;

	private String name;

	private String email;

	private String phoneNo;

	private List<RespOrder> orders;

	private List<RespStakeholderType> respTypes;

	private List<RespBankAccount> accounts;

	private double debitAmount;

	private double creditAmount;

	private double creditBalance;

	private double debitBalance;

	private String description;

	private RespEsCredit esCredit;

	private RespEsDebit esDebit;

	private RespEsUser user;

	private RespEsUser approveUser;

	private int approve;

	private boolean active;

	private boolean activePayroll;

	private boolean updateReq;

	private Date date;

}
