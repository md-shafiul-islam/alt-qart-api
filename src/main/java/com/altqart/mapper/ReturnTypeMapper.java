package com.altqart.mapper;

import java.util.List;

import com.altqart.model.ReturnType;
import com.altqart.resp.model.RespReturnType;

public interface ReturnTypeMapper {

	public List<RespReturnType> mapAllRespReturnType(List<ReturnType> retTypes);
	
	public RespReturnType mapRespReturnTypeOnly(ReturnType type);
	
	public RespReturnType mapRespReturnType(ReturnType type);

}
