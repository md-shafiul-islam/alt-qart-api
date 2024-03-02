package com.altqart.resp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RespVariant {

	private String id;

	private RespVariant product;

	private RespColor color;

	private RespSize size;

	private double price;

	private double qty;

	private double discountPrice;

	private String sku;

	private String barCode;

	private String imageUrl;

	private String freeItems;

	private boolean discount;

	private boolean available;

	private boolean isFeature;

}
