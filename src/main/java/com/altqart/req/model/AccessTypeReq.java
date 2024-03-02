package com.altqart.req.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessTypeReq {
	
	private String name;
	
	private String value;
	
	private int numValue;
	
}
