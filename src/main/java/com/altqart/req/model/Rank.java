package com.altqart.req.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rank {

	private int id;
	
	private RangeReq accessRange;

	private String name;
	
	private String description;

	
	
}
