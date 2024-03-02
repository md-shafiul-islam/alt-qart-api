package com.altqart.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.altqart.model.Order;

public interface OrderRepository extends CrudRepository<Order, Integer> {

	public Optional<Order> getOrderByPublicId(String id);

}
