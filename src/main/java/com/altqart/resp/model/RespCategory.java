package com.altqart.resp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespCategory {

	private int id;

	private String name, description;

	private List<RespProduct> products = new ArrayList<>();
	
	private RespCategory parent;
	
	private List<RespCategory> subCats;
	
	private boolean subCat;

	private Date date;

}
