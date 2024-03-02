package com.altqart.resp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RespNamePhoneNo {

	private int id;

	private RespStore store;

	private String name;

	private String phoneNo;

	private boolean office;
}
