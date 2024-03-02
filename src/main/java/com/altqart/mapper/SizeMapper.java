package com.altqart.mapper;

import java.util.List;

import com.altqart.model.MeasurementStandard;
import com.altqart.model.Size;
import com.altqart.req.model.MeasurementStandardReq;
import com.altqart.req.model.SizeReq;
import com.altqart.resp.model.RespItemSize;
import com.altqart.resp.model.RespMeasurementStandard;
import com.altqart.resp.model.RespSize;

public interface SizeMapper {

	public List<RespSize> mapAllRespSize(List<Size> sizes);

	public Size mapSize(SizeReq sizeReq);

	public RespSize mapRespSize(Size size);

	public MeasurementStandard mapMeasurementStandard(MeasurementStandardReq standard);

	public List<RespMeasurementStandard> mapAllStandard(List<MeasurementStandard> standards);

	public RespMeasurementStandard mapRespStandard(MeasurementStandard measurementStandard);

	public RespSize mapRespSizeOnly(Size size);

	public RespItemSize mapRespSizeItemOnly(Size size);

}
