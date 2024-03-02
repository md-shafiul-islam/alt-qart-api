package com.altqart.req.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankBranchReq {

	private String bank;

	private String name;

	private String key;

	private String address;

	private String phoneNo;
}
