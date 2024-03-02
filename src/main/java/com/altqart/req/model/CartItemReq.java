package com.altqart.req.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemReq {

	private String id;

	private String product;

	private String variant;

	private String cart;

	private double qty;

	private double price;

	private double subTotal;

	private double discountPrice;

	private boolean isChoose;

}
