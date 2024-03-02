package com.altqart.mapper;

import java.util.List;

import com.altqart.model.City;
import com.altqart.req.model.CitiesReq;
import com.altqart.req.model.CityReq;
import com.altqart.resp.model.RespCity;
import com.altqart.resp.model.RespLocOption;

public interface CityMapper {

	public List<City> mapAllCities(CitiesReq citiesReq);

	public City mapCity(CityReq cityReq);

	public RespCity mapRespCityOnly(City city);

	public RespCity mapRespCity(City city);

	public List<RespCity> mapAllRespCityOnly(List<City> cities);

	public List<RespCity> mapAllRespCity(List<City> cities);

	public List<RespLocOption> mapAllOptionCity(List<City> cities);

}
