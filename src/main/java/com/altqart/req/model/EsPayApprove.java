package com.altqart.req.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsPayApprove {

	private String stId;
	
	private int id;
	
	private String narration;
	
	private boolean status;
	
	
}
