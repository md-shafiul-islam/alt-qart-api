package com.altqart.req.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SizeReq {

	private int id;

	private int standard;

	private String name;

	private double count;

	private String value;
}
