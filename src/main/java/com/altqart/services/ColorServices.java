package com.altqart.services;

import java.util.Map;

import com.altqart.req.model.ColorReq;

public interface ColorServices {

	public void getAllColor(Map<String, Object> map, int start, int size);

	public void add(ColorReq colorReq, Map<String, Object> map);

	public void update(ColorReq colorReq, Map<String, Object> map);

	public void remove(ColorReq colorReq, Map<String, Object> map);

	public void getOneById(int id, Map<String, Object> map);

}
