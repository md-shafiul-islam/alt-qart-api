package com.altqart.resp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespShippingAddress {

	private String id;

	private String fullName;
	
	private String phoneNo;
	
	private String phoneNo2;

	private String city;

	private String zone;

	private String area;

	private String zipCode;

}
