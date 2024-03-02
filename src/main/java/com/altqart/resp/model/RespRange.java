package com.altqart.resp.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespRange{

	private String id;
	
	private List<RespRank> rankLavels = new ArrayList<>();
	
	private String name;
	
	private String description;

	
	
}
