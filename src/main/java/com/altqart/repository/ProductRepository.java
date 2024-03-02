package com.altqart.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.altqart.model.Product;

public interface ProductRepository extends CrudRepository<Product, Integer> {

	public Optional<Product> getProductByPublicId(String id);

}
