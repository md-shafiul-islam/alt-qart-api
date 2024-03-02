package com.altqart.req.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqBrand {

	private String id;

	private String name;

	private String description;

	private String web;

	private String key;

	private String aliasName;

	private String tagLine;

	private String logoUrl;

	private Date date;
}
