package com.altqart.resp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatementInf {

	private double totalCreditAmount;
	
	private double totalDebitAmount;
	
	private double deffValue;
}
