package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.altqart.mapper.SizeMapper;
import com.altqart.model.MeasurementStandard;
import com.altqart.model.Size;
import com.altqart.req.model.MeasurementStandardReq;
import com.altqart.req.model.SizeReq;
import com.altqart.resp.model.RespItemSize;
import com.altqart.resp.model.RespMeasurementStandard;
import com.altqart.resp.model.RespSize;

@Service
public class SizeMapperImpl implements SizeMapper {

	@Override
	public List<RespSize> mapAllRespSize(List<Size> sizes) {

		if (sizes != null) {

			List<RespSize> respSizes = new ArrayList<>();

			for (Size size : sizes) {
				RespSize respSize = mapRespSize(size);

				if (respSize != null) {
					respSizes.add(respSize);
				}
			}

			return respSizes;
		}

		return null;
	}

	@Override
	public Size mapSize(SizeReq sizeReq) {

		if (sizeReq != null) {
			Size size = new Size();
			size.setCount(sizeReq.getCount());
			size.setId(sizeReq.getId());
			size.setName(sizeReq.getName());
			size.setSKey(sizeReq.getValue());

			return size;
		}
		return null;
	}

	@Override
	public RespSize mapRespSize(Size size) {

		if (size != null) {
			RespSize respSize = new RespSize();
			respSize.setCount(size.getCount());
			respSize.setId(size.getId());
			respSize.setValue(size.getSKey());
			respSize.setStandard(mapRespStandard(size.getStandard()));
			respSize.setName(size.getName());

			return respSize;

		}

		return null;
	}

	@Override
	public MeasurementStandard mapMeasurementStandard(MeasurementStandardReq standard) {

		if (standard != null) {
			MeasurementStandard measurementStandard = new MeasurementStandard();
			measurementStandard.setId(standard.getId());
			measurementStandard.setKey(standard.getValue());
			measurementStandard.setName(standard.getName());

			return measurementStandard;
		}
		return null;
	}

	@Override
	public List<RespMeasurementStandard> mapAllStandard(List<MeasurementStandard> standards) {

		if (standards != null) {
			List<RespMeasurementStandard> measurementStandards = new ArrayList<>();

			for (MeasurementStandard measurementStandard : standards) {
				RespMeasurementStandard standard = mapRespStandard(measurementStandard);

				if (standard != null) {
					measurementStandards.add(standard);

				}
			}

			return measurementStandards;
		}

		return null;
	}

	@Override
	public RespMeasurementStandard mapRespStandard(MeasurementStandard measurementStandard) {

		if (measurementStandard != null) {
			RespMeasurementStandard standard = new RespMeasurementStandard();
			standard.setId(measurementStandard.getId());
			standard.setName(measurementStandard.getName());
			standard.setValue(measurementStandard.getKey());

			return standard;

		}

		return null;
	}

	@Override
	public RespSize mapRespSizeOnly(Size size) {

		if (size != null) {
			RespSize respSize = new RespSize();
			respSize.setCount(size.getCount());
			respSize.setId(size.getId());
			respSize.setName(size.getName());
			respSize.setValue(size.getSKey());

			return respSize;
		}

		return null;
	}

	@Override
	public RespItemSize mapRespSizeItemOnly(Size size) {

		if (size != null) {
			RespItemSize respSize = new RespItemSize();
			respSize.setCount(size.getCount());
			respSize.setId(size.getId());
			respSize.setName(size.getName());
			respSize.setValue(size.getSKey());

			return respSize;
		}

		return null;

	}

}
