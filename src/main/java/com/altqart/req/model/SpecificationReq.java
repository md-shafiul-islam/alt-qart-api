package com.altqart.req.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecificationReq {

	private int specKey;

	private String value;

	private String description;

	private boolean isFeature;
}
