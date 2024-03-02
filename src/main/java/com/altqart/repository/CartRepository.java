package com.altqart.repository;

import org.springframework.data.repository.CrudRepository;

import com.altqart.model.Cart;

public interface CartRepository extends CrudRepository<Cart, Integer> {

}
