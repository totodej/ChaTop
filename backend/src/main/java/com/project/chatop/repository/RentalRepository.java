package com.project.chatop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.chatop.model.Rental;

public interface RentalRepository extends JpaRepository<Rental, Integer> {

}
