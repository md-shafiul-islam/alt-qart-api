package com.altqart.resp.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespEsDebitDetail {

	private int id;

	private RespEsDebit debit;

	private RespEsUser user;

	private double amount;

	private String note;

	private int status;

	private Date date;

	private Date dateGroup;

}
