package com.altqart.req.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentApproveReq {

	private String id;
	
	private int type; //1,2,3  Approve, Reject, delete
	
	private int status; //0, 1
	
	private String approveNote;
	
	private String rejectNote;
}
