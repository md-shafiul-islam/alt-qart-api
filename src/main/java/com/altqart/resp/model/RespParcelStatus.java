package com.altqart.resp.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespParcelStatus {

	private int id;

	private String name;

	private String value;
	
	private List<RespParcel> parcels;

}
