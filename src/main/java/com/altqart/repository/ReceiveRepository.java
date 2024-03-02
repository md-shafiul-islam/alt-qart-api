package com.altqart.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.altqart.model.Receive;

public interface ReceiveRepository extends CrudRepository<Receive, Integer> {

	public Optional<Receive> getReceiveByPublicId(String id);

}
