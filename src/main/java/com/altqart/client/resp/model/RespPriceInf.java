package com.altqart.client.resp.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespPriceInf {

	public double price;

	public double discount;

	@JsonAlias("promo_discount")
	public double promoDiscount;

	@JsonAlias("plan_id")
	public double planId;

	@JsonAlias("cod_enabled")
	public double codEnabled;

	@JsonAlias("cod_percentage")
	public double codPercentage;

	@JsonAlias("additional_charge")
	public double additionalCharge;

	@JsonAlias("final_price")
	public double finalPrice;

}