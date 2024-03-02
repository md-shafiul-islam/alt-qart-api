package com.altqart.req.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NamePhoneNoReq {

	private int id;

	private int store;

	private String name;

	private String phoneNo;

	private boolean office;
}
