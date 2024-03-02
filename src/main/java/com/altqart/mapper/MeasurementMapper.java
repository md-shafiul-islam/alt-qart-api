package com.altqart.mapper;

import java.util.List;

import com.altqart.model.Measurement;
import com.altqart.req.model.MeasurementReq;
import com.altqart.resp.model.RespMeasurement;

public interface MeasurementMapper {

	public List<RespMeasurement> mapAllRespMeasurement(List<Measurement> measurements);

	public Measurement mapMeasurement(MeasurementReq measurementReq);

	public RespMeasurement mapRespMeasurement(Measurement measurement);

	public RespMeasurement mapRespMeasurementOnly(Measurement measurement);

}
