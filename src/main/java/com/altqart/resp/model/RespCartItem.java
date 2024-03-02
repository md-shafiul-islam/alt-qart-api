package com.altqart.resp.model;

import com.altqart.model.Cart;
import com.altqart.model.Product;
import com.altqart.model.Variant;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RespCartItem {

	private String id;

	private String product;

	private String variant;

	private String cart;

	private String image;
	
	private String title;
	
	private String size;
	
	private String color;
	
	private double stkQty;
	
	private double qty;

	private double price;

	private double subTotal;

	private double discountPrice;
	
	private boolean isChoose;
}
