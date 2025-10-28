package com.project.chatop.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.chatop.dto.RentalDto;
import com.project.chatop.model.Rental;
import com.project.chatop.service.RentalService;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/rentals")
@SecurityRequirement(name = "bearerAuth")
public class RentalController {
	private RentalService rentalService;
	
	public RentalController(RentalService rentalService) {
		this.rentalService = rentalService;
	}
	
	@GetMapping
	public ResponseEntity<Map<String, List<RentalDto>>> getAllRentals() {
		List<RentalDto> rentals = rentalService.getAllRentals();

		return ResponseEntity.ok(Map.of("rentals", rentals));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<RentalDto> getRentalById(@PathVariable Integer id) {
		Optional<RentalDto> rentalOptional = rentalService.getRentalById(id);
		
	    if (rentalOptional.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    }
		
		RentalDto rental = rentalOptional.get();
		
	    return ResponseEntity.ok(rental);
	}
	
	@PostMapping
	public ResponseEntity<Map<String, String>> createRental(
			@RequestParam("name") String name, 
			@RequestParam("surface") Integer surface, 
			@RequestParam("price") float price,
			@RequestParam("picture") MultipartFile picture,
			@RequestParam("description") String description) throws IOException {

	    Rental rental = new Rental();
	    rental.setName(name);
	    rental.setSurface(surface);
	    rental.setPrice(price);
	    rental.setDescription(description);

	    Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    rental.setOwnerId(claims.get("userId", Integer.class));

	    rentalService.createRental(picture, rental);

	    return ResponseEntity.ok(Map.of("message", "Rental created !"));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, String>> updateRental(@PathVariable Integer id, @RequestBody Rental rental) {
		rentalService.updateRental(id, rental);
		
		return ResponseEntity.ok(Map.of("message", "Rental updated !"));
	}
}
