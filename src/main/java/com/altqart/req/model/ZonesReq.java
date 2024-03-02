package com.altqart.req.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZonesReq {

	private List<ZoneReq> zoneReqs;
	
	private int city;
}
