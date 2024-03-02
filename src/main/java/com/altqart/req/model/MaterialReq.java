package com.altqart.req.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialReq {

	private int id;

	private String name;

	private String description;

	private String slug;
	
}
