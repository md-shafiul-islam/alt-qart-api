package com.altqart.req.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeasurementReq {

	private int id;

	private double weight;

	private double lenght;

	private double width;

	private double height;
}
