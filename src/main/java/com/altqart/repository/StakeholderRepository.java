package com.altqart.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.altqart.model.Stakeholder;

public interface StakeholderRepository extends CrudRepository<Stakeholder, Integer> {

	public Optional<Stakeholder> getStakeholderByPublicId(String id);

	public Optional<Stakeholder> getStakeholderByGenId(String id);

}
