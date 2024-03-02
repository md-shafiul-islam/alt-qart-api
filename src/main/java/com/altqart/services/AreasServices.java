package com.altqart.services;

import java.util.Map;

import com.altqart.model.Area;
import com.altqart.req.model.AreaReq;
import com.altqart.req.model.AreasReq;
import com.altqart.req.model.AreasZReq;

public interface AreasServices {

	public void update(AreaReq areasReq, Map<String, Object> map);

	public void getAll(Map<String, Object> map, int start, int size);

	public void add(AreaReq areaReq, Map<String, Object> map);

	public void remove(AreaReq areasReq, Map<String, Object> map);

	public void getOne(int id, Map<String, Object> map);

	public void addAll(AreasReq areasReq, Map<String, Object> map);

	public void addZAll(AreasZReq areasReq, Map<String, Object> map);

	public void getAllAreaByZone(int id, Map<String, Object> map);

	public Area getAreaByKey(String zone);

}
