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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
public class RentalController {

	@Autowired
	private RentalService rentalService;

	private static final String schemaExample = "{ \"name\": \"My rental\", \"surface\": 25.5, \"price\": 255.55, \"picture\": \"LINK_URL\", \"description\": \"A description\", \"owner\": { \"id\": 0} }";

	@Operation(summary = "Create a new rental")
	@PostMapping("/rental")
	public Rental createRental(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class), examples = @ExampleObject(value = schemaExample))) 
			@RequestBody Rental rental) {
		return rentalService.saveRental(rental);
	}

	@Operation(summary = "Get one rental by id")
	@GetMapping("/rental/{id}")
	public Rental getRental(@PathVariable("id") final Long id) {
		Optional<Rental> rental = rentalService.getRental(id);
		if (rental.isPresent()) {
			return rental.get();
		} else {
			return null;
		}
	}

	@Operation(summary = "Get all rentals")
	@GetMapping("/rentals")
	public Iterable<Rental> getRentals() {
		return rentalService.getRentals();
	}

	@Operation(summary = "Update an existing rental")
	@PutMapping("/rental/{id}")
	public Rental updateRental(
		@PathVariable("id") final Long id, 
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class), examples = @ExampleObject(value = schemaExample)))
		@RequestBody Rental rental ) {
		Optional<Rental> r = rentalService.getRental(id);

		if (r.isPresent()) {
			Rental currentRental = r.get();

			String name = rental.getName();
			if (name != null) {
				currentRental.setName(name);
			}

			Float surface = rental.getSurface();
			if (surface != null) {
				currentRental.setSurface(surface);
			}

			Float price = rental.getPrice();
			if (price != null) {
				currentRental.setPrice(price);
			}

			String picture = rental.getPicture();
			if (picture != null) {
				currentRental.setPicture(picture);
			}

			String description = rental.getDescription();
			if (description != null) {
				currentRental.setDescription(description);
			}

			User owner = rental.getOwner();
			if (owner != null) {
				currentRental.setOwner(owner);
			}

			rentalService.saveRental(currentRental);

			return currentRental;
		} else {
			return null;
		}
	}

	@Operation(summary = "Delete a rental")
	@DeleteMapping("/rental/{id}")
	public void deleteRental(@PathVariable("id") final Long id) {
		rentalService.deleteRental(id);
	}
}
