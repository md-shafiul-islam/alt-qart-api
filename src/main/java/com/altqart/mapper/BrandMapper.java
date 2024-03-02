package com.altqart.mapper;

import java.util.List;

import com.altqart.model.Brand;
import com.altqart.req.model.ReqBrand;
import com.altqart.resp.model.RespBrand;
import com.altqart.resp.model.SelectOption;

public interface BrandMapper {

	public List<RespBrand> mapAllRespBrand(List<Brand> brands);

	public RespBrand mapRespBrand(Brand brand);

	public Brand mapBrand(ReqBrand reqBrand);

	public List<SelectOption> mapAllRespBrandOption(List<Brand> brands);

	public List<Brand> mapBrandAll(List<ReqBrand> reqBrand);

	public RespBrand mapBrandOnly(Brand brand);

}
