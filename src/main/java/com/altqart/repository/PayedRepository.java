package com.altqart.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.altqart.model.Paid;

public interface PayedRepository extends CrudRepository<Paid, Integer> {

	public Optional<Paid> getPaidByPublicId(String id);

}
