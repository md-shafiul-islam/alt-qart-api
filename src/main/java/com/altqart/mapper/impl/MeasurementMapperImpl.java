package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.MeasurementMapper;
import com.altqart.model.Measurement;
import com.altqart.req.model.MeasurementReq;
import com.altqart.resp.model.RespMeasurement;

@Service
public class MeasurementMapperImpl implements MeasurementMapper {

	@Autowired
	private HelperServices helperServices;

	@Override
	public List<RespMeasurement> mapAllRespMeasurement(List<Measurement> measurements) {

		if (measurements != null) {

			List<RespMeasurement> respMeasurements = new ArrayList<>();

			for (Measurement measurement : measurements) {
				RespMeasurement respMeasurement = mapRespMeasurement(measurement);
				if (respMeasurement != null) {
					respMeasurements.add(respMeasurement);
				}
			}

			return respMeasurements;
		}

		return null;
	}

	@Override
	public Measurement mapMeasurement(MeasurementReq measurementReq) {

		if (measurementReq != null) {

			Measurement measurement = new Measurement();
			measurement.setHeight(measurementReq.getHeight());
			measurement.setLenght(measurementReq.getLenght());
			measurement.setId(measurementReq.getId());
			measurement.setWeight(measurementReq.getWeight());
			measurement.setWidth(measurementReq.getWidth());

			return measurement;
		}

		return null;
	}

	@Override
	public RespMeasurement mapRespMeasurement(Measurement measurement) {

		if (measurement != null) {

			RespMeasurement respMeasurement = mapRespMeasurementOnly(measurement);

			return respMeasurement;
		}

		return null;
	}

	@Override
	public RespMeasurement mapRespMeasurementOnly(Measurement measurement) {

		if (measurement != null) {

			RespMeasurement respMeasurement = new RespMeasurement();
			respMeasurement.setHeight(measurement.getHeight());
			respMeasurement.setLenght(measurement.getLenght());
			respMeasurement.setId(measurement.getId());
			respMeasurement.setWeight(measurement.getWeight());
			respMeasurement.setWidth(measurement.getWidth());

			return respMeasurement;
		}
		return null;
	}

}
