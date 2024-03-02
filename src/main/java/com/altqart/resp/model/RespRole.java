package com.altqart.resp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespRole {

	private String id;
	
	private String genId;

	private List<RespAccess> accesses = new ArrayList<>();
	
	private List<RespEsUser> users = new ArrayList<>();
	
	private String name;
	
	private String description;
	
	private Date date;
	
	private Date dateGroupe;
	
	private int authStatus;
}
