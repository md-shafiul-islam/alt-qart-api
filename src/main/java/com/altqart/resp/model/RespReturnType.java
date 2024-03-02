package com.altqart.resp.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespReturnType {

	private int id;
	
	private List<RespSaleReturn> saleReturns = new ArrayList<>();
	
	private String name;
	
	private String value;
		
	
}
