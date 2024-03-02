package com.altqart.req.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryReq {

	private int id;

	private int parentId;

	private String name;
	
	private String description;
	
	private String value;

}
