package com.altqart.resp.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespAccessType {

	private String id;

	private List<RespAccess> accesses = new ArrayList<>();

	private String name;

	private String value;

	private int numValue;

}
