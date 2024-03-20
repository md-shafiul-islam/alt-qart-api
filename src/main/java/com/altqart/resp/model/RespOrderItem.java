package com.altqart.resp.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespOrderItem {

	private String id;

	private RespOrder order;

	private RespVariant variant;

	private double qty;

	private double price;

	private boolean isReturn;

	private double returnQty;

	private double subTotal;

	private Date expWarranty;

	private Date date;

	private Date groupDate;

}
