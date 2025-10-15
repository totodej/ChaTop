package com.project.chatop.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.project.chatop.model.Rental;
import com.project.chatop.repository.RentalRepository;

@Service
public class RentalService {
	
	private final RentalRepository rentalRepository;
	
	public RentalService(RentalRepository rentalRepository) {
		this.rentalRepository = rentalRepository;
	}
	
	public List<Rental> getAllRentals() {
		return rentalRepository.findAll();
	}
	
	public Optional<Rental> getRentalById(Integer id) {
		return rentalRepository.findById(id);
	}
	
	public Rental createRental(Rental rental) {
		LocalDateTime currentDate = LocalDateTime.now();
		rental.setCreatedAt(currentDate);
		rental.setUpdatedAt(currentDate);
		return rentalRepository.save(rental);
	}
	
	public Rental updateRental(Integer id, Rental rental) {
		LocalDateTime currentDate = LocalDateTime.now();
		Rental existingRental = getRentalById(id).get();
		existingRental.setName(rental.getName());
		existingRental.setSurface(rental.getSurface());
		existingRental.setPrice(rental.getPrice());
		existingRental.setPicture(rental.getPicture());
		existingRental.setDescription(rental.getDescription());
		existingRental.setOwnerId(rental.getOwnerId()); 
		existingRental.setUpdatedAt(currentDate);
		
		return rentalRepository.save(existingRental);
	}
} 
