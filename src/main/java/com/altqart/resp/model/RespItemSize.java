package com.altqart.resp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RespItemSize {
	
	private int id;

	private RespMeasurementStandard standard;

	private String name;
	
	private String variant;

	private double count;

	private String value;

}
