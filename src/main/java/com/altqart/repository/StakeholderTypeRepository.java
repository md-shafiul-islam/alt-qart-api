package com.altqart.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.altqart.model.StakeholderType;

public interface StakeholderTypeRepository extends CrudRepository<StakeholderType, Integer> {

	public Optional<StakeholderType> getStakeholderByKey(String key);

}
