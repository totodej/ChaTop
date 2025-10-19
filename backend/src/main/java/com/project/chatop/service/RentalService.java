package com.project.chatop.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
	
	public List<Rental> getAllRentals() {
		return rentalRepository.findAll();
	}
	
	public Optional<Rental> getRentalById(Integer id) {
		return rentalRepository.findById(id);
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
