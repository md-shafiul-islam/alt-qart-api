package com.altqart.req.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartReq {

	private String id;
	
	private String stakeholder;

	private List<CartItemReq> cartItemReqs;

	private double dicountPar;

	private double discount;

	private String couponCode;

	private double totalAmount;

	private double totalQty;

}
