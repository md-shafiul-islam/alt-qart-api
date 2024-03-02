package com.altqart.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.altqart.model.Zone;

public interface ZoneRepository extends CrudRepository<Zone, Integer> {

	public Optional<Zone> findByPathaoCode(int pathaoCode);

	public Optional<Zone> getZoneByValue(String zone);

}
