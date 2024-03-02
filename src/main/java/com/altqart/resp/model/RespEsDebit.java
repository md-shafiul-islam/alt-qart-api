package com.altqart.resp.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespEsDebit {

	private String id;

	private RespStakeholder customer;

	private RespEsUser user;
	
	private List<RespEsDebitDetail> debitDetails;

	private double amount;

	private String note;

	private Date date;
}
