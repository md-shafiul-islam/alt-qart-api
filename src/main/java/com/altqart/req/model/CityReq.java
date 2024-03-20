package com.altqart.req.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityReq {

	private int id;

	private int code;

	@JsonAlias("city_name")
	private String name;

	private String key;

	@JsonAlias("city_id")
	private int pathaoCode;

}
