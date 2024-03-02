package com.altqart.resp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespEsUser {
	
	private String publicId;

	private String name;

	private String address1;

	private String address2;

	private String phoneNo;

	private String email;

	private String code;
	
}
