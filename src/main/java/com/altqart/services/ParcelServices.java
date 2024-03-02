package com.altqart.services;

import java.util.Map;

import com.altqart.req.model.ParcelReq;

public interface ParcelServices {

	public void getAllParcel(Map<String, Object> map, int start, int size);

	public void addParcel(ParcelReq parcelReq, Map<String, Object> map);

	public void updateParcel(ParcelReq parcelReq, Map<String, Object> map);

	public void getParcelById(String id);
	
	

}
