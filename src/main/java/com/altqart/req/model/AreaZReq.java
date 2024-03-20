package com.altqart.req.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AreaZReq {

	private TempZoneReq zone;

	private int pathaoCode;

	private String name;

	private boolean isHome;

	private boolean isPickup;
	
	private String value;
	
	
}
