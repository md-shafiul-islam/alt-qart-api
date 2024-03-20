package com.altqart.mapper;

import java.util.List;

import com.altqart.model.Zone;
import com.altqart.req.model.ZoneReq;
import com.altqart.req.model.ZonesCityReq;
import com.altqart.req.model.ZonesReq;
import com.altqart.resp.model.RespLocOption;
import com.altqart.resp.model.RespNameCode;
import com.altqart.resp.model.RespZone;

public interface ZoneMapper {

	public List<Zone> mapAllZone(ZonesReq zonesReq);

	public Zone mapZone(ZoneReq zoneReq);

	public RespZone mapRespZoneAndParentOnly(Zone zone);

	public RespZone mapRespZoneAndParent(Zone zone);

	public RespZone mapRespZoneOnly(Zone zone);

	public List<RespZone> mapAllRespZoneOnly(List<Zone> zones);

	public List<Zone> mapZoneAll(ZonesCityReq zonesReq);

	public List<RespLocOption> mapAllRespZoneOption(List<Zone> zones);

	public RespNameCode mapNameCode(Zone zone);

}
