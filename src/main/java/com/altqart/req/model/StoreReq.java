package com.altqart.req.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreReq {

	private String id;
	
	private String startLine;
	
	private String name;
	
	private String description;
	
	private String proprietor;

	private String address;

	private String email;

	private String website;
	
	private double vatRat;
	
	private String businessType;
	
}
