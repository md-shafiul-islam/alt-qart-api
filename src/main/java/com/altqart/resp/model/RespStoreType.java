package com.altqart.resp.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespStoreType {

	private int id;

	private List<RespStore> stores;

	private String name;

	private String description;

	private String value;

}
