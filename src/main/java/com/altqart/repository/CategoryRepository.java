package com.altqart.repository;

import org.springframework.data.repository.CrudRepository;

import com.altqart.model.Category;

public interface CategoryRepository extends CrudRepository<Category, Integer> {

	public Category findByName(String name);

}
