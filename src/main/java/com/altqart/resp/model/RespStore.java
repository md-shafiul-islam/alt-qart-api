package com.altqart.resp.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespStore {

	private String id;

	private String startLine;

	private String logoUrl;

	private String name;

	private String address;

	private String email;

	private String website;

	private String description;

	private String proprietor;

	private RespStoreType storeType;

	private List<RespNamePhoneNo> namePhoneNos;

}
