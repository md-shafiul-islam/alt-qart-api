package com.altqart.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.altqart.helper.services.HelperServices;
import com.altqart.mapper.CityMapper;
import com.altqart.mapper.ZoneMapper;
import com.altqart.model.City;
import com.altqart.req.model.CitiesReq;
import com.altqart.req.model.CityReq;
import com.altqart.resp.model.RespCity;
import com.altqart.resp.model.RespLocOption;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CityMapperImpl implements CityMapper {

	@Autowired
	private HelperServices helperServices;

	@Autowired
	private ZoneMapper zoneMapper;

	@Override
	public List<City> mapAllCities(CitiesReq citiesReq) {

		if (citiesReq != null) {

			List<City> cities = new ArrayList<City>();

			for (CityReq cityReq : citiesReq.getCityReqs()) {

				City city = mapCity(cityReq);

				if (city != null) {
					cities.add(city);
				}
			}

			return cities;
		}

		return null;
	}

	public City mapCity(CityReq cityReq) {

		if (cityReq != null) {

			City city = new City();
			city.setCode(cityReq.getCode());

			if (helperServices.isNullOrEmpty(cityReq.getKey())) {
				city.setKey(helperServices.getKeyById(cityReq.getName(), cityReq.getPathaoCode()));
			} else {
				city.setKey(helperServices.getKey(cityReq.getKey()));
			}

			city.setName(cityReq.getName());

			city.setPathaoCode(cityReq.getPathaoCode());

			return city;
		}

		return null;
	}

	@Override
	public RespCity mapRespCityOnly(City city) {

		if (city != null) {
			RespCity respCity = new RespCity();
			respCity.setId(city.getId());
			respCity.setCode(city.getCode());
			respCity.setKey(city.getKey());
			respCity.setName(city.getName());
			respCity.setPathaoCode(city.getPathaoCode());

			return respCity;

		}
		return null;
	}

	@Override
	public RespCity mapRespCity(City city) {

		RespCity respCity = mapRespCityOnly(city);

		if (respCity != null) {
			respCity.setZones(zoneMapper.mapAllRespZoneOnly(city.getZones()));
		}
		return respCity;
	}

	@Override
	public List<RespCity> mapAllRespCity(List<City> cities) {

		if (cities != null) {
			List<RespCity> respCities = new ArrayList<>();

			for (City city : cities) {

				RespCity respCity = mapRespCityOnly(city);

				if (respCity != null) {
					respCities.add(respCity);
				}

			}

			return respCities;
		}

		return null;
	}

	@Override
	public List<RespCity> mapAllRespCityOnly(List<City> cities) {

		if (cities != null) {

			List<RespCity> respCities = new ArrayList<>();

			for (City city : cities) {
				RespCity respCity = mapRespCityOnly(city);

				if (respCity != null) {
					respCities.add(respCity);
				}
			}

			return respCities;
		}
		return null;
	}

	@Override
	public List<RespLocOption> mapAllOptionCity(List<City> cities) {

		if (cities != null) {

			List<RespLocOption> locOptions = new ArrayList<>();

			for (City city : cities) {
				RespLocOption option = mapCityOption(city);
				if (city != null) {
					locOptions.add(option);
				}

			}

			return locOptions;
		}
		return null;
	}

	private RespLocOption mapCityOption(City city) {

		if (city != null) {
			RespLocOption locOption = new RespLocOption();
			locOption.setCode(city.getId());
			locOption.setLabel(city.getName());
			locOption.setValue(city.getKey());

			return locOption;
		}
		return null;
	}

}
