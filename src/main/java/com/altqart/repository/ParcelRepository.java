package com.altqart.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.altqart.model.Parcel;

public interface ParcelRepository extends CrudRepository<Parcel, Integer> {

	public Optional<Parcel> getParcelByPublicId(String id);

}
