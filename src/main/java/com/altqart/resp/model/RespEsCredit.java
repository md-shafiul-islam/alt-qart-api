package com.altqart.resp.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespEsCredit {

	private String id;
	
	private RespStakeholder customer;
	
	private RespEsUser user;
	
	private List<RespEsCreditDetail> creditDetails;
	
	private double amount;
	
	private String note;
	
	private Date date;
}
