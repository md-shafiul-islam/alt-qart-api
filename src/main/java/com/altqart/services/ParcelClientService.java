package com.altqart.services;

import java.util.Map;

import com.altqart.client.req.model.ParcelPriceReq;

public interface ParcelClientService {

	public Object initParcelPricing(String baseUrl, String uri);

	public void getToken();

	public Object getCreateParcel(String baseUrl, String uri, String token);

	public void getValidateToken(Map<String, Object> map);

	public void getParcelPrice(ParcelPriceReq parcelPriceReq, Map<String, Object> map);

}
