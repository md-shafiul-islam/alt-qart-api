package com.altqart.resp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespInvoiceSetting {

	private int id;

	private RespStore store;

	private String title;

	private String bnTitle;

	private boolean calculate;

	private boolean thermalPrint;

	private String template;

	private boolean paidInfEnable;

	private boolean disableUserInf;

	private boolean prevAmount;

	private boolean presentAmount;
}
