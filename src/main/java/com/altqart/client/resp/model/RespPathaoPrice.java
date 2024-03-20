package com.altqart.client.resp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespPathaoPrice {

	public String message;

	public String type;

	public String code;

	public RespPriceInf data;
}
