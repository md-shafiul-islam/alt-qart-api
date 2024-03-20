package com.altqart.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.altqart.client.model.PathaoToken;

public interface PathaoTokenRepository extends CrudRepository<PathaoToken, Integer> {

	public Optional<PathaoToken> getPathaoTokenByKey(String key);

}
