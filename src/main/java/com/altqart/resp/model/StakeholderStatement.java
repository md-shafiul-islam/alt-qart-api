package com.altqart.resp.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StakeholderStatement {

	private String description;

	private double debit;

	private double debitBalance;

	private double credit;

	private double creditBalance;

	private Date date;

	private Date dateGrupe;

	private boolean start;

	public StakeholderStatement(String description, double credit, Date date, Date dateGrupe) {
		super();
		this.description = description;
		this.credit = credit;
		this.date = date;
	}

}
