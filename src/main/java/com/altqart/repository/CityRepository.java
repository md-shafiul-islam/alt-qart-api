package com.altqart.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.altqart.model.City;

public interface CityRepository extends CrudRepository<City, Integer>{

	public Optional<City> findByPathaoCode(int pathaoCode);

	public Optional<City> getCityByKey(String city);

}
