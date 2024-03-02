package com.altqart.services;

import java.util.List;
import java.util.Map;

import com.altqart.req.model.MeasurementStandardReq;
import com.altqart.req.model.SizeReq;
import com.altqart.resp.model.RespMeasurementStandard;

public interface SizeServices {

	public void getAll(Map<String, Object> map, int start, int size);

	public void add(SizeReq sizeReq, Map<String, Object> map);

	public void update(SizeReq sizeReq, Map<String, Object> map);

	public void remove(SizeReq sizeReq, Map<String, Object> map);

	public void getOneById(int id, Map<String, Object> map);

	public List<RespMeasurementStandard> getAllRespMeasurementStandard();

	public void addMeasurementStandard(MeasurementStandardReq standard, Map<String, Object> map);

	public void updateMeasurementStandard(MeasurementStandardReq standard, Map<String, Object> map);

	public RespMeasurementStandard getOneMeasurementStandard(int id);

}
