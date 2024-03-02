package com.altqart.resp.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespBankTransection {

	private String id;

	private RespEsUser user;

	private RespBankAccount account;

	private double amount;

	private RespBankStatus bankStatus;

	private String token;

	private int pay;

	private int receive;

	private Date date;

	private Date dateGroup;

}
