package com.altqart.req.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsUserActionReq {

	private int locked, enabled, credentialsNonExpired, accountNonExpired;
	
	private String id;

}
