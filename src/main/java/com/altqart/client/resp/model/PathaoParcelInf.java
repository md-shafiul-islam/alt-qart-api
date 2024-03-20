package com.altqart.client.resp.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PathaoParcelInf {

	@JsonAlias("consignment_id")
	private String consignmentId;

	@JsonAlias("merchant_order_id")
	private String merchantOrderId;

	@JsonAlias("order_status")
	private String orderStatus;

	@JsonAlias("delivery_fee")
	private double deliveryFee;
}
