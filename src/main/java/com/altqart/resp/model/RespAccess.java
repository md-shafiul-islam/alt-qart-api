package com.altqart.resp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespAccess {

	private String id;

	private RespAccessType accessType;

	private RespRole role;

	private String name;

	private String description;

	private boolean view;

	private boolean noAccess;

	private boolean add;

	private boolean edit;

	private boolean approve;

	private boolean updateApproval;

	private boolean all;
}
