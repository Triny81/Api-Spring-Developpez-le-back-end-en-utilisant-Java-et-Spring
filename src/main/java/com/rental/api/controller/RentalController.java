package com.rental.api.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rental.api.model.Rental;
import com.rental.api.model.User;
import com.rental.api.service.RentalService;

@RestController
public class RentalController {
    
    @Autowired
    private RentalService rentalService;

    /**
	 * Create - Add a new rental
	 * @param rental An object rental
	 * @return The rental object saved
	 */
	@PostMapping("/rental")
	public Rental createRental(@RequestBody Rental rental) {
		return rentalService.saveRental(rental);
	}
	
	/**
	 * Read - Get one rental 
	 * @param id The id of the rental
	 * @return An Rental object full filled
	 */
	@GetMapping("/rental/{id}")
	public Rental getRental(@PathVariable("id") final Long id) {
		Optional<Rental> rental = rentalService.getRental(id);
		if(rental.isPresent()) {
			return rental.get();
		} else {
			return null;
		}
	}
	
	/**
	 * Read - Get all rentals
	 * @return - An Iterable object of Rental full filled
	 */
	@GetMapping("/rentals")
	public Iterable<Rental> getRentals() {
		return rentalService.getRentals();
	}
	
	/**
	 * Update - Update an existing rental
	 * @param id - The id of the rental to update
	 * @param rental - The rental object updated
	 * @return
	 */
	@PutMapping("/rental/{id}")
	public Rental updateRental(@PathVariable("id") final Long id, @RequestBody Rental rental) {
		Optional<Rental> r = rentalService.getRental(id);

		if(r.isPresent()) {
			Rental currentRental = r.get();

			String name = rental.getName();
			if(name != null) {
				currentRental.setName(name);
			}

			Float surface = rental.getSurface();
			if(surface != null) {
				currentRental.setSurface(surface);
			}

            Float price = rental.getPrice();
			if(price != null) {
				currentRental.setPrice(price);
			}

			String picture = rental.getPicture();
			if(picture != null) {
				currentRental.setPicture(picture);
			}

            String description = rental.getDescription();
			if(description != null) {
				currentRental.setDescription(description);
			}

            User owner = rental.getOwner();
			if(owner != null) {
				currentRental.setOwner(owner);
			}

			rentalService.saveRental(currentRental);

			return currentRental;
		} else {
			return null;
		}
	}
	
	
	/**
	 * Delete - Delete an rental
	 * @param id - The id of the rental to delete
	 */
	@DeleteMapping("/rental/{id}")
	public void deleteRental(@PathVariable("id") final Long id) {
		rentalService.deleteRental(id);
	}
}
