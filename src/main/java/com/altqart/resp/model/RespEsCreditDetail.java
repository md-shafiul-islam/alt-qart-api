package com.altqart.resp.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespEsCreditDetail {

	private int id;

	private RespEsCredit credit;

	private RespEsUser user;

	private double amount;

	private String note;

	private Date date;

	private int status;

	private Date dateGroup;

}
