package com.altqart.req.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AreaReq {

	private int id;

	private int zone;

	@JsonAlias("area_name")
	private String name;

	private String value;

	@JsonAlias("area_id")
	private int pathaoCode;
}
