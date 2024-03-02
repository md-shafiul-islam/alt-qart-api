package com.altqart.req.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessReq {
	
	private int accessType;
	
	private String role;
	
	private String name;
	
	private String description;
	
	private int view;
	
	private int noAccess;
	
	private int add;
	
	private int edit;
	
	private int approve;
	
	private int updateApproval;
	
	private int all;
}
