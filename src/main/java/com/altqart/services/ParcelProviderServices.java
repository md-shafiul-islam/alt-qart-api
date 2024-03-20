package com.altqart.services;

import java.util.Map;

import com.altqart.model.ParcelProvider;
import com.altqart.req.model.ParcelProviderReq;
import com.altqart.resp.model.RespParcelProvider;

public interface ParcelProviderServices {

	public ParcelProvider getParcelProviderByKey(String key);
	
	public boolean save(ParcelProviderReq parcelProviderReq );

	public RespParcelProvider getParcelProviderById(String id);

	public ParcelProvider getParcelProviderByWebHookSecret(String givenKey);

	public void getAllProvider(Map<String, Object> map, int start, int size);

}
