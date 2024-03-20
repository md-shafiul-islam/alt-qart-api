package com.altqart.client.req.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PathaoWebHookReq {

	@JsonAlias("consignment_id")
	private String consignmentId;
	
	@JsonAlias("merchant_order_id")
	private String merchantOrderId;
	
	@JsonAlias("order_status")
	private String orderStatus;
	
	@JsonAlias("order_status_slug")
	private String orderStatusSlug;
	
	@JsonAlias("updatedAt")
	@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
	private String updated_at;

	@JsonAlias("paymentStatus")
	private String payment_status;
	
	@JsonAlias("invoiceId")
	private String invoice_id;

	@JsonAlias("reason")
	private String reason;

	@JsonAlias("collected_amount")
	private double collectedAmount;
}
