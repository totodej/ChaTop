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
import com.project.chatop.exception.ForbiddenException;
import com.project.chatop.exception.ResourceNotFoundException;
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
	
	/*
	 Récupère toutes les locations et les transforme en DTO pour l'affichage.
	*/
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
	
	/*
	 Récupère une location par son ID.
	*/
	public RentalDto getRentalById(Integer id) {
		Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found"));

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
		
		return rentalDto;
	}
	
	/*
	 Crée une nouvelle location et associe l'utilisateur courant comme propriétaire.
	 Gère le téléchargement d'image si fourni.
	*/
	public Rental createRental(MultipartFile picture, Rental rental) {
		LocalDateTime currentDate = LocalDateTime.now();
		
		Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer currentUserId = claims.get("userId", Integer.class);
        
        rental.setOwnerId(currentUserId);
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
	
	/*
	 Met à jour une location existante.
	 Vérifie que l'utilisateur courant est bien le propriétaire.
	*/
	public RentalDto updateRental(Integer id, Rental rental) {
		LocalDateTime currentDate = LocalDateTime.now();
		
		Rental existingRental = rentalRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Rental not found"));
		
		Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer currentUserId = claims.get("userId", Integer.class);
		
		if (!existingRental.getOwnerId().equals(currentUserId)) {
		    throw new ForbiddenException("You can only modify your own rentals.");
		}

		existingRental.setName(rental.getName());
		existingRental.setSurface(rental.getSurface());
		existingRental.setPrice(rental.getPrice());
		existingRental.setDescription(rental.getDescription());
		existingRental.setUpdatedAt(currentDate);
		
		Rental updatedRental = rentalRepository.save(existingRental);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		
		RentalDto rentalDto = new RentalDto(
				updatedRental.getId(),
				updatedRental.getName(),
				updatedRental.getSurface(),
				updatedRental.getPrice(),
				updatedRental.getPicture(),
				updatedRental.getDescription(),
				updatedRental.getOwnerId(),
				updatedRental.getCreatedAt() != null ? updatedRental.getCreatedAt().format(formatter) : null,
				updatedRental.getUpdatedAt() != null ? updatedRental.getUpdatedAt().format(formatter) : null);
		
		return rentalDto;
	}
} 
