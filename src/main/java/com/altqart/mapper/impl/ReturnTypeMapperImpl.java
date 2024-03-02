package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.altqart.mapper.ReturnTypeMapper;
import com.altqart.model.ReturnType;
import com.altqart.resp.model.RespReturnType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReturnTypeMapperImpl implements ReturnTypeMapper {

	@Override
	public List<RespReturnType> mapAllRespReturnType(List<ReturnType> retTypes) {
		
		if(retTypes != null) {
			List<RespReturnType> returnTypes = new ArrayList<>();
			
			for (ReturnType returnType : retTypes) {
				RespReturnType respReturnType = mapRespReturnType(returnType);
				
				if(respReturnType != null) {
					returnTypes.add(respReturnType);
				}
			}
			
			return returnTypes;					
		}

		return null;
	}
	
	@Override
	public RespReturnType mapRespReturnType(ReturnType type) {
		
		RespReturnType respReturnType = mapRespReturnTypeOnly(type);
		
		if(respReturnType != null) {
			//TODO: Map Details
		}
		
		return respReturnType;
	}
	
	@Override
	public RespReturnType mapRespReturnTypeOnly(ReturnType type) {
		
		if(type != null) {
			RespReturnType respReturnType = new RespReturnType();
			
			respReturnType.setId(type.getId());
			respReturnType.setName(type.getName());
			respReturnType.setValue(type.getValue());
			
			return respReturnType;
		}
		
		return null;
	}
}
