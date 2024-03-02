package com.altqart.req.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankReq {

	private String id;
	
	private String name;
	
	private String description;

	private String logoUrl;
	
	private String bankType;

	
}
