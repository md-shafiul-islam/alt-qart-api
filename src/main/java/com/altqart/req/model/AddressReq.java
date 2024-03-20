package com.altqart.req.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressReq {
		
	private String fullName;
	
	private String phoneNoOpt;
	
	private String phoneNo;
	
	private String stakeholder;
	
	private String city;

	private String zone;

	private String area;

	private String upazila;

	private String zipCode;
	
	private String fullAddress;

	private boolean isDefault;

	private boolean isOffice;
}
