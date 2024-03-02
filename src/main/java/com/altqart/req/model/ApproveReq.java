package com.altqart.req.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApproveReq {

	private String id;
	
	private int type; //Add, Update Or Delete. 1,2,3 (If 4 then Only active inactive)
	
	private int status; //Pending 0,  Approve 1, Reject 2, (If type 4 then active or deactive Only status 0 Or 1)
	
	private String rejectNote;
	
	private String narration;

}
