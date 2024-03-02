package com.altqart.req.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleReq {

	private String id;

	private String name;

	private String description;

	private Date date;

	private int authStatus;
}
