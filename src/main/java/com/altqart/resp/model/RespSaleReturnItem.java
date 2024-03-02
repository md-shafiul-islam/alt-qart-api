package com.altqart.resp.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RespSaleReturnItem {

	private String id;

	private String name;

	private RespSaleReturn saleReturnInvoice;

	private RespProduct product;

	private RespOrderItem orderItem;

	private double qty;

	private double price;

	private double itemSub;

	private double discountRate;

	private double dsicount;

	private double subDsicount;

	private double vatRate;

	private double vat;

	private double subTotal;

	private double profit;

	private double loss;

	private Date date;

}
