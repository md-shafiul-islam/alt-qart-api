package com.altqart.resp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespMinAddress {

	private String id;

	private String fullName;
	
	private String phoneNo;
	
	private String phoneNo2;

	private RespStakeholder stakeholder;

	private RespNameCode city;

	private RespNameCode zone;

	private RespNameCode area;

	private boolean isDefault;

	private boolean isOffice;

	private String zipCode;
}
