package com.altqart.resp.model;

import java.util.HashSet;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespMaterial {

	private int id;

	private List<RespProduct> products;

	private String name;

	private String description;

	private String slug;

}
