package com.altqart.resp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespBankBranch {

	private int id;
	
	private RespBank bank;
	
	private String name;
	
	private String address;
	
	private String phoneNo;
}
