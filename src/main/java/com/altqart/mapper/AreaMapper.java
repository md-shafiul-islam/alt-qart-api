package com.altqart.mapper;

import java.util.List;
import java.util.Map;

import com.altqart.model.Area;
import com.altqart.req.model.AreaReq;
import com.altqart.req.model.AreasReq;
import com.altqart.req.model.AreasZReq;
import com.altqart.resp.model.RespArea;
import com.altqart.resp.model.RespLocOption;

public interface AreaMapper {

	public List<Area> mapAllArea(AreasReq areasReq);
	
	public Area mapArea(AreaReq areaReq);

	public List<RespArea> mapAllRespArea(List<Area> areas, Map<String, Object> map);

	public RespArea mapRespArea(Area area, Map<String, Object> map);

	public List<RespArea> mapAllRespAreaOnly(List<Area> areas);

	public RespArea mapRespAreaOnly(Area area);

	public List<Area> mapAllZArea(AreasZReq areasReq);

	public List<RespLocOption> mapAllRespOptionArea(List<Area> resultList);

}
