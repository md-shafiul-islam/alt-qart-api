package com.altqart.resp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespAddress {

	private String id;
	
	private String fullName;

	private RespStakeholder stakeholder;

	private RespCity city;

	private RespZone zone;

	private RespArea area;

	private boolean isDefault;

	private boolean isOffice;

	private String zipCode;

}
