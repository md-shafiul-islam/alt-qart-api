package com.altqart.client.req.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParcelPriceReq {

	@JsonProperty("store_id")
	private int storeId;

	@JsonProperty("item_type")
	private int itemType = 2;

	@JsonProperty("delivery_type")
	private int deliveryType = 48;

	@JsonAlias("weight")
	@JsonProperty("item_weight")
	private double itemWeight;

	@JsonAlias("city")
	@JsonProperty("recipient_city")
	private int city;

	@JsonAlias("zone")
	@JsonProperty("recipient_zone")
	private int zone;

}
