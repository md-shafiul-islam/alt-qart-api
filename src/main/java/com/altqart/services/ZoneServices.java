package com.altqart.services;

import java.util.Map;

import com.altqart.model.Zone;
import com.altqart.req.model.ZoneReq;
import com.altqart.req.model.ZonesCityReq;
import com.altqart.req.model.ZonesReq;

public interface ZoneServices {

	public void getAll(Map<String, Object> map, int start, int size);

	public void add(ZoneReq zoneReq, Map<String, Object> map);

	public void remove(ZoneReq zoneReq, Map<String, Object> map);

	public void update(ZoneReq zoneReq, Map<String, Object> map);

	public void getOne(int id, Map<String, Object> map);

	public void addAll(ZonesReq zonesReq, Map<String, Object> map);

	public Zone getZoneById(int zone);

	public Zone getZoneByPathaoId(int pathaoCode);

	public void addCityZoneAll(ZonesCityReq zonesReq, Map<String, Object> map);

	public void getAllByCity(int id, Map<String, Object> map);

	public Zone getZoneByKey(String zone);

}
