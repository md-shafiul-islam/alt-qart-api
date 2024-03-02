package com.altqart.resp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RespMethodAndTransaction {

	private String id;

	private RespBankAccount source;

	private RespBankAccount destination;

	private RespSaleReturn saleReturnInvoice;

	private RespOrder order;

	private RespPaid paid;

	private RespReceive receive;

	private String refNo;

	private double amount;

}
