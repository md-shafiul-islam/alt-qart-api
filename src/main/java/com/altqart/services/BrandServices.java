package com.altqart.services;

import java.util.List;
import java.util.Map;

import com.altqart.model.Brand;
import com.altqart.req.model.ReqBrand;

public interface BrandServices {

	public List<Brand> getAllBrand();

	public Brand getBrandByPublicId(String id);

	public boolean addBrand(ReqBrand brand, Map<String, Object> map);

	public boolean update(ReqBrand reqBrand, Map<String, Object> map);

	public Brand getBrandByAliasName(String brand);

	public boolean addAllBrand(List<Brand> brands);

	public Brand getBrandByKey(String key);

	public void getAllRespBrand(Map<String, Object> map, int start, int size);

	public void removeBrand(ReqBrand brandReq, Map<String, Object> map);

}
