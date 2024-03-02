package com.altqart.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.altqart.model.User;

public interface UserRepositry extends CrudRepository<User, Integer> {

	public User getUserByPublicId(String userId);

	public Optional<User> findUserByPublicId(String userId);

}
