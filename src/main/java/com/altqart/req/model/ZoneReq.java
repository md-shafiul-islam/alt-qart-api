package com.altqart.req.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZoneReq {

	private int id;

	@JsonAlias("zone_name")
	private String name;

	private int city;

	private String value;

	@JsonAlias("zone_id")
	private int pathaoCode;
}
