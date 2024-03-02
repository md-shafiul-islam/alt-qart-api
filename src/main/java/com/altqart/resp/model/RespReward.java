package com.altqart.resp.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespReward {
	
	private int id;
	
	private RespUser user;
	
	private RespStakeholder stakeholder;
	
	private double amount;
	
	private String typeOfPayment;
	
	private String note;
	
	private Date date;
	
}
