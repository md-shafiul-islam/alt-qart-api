package com.altqart.services;

import java.util.Map;

import com.altqart.model.City;
import com.altqart.req.model.CitiesReq;
import com.altqart.req.model.CityReq;

public interface CitiyServices {

	public void getAll(Map<String, Object> map, int start, int size, int type);

	public void add(CityReq cityReq, Map<String, Object> map);

	public void remove(CityReq cityReq, Map<String, Object> map);

	public void update(CityReq cityReq, Map<String, Object> map);

	public void addAll(CitiesReq citiesReq, Map<String, Object> map);

	public City getCityById(int city);

	public City getCityByPathaoCode(int pathaoCode);

	public void getOne(int id, Map<String, Object> map);

	public City getCityByKey(String city);

}
