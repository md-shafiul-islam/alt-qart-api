package com.altqart.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.altqart.model.ParcelStatus;

public interface ParcelStatusRepository extends CrudRepository<ParcelStatus, Integer> {

	public Optional<ParcelStatus> getParcelStatusBySlug(String orderStatus);

	public Optional<ParcelStatus> getParcelStatusByPathaoKey(String orderStatus);

}
