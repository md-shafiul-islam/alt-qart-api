package com.altqart.req.model;

import java.util.Date;

import com.altqart.resp.model.RespBankAccount;
import com.altqart.resp.model.RespBankStatus;
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
public class RespBankWithdraw {

	private String id;

	private RespEsUser user;

	private double amount;

	private RespBankAccount bankAccount;

	private RespBankStatus bankStatus;

	private RespBankTransection bankTransection;

	private String chequeNo;

	private String transactionID; // If Online Transaction

	private String note;

	private String phoneNo;

	private String withdrawnBy;

	private int approve;

	private Date date;

	private Date dateGroup;

}
