package com.altqart.resp.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespEsStakeholder {

	private String genId;

	private String id;

	private String description;

	private String rejectNote;

	private boolean active;

	private List<RespStakeholderType> respTypes;;

	private String name;

	private String address1;

	private String address2;

	private String email;

	private String phoneNo;
	
	private double totalCreditAmount;

	private double totalDebitAmount;

	private double creditBalance;

	private double debitBalance;

	private String note;

	private int approve; // 0, 1, 2 value Pending, Approve, Reject,

	private Date date;
}
