package com.project.chatop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.chatop.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	User findByEmail(String email);
}
