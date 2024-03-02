package com.altqart.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.altqart.model.Brand;

public interface BrandRepository extends CrudRepository<Brand, Integer> {

	public Optional<Brand> getBrandByPublicId(String id);

}
