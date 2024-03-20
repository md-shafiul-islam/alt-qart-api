package com.altqart.req.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZoneCityReq {

//	private int ptCity;

	private String name;

	private int pathaoCode;

	private String value;

	
	private TempCityReq city;

}
