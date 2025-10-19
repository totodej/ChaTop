package com.project.chatop.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
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

import com.project.chatop.model.Rental;
import com.project.chatop.service.RentalService;

import io.jsonwebtoken.Claims;

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
	public ResponseEntity<RentalResponseDto> getRentalById(@PathVariable Integer id) {
		Optional<Rental> rentalOptional = rentalService.getRentalById(id);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		
	    if (rentalOptional.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    }
		
		Rental rental = rentalOptional.get();
		
		String createdAtFormatted = rental.getCreatedAt() != null ? rental.getCreatedAt().format(formatter) : null;
	    String updatedAtFormatted = rental.getUpdatedAt() != null ? rental.getUpdatedAt().format(formatter) : null;
	    
	    RentalResponseDto responseDto = new RentalResponseDto(
	            rental.getId(),
	            rental.getName(),
	            rental.getSurface(),
	            rental.getPrice(),
	            rental.getPicture(),
	            rental.getDescription(),
	            rental.getOwnerId(),
	            createdAtFormatted,
	            updatedAtFormatted
	        );
	    
	    return ResponseEntity.ok(responseDto);
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
