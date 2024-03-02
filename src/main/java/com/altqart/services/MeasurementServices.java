package com.altqart.services;

import java.util.Map;

import com.altqart.req.model.MeasurementReq;

public interface MeasurementServices {

	public void getAll(Map<String, Object> map, int start, int size);

	public void add(MeasurementReq measurementReq, Map<String, Object> map);

	public void update(MeasurementReq measurementReq, Map<String, Object> map);

	public void remove(MeasurementReq measurementReq, Map<String, Object> map);

	public void getOneById(int id, Map<String, Object> map);

}
