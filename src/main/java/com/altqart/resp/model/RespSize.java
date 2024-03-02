package com.altqart.resp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespSize {

	private int id;

	private RespMeasurementStandard standard;

	private String name;

	private double count;

	private String value;
}
