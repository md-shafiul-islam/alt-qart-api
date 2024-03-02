package com.altqart.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.altqart.model.Area;

public interface AreaRepsoitory extends CrudRepository<Area, Integer>{

	public Optional<Area> getAreaByValue(String key);

}
