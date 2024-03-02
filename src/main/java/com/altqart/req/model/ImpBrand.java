package com.altqart.req.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImpBrand {

	private String name;
	private String description;

	@JsonAlias("tag_line")
	private String tagLine;

	@JsonAlias("logo_url")
	private String logoUrl;

	@JsonAlias("web_url")
	private String webUrl;

	@JsonAlias("company_key")
	private String companyKey;

	@JsonAlias("alias_name")
	private String aliasName;
	
	private String address;

}
