package com.altqart.services;

import java.util.Map;

import com.altqart.req.model.MaterialReq;

public interface MaterialServices {

	public void getAllMaterial(Map<String, Object> map, int start, int size);

	public void add(MaterialReq materialReq, Map<String, Object> map);

	public void getOneById(int id, Map<String, Object> map);

	public void remove(MaterialReq materialReq, Map<String, Object> map);

	public void update(MaterialReq materialReq, Map<String, Object> map);

}
