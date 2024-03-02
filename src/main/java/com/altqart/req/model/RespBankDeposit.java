package com.altqart.req.model;

import java.util.Date;

import com.altqart.resp.model.RespBankAccount;
import com.altqart.resp.model.RespBankTransection;
import com.altqart.resp.model.RespEsUser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RespBankDeposit {

	private String id;

	private RespEsUser user;

	private RespBankAccount account;

	private RespBankTransection bankTransection;

	private String recepitNo;

	private String phoneNo;

	private String depositedBy;

	private String note;

	private double amount;

	private int approve;

	private Date date;

	private Date dateGroup;

}
