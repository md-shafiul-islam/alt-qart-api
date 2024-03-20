package com.altqart.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.altqart.model.Status;

public interface StatusRepository extends CrudRepository<Status, Integer> {

	public Optional<Status> getStatusByValue(String statusKey);

}
