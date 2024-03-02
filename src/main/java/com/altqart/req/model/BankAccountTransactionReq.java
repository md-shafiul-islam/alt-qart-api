package com.altqart.req.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountTransactionReq {

	private String account;
	
	private String receptNo, postBy, refPhoneNo, chequeNo, receptUrl, refAccount, note, transId;
	
	private double amount;
}
