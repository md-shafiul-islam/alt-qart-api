package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.AreaMapper;
import com.altqart.mapper.CityMapper;
import com.altqart.mapper.ZoneMapper;
import com.altqart.model.City;
import com.altqart.model.Zone;
import com.altqart.req.model.ZoneCityReq;
import com.altqart.req.model.ZoneReq;
import com.altqart.req.model.ZonesCityReq;
import com.altqart.req.model.ZonesReq;
import com.altqart.resp.model.RespLocOption;
import com.altqart.resp.model.RespNameCode;
import com.altqart.resp.model.RespZone;
import com.altqart.services.CitiyServices;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ZoneMapperImpl implements ZoneMapper {

	@Autowired
	private HelperServices helperServices;

	@Autowired
	private CitiyServices citiyServices;

	@Autowired
	private CityMapper cityMapper;

	@Autowired
	private AreaMapper areaMapper;

	@Override
	public List<Zone> mapAllZone(ZonesReq zonesReq) {

		if (zonesReq != null) {
			List<Zone> zones = new ArrayList<>();

			City city = citiyServices.getCityByPathaoCode(zonesReq.getCity());
			for (ZoneReq zoneReq : zonesReq.getZoneReqs()) {

				Zone zone = mapZoneAll(zoneReq);

				if (zone != null) {

					zone.setCity(city);

					zones.add(zone);
				}

			}
			return zones;
		}

		return null;
	}

	@Override
	public Zone mapZone(ZoneReq zoneReq) {

		if (zoneReq != null) {

			Zone zone = new Zone();
			zone.setId(zoneReq.getId());
			zone.setName(zoneReq.getName());
			zone.setPathaoCode(zoneReq.getPathaoCode());
			zone.setValue(zoneReq.getValue());
			if (helperServices.isNullOrEmpty(zoneReq.getValue())) {
				zone.setValue(helperServices.getKeyById(zoneReq.getName(), zoneReq.getPathaoCode()));
			}
			if (zoneReq.getCity() > 0) {
				zone.setCity(citiyServices.getCityById(zoneReq.getCity()));
			} else {
				zone.setCity(citiyServices.getCityByPathaoCode(zoneReq.getPathaoCode()));
			}

			return zone;

		}

		return null;
	}

	@Override
	public RespZone mapRespZoneOnly(Zone zone) {

		if (zone != null) {

			RespZone respZone = new RespZone();
			respZone.setId(zone.getId());
			respZone.setName(zone.getName());
			respZone.setPathaoCode(zone.getPathaoCode());
			respZone.setValue(zone.getValue());

			if (zone.getCity() != null) {
				respZone.setCity(cityMapper.mapRespCityOnly(zone.getCity()));
			}

			return respZone;
		}
		return null;
	}

	@Override
	public RespZone mapRespZoneAndParentOnly(Zone zone) {

		RespZone respZone = mapRespZoneOnly(zone);

		if (respZone != null) {
			respZone.setCity(cityMapper.mapRespCityOnly(zone.getCity()));

		}

		return respZone;
	}

	@Override
	public RespZone mapRespZoneAndParent(Zone zone) {

		RespZone respZone = mapRespZoneAndParentOnly(zone);

		if (respZone != null) {
			respZone.setAreas(areaMapper.mapAllRespAreaOnly(zone.getAreas()));
		}

		return respZone;
	}

	@Override
	public List<RespZone> mapAllRespZoneOnly(List<Zone> zones) {

		if (zones != null) {

			List<RespZone> respZones = new ArrayList<>();

			for (Zone zone : zones) {
				RespZone respZone = mapRespZoneOnly(zone);

				if (respZone != null) {
					respZones.add(respZone);
				}
			}

			return respZones;
		}
		return null;
	}

	public Zone mapZoneAll(ZoneReq zoneReq) {

		if (zoneReq != null) {

			Zone zone = new Zone();
			zone.setId(zoneReq.getId());
			zone.setName(zoneReq.getName());
			zone.setPathaoCode(zoneReq.getPathaoCode());
			zone.setValue(zoneReq.getValue());
			if (helperServices.isNullOrEmpty(zoneReq.getValue())) {
				zone.setValue(helperServices.getKeyById(zoneReq.getName(), zoneReq.getPathaoCode()));
			}

			return zone;

		}

		return null;
	}

	@Override
	public List<Zone> mapZoneAll(ZonesCityReq zonesReq) {

		if (zonesReq != null) {

			if (zonesReq.getZones() != null) {
				List<Zone> zones = new ArrayList<>();

				for (ZoneCityReq zoneReq : zonesReq.getZones()) {

					Zone zone = mapZoneByCity(zoneReq);

					if (zone != null) {
						zones.add(zone);
					}
				}

				return zones;

			}

		}
		return null;
	}

	@Override
	public List<RespLocOption> mapAllRespZoneOption(List<Zone> zones) {

		if (zones != null) {

			List<RespLocOption> options = new ArrayList<>();

			for (Zone zone : zones) {
				RespLocOption option = mapRespOption(zone);

				if (option != null) {
					options.add(option);
				}
			}

			return options;
		}

		return null;
	}

	@Override
	public RespNameCode mapNameCode(Zone zone) {

		if (zone != null) {
			RespNameCode code = new RespNameCode();
			code.setCode(zone.getPathaoCode());
			code.setId(zone.getId());
			code.setName(zone.getName());
			return code;
		}

		return null;
	}

	private RespLocOption mapRespOption(Zone zone) {

		if (zone != null) {
			RespLocOption locOption = new RespLocOption();
			locOption.setCode(zone.getId());
			locOption.setLabel(zone.getName());
			locOption.setValue(zone.getValue());
			return locOption;
		}

		return null;
	}

	private Zone mapZoneByCity(ZoneCityReq zoneReq) {

		if (zoneReq != null) {

			Zone zone = new Zone();
			zone.setName(zoneReq.getName());
			zone.setPathaoCode(zoneReq.getPathaoCode());
			if(!helperServices.isNullOrEmpty(zoneReq.getValue())) {
				zone.setValue(zoneReq.getValue());
			}else {
				zone.setValue(helperServices.getKeyById(zoneReq.getName(), zoneReq.getPathaoCode()));
			}
			

			log.info("City Zone " + zone.getCity());

			log.info("zoneReq.getName() " + zoneReq.getName());

			if (zoneReq.getCity() != null) {
				City city = citiyServices.getCityByPathaoCode(zoneReq.getCity().getPathaoCode());
				zone.setCity(city);
			}

			return zone;
		}
		return null;
	}

}
