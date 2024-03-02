package com.altqart.mapper;

import java.util.List;

import com.altqart.model.Specification;
import com.altqart.resp.model.RespSpecification;

public interface SpecificationMapper {

	public List<RespSpecification> mapRespSpecifications(List<Specification> specifications);
	
	public RespSpecification mapRespSpecificationOnly(Specification specification);

}
