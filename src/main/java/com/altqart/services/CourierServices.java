package com.altqart.services;

import java.util.Map;

import com.altqart.req.model.CourierReq;

public interface CourierServices {

	public void getAllCourier(Map<String, Object> map, int start, int size);

	public void addCourier(CourierReq courier, Map<String, Object> map);

	public void removeCourier(CourierReq courierReq, Map<String, Object> map);

	public void updateCourier(CourierReq courier, Map<String, Object> map);

}
