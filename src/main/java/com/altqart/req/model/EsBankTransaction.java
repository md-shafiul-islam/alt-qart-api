package com.altqart.req.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsBankTransaction {

	private String stakholder;

	private String giverAccount;
	
	private String receiverAccount;
	
	private String transType;

	private double amount;

	private String chequeNo;

	private String receiptNo;

	private String transactionID; // If Online Transaction

	private String note;

	private String phoneNo;

	private String postedBy;
}
