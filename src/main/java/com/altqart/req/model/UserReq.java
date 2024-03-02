package com.altqart.req.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReq {

	private String publicId;

	private String name;

	private String username;

	private String password;

	private String address1;

	private String address2;

	private String phoneNo;

	private String email;

	private String code;

	private Date date;

	private Date udate;

	private String role;

}
