package com.altqart.req.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CartChooseReq {

	public String id;

	private String cartItem;

	private int type;

	private boolean choose;

}
