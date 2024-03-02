package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.AreaMapper;
import com.altqart.mapper.ZoneMapper;
import com.altqart.model.Area;
import com.altqart.model.Zone;
import com.altqart.req.model.AreaReq;
import com.altqart.req.model.AreaZReq;
import com.altqart.req.model.AreasReq;
import com.altqart.req.model.AreasZReq;
import com.altqart.resp.model.RespArea;
import com.altqart.resp.model.RespLocOption;
import com.altqart.services.ZoneServices;

@Service
public class AreaMapperImpl implements AreaMapper {

	@Autowired
	private HelperServices helperServices;

	@Autowired
	private ZoneServices zoneServices;

	@Autowired
	private ZoneMapper zoneMapper;

	@Override
	public List<Area> mapAllArea(AreasReq areasReq) {

		if (areasReq != null) {
			List<Area> areas = new ArrayList<>();
			Zone zone = zoneServices.getZoneByPathaoId(areasReq.getZone());

			for (AreaReq areaReq : areasReq.getAreaReqs()) {

				Area area = mapAreaViaAll(areaReq);

				if (area != null) {
					area.setZone(zone);
					areas.add(area);
				}
			}

			return areas;
		}

		return null;
	}

	private Area mapAreaViaAll(AreaReq areaReq) {
		if (areaReq != null) {

			Area area = new Area();

			area.setName(areaReq.getName());
			area.setPathaoCode(areaReq.getPathaoCode());
			if (!helperServices.isNullOrEmpty(areaReq.getValue())) {
				area.setValue(areaReq.getValue());
			} else {
				area.setValue(helperServices.getKeyById(areaReq.getName(), areaReq.getPathaoCode()));
			}

			return area;

		}

		return null;
	}

	@Override
	public Area mapArea(AreaReq areaReq) {

		if (areaReq != null) {

			Area area = new Area();

			area.setName(areaReq.getName());
			area.setPathaoCode(areaReq.getPathaoCode());
			if (!helperServices.isNullOrEmpty(areaReq.getValue())) {
				area.setValue(areaReq.getValue());
			} else {
				area.setValue(helperServices.getKeyById(areaReq.getName(), areaReq.getPathaoCode()));
			}

			if (areaReq.getZone() > 0) {
				area.setZone(zoneServices.getZoneById(areaReq.getZone()));
			} else {
				area.setZone(zoneServices.getZoneByPathaoId(areaReq.getPathaoCode()));
			}

			return area;

		}

		return null;
	}

	@Override
	public List<RespArea> mapAllRespArea(List<Area> areas, Map<String, Object> map) {

		if (areas != null) {

			List<RespArea> respAreas = new ArrayList<>();
			for (Area area : areas) {
				RespArea respArea = mapRespArea(area, map);

				if (respArea != null) {
					respAreas.add(respArea);
				}
			}
		}

		return null;
	}

	@Override
	public RespArea mapRespArea(Area area, Map<String, Object> map) {

		if (area != null) {
			RespArea respArea = new RespArea();
			respArea.setId(area.getId());
			respArea.setName(area.getName());
			respArea.setPathaoCode(area.getPathaoCode());
			respArea.setValue(area.getValue());
			respArea.setZone(zoneMapper.mapRespZoneAndParentOnly(area.getZone()));

			return respArea;
		}

		return null;
	}

	@Override
	public List<RespArea> mapAllRespAreaOnly(List<Area> areas) {

		if (areas != null) {

			List<RespArea> respAreas = new ArrayList<>();

			for (Area area : areas) {
				RespArea respArea = mapRespAreaOnly(area);

				if (respArea != null) {
					respAreas.add(respArea);
				}
			}
		}

		return null;
	}

	@Override
	public RespArea mapRespAreaOnly(Area area) {

		if (area != null) {
			RespArea respArea = new RespArea();
			respArea.setId(area.getId());
			respArea.setName(area.getName());
			respArea.setPathaoCode(area.getPathaoCode());
			respArea.setValue(area.getValue());

			return respArea;
		}
		return null;
	}

	@Override
	public List<Area> mapAllZArea(AreasZReq areasReq) {

		if (areasReq != null) {

			if (areasReq.getAreaReqs() != null) {

				List<Area> areas = new ArrayList<>();

				for (AreaZReq areaReq : areasReq.getAreaReqs()) {

					Area area = mapAreaByZ(areaReq);

					if (area != null) {
						areas.add(area);
					}

				}

				return areas;
			}

		}

		return null;
	}

	@Override
	public List<RespLocOption> mapAllRespOptionArea(List<Area> areas) {

		if (areas != null) {
			List<RespLocOption> options = new ArrayList<>();

			for (Area area : areas) {
				RespLocOption option = mapRespOptionArea(area);

				if (option != null) {
					options.add(option);
				}
			}

			return options;
		}
		return null;
	}

	private RespLocOption mapRespOptionArea(Area area) {

		if (area != null) {
			RespLocOption option = new RespLocOption();
			option.setCode(area.getId());
			option.setLabel(area.getName());
			option.setValue(area.getValue());

			return option;
		}

		return null;
	}

	private Area mapAreaByZ(AreaZReq areaReq) {

		if (areaReq != null) {
			Area area = new Area();
			area.setName(areaReq.getName());
			area.setPathaoCode(areaReq.getPathaoCode());
			area.setValue(helperServices.getKeyById(areaReq.getName(), areaReq.getPathaoCode()));
			area.setZone(zoneServices.getZoneByPathaoId(areaReq.getZone()));

			return area;
		}
		return null;
	}

}
