package com.project.chatop.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.chatop.model.Rental;
import com.project.chatop.service.RentalService;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {
	private RentalService rentalService;
	
	public RentalController(RentalService rentalService) {
		this.rentalService = rentalService;
	}
	
	@GetMapping
	public ResponseEntity<Map<String, List<Rental>>> getAllRentals() {
		List<Rental> rentals = rentalService.getAllRentals();
		return ResponseEntity.ok(Map.of("rentals", rentals));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Optional<Rental>> getRentalById(@PathVariable Integer id) {
		Optional<Rental> rental = rentalService.getRentalById(id);
		return ResponseEntity.ok(rental);
	}
	
	@PostMapping
	public ResponseEntity<Map<String, String>> createRental(@RequestBody Rental rental) {
		rentalService.createRental(rental);
		return ResponseEntity.ok(Map.of("message", "Rental created !"));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, String>> updateRental(@PathVariable Integer id, @RequestBody Rental rental) {
		rentalService.updateRental(id, rental);
		return ResponseEntity.ok(Map.of("message", "Rental updated !"));
	}
}
