package com.altqart.resp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RespUser {

	private String id;

	private RespStore store;

	private List<RespSaleReturn> approveSaleReturns = new ArrayList<>();

	private List<RespPaid> paids = new ArrayList<>();

	private RespStakeholder stakeholder;

	private String name;

	private String username;

	private String address1;

	private String address2;

	private String phoneNo;

	private String email;

	private String code;

	private Date date;

	private Date udate;

	private RespRole role;

	private int enabled;

	private int locked;

	private int credentialsNonExpired;

	private int accountNonExpired;

}
