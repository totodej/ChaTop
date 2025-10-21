package com.project.chatop.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.chatop.dto.RentalDto;
import com.project.chatop.model.Rental;
import com.project.chatop.repository.RentalRepository;

import io.jsonwebtoken.Claims;

@Service
public class RentalService {
	
	private final RentalRepository rentalRepository;
	private final FileService fileService;
	
	public RentalService(RentalRepository rentalRepository, FileService fileService) {
		this.rentalRepository = rentalRepository;
		this.fileService = fileService;
	}
	
	public List<RentalDto> getAllRentals() {
		List<Rental> rentals = rentalRepository.findAll();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		
		return rentals.stream().map(rental -> new RentalDto(
				rental.getId(), 
				rental.getName(),
				rental.getSurface(),
				rental.getPrice(),
				rental.getPicture(),
				rental.getDescription(),
				rental.getOwnerId(),
				rental.getCreatedAt() != null ? rental.getCreatedAt().format(formatter) : null,
				rental.getUpdatedAt() != null ? rental.getUpdatedAt().format(formatter) : null
				))
				.toList();
	}
	
	public Optional<RentalDto> getRentalById(Integer id) {
		Optional<Rental> rentalOptional = rentalRepository.findById(id);
		
		if (rentalOptional.isEmpty()) {
	        return Optional.empty();
	    }
		
		Rental rental = rentalOptional.get();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		
		RentalDto rentalDto = new RentalDto(
				rental.getId(),
				rental.getName(),
				rental.getSurface(),
				rental.getPrice(), 
				rental.getPicture(), 
				rental.getDescription(),
				rental.getOwnerId(), 
				rental.getCreatedAt() != null ? rental.getCreatedAt().format(formatter) : null,
				rental.getUpdatedAt() != null ? rental.getUpdatedAt().format(formatter): null);
		
		return Optional.of(rentalDto);
	}
	
	public Rental createRental(MultipartFile picture, Rental rental) {
		LocalDateTime currentDate = LocalDateTime.now();
		
		Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer ownerId = claims.get("userId", Integer.class);
        
        rental.setOwnerId(ownerId);
        rental.setCreatedAt(currentDate);
		rental.setUpdatedAt(currentDate);
		
		String altText = "Photo non disponible";
		
        if (picture != null && !picture.isEmpty()) {
            try {
                String fileUrl = fileService.save(picture);
                rental.setPicture(fileUrl);
            } catch (IOException e) {
                e.printStackTrace();
                rental.setPicture(altText);
            }
        } else {
            rental.setPicture(altText);
        }

		return rentalRepository.save(rental);
	}
	
	public RentalDto updateRental(Integer id, Rental rental) {
		LocalDateTime currentDate = LocalDateTime.now();
		Rental existingRental = rentalRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Rental not found"));

		existingRental.setName(rental.getName());
		existingRental.setSurface(rental.getSurface());
		existingRental.setPrice(rental.getPrice());
		existingRental.setPicture(rental.getPicture());
		existingRental.setDescription(rental.getDescription());
		existingRental.setOwnerId(rental.getOwnerId()); 
		existingRental.setUpdatedAt(currentDate);
		
		Rental updatedRental = rentalRepository.save(existingRental);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		
		return new RentalDto(
				updatedRental.getId(),
				updatedRental.getName(),
				updatedRental.getSurface(),
				updatedRental.getPrice(),
				updatedRental.getPicture(),
				updatedRental.getDescription(),
				updatedRental.getOwnerId(),
				updatedRental.getCreatedAt() != null ? updatedRental.getCreatedAt().format(formatter) : null,
				updatedRental.getUpdatedAt() != null ? updatedRental.getUpdatedAt().format(formatter) : null);
	}
	
} 
