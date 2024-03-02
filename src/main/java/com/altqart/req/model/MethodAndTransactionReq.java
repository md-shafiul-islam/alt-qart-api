package com.altqart.req.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MethodAndTransactionReq {
	
	private String source;
	
	private String destination;

	private String refNo;

	private double amount;
	
	
}
