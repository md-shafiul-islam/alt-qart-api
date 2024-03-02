package com.altqart.resp.model;

import java.util.Date;
import java.util.List;

import com.altqart.model.Product;

import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespBrand {

	private String id;

	private String key;

	private String name;

	private List<RespProduct> products;

	private String description;

	private String tagLine;

	private String logoUrl;

	private String web;

	private Date date;
}
