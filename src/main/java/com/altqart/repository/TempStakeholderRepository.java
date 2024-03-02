package com.altqart.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.altqart.model.TempStakeholder;

public interface TempStakeholderRepository extends CrudRepository<TempStakeholder, Integer> {

	public Optional<TempStakeholder> getTempStakeholderByPublicIdAndActive(String publicId, boolean b);

}
