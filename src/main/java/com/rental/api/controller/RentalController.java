package com.rental.api.controller;

import java.util.ArrayList;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.rental.api.dto.RentalDTO;
import com.rental.api.model.Rental;
import com.rental.api.model.RentalFormWrapper;
import com.rental.api.service.RentalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
public class RentalController {

	@Autowired
	private RentalService rentalService;

	@Autowired
    private ModelMapper modelMapper;

	private static final String schemaExample = "{ \"name\": \"My rental\", \"surface\": 25.5, \"price\": 255.55, \"picture\": \"LINK_URL\", \"description\": \"A description\" }";

	@Operation(summary = "Create a new rental")
	@PostMapping(path="/api/rentals", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public RentalDTO createRental(
			// @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = Rental.class), examples = @ExampleObject(value = schemaExample))) 
			@ModelAttribute RentalFormWrapper rental) throws Exception {

			return convertToDto(rentalService.saveRental(rental));
	}

	@Operation(summary = "Get one rental by id")
	@GetMapping("/api/rentals/{id}")
	public RentalDTO getRental(@PathVariable("id") final Long id) {
		Optional<Rental> rental = rentalService.getRental(id);
		if (rental.isPresent()) {
			return convertToDto(rental.get());
		} else {
			return null;
		}
	}

	@Operation(summary = "Get all rentals")
	@GetMapping("/api/rentals")
	public String getRentals() {
		return convertIterableToDto(rentalService.getRentals());
	}

	@Operation(summary = "Update an existing rental")
	@PutMapping("/api/rentals/{id}")
	public RentalDTO updateRental(
		@PathVariable("id") final Long id, 
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = Rental.class), examples = @ExampleObject(value = schemaExample)))
		@ModelAttribute RentalFormWrapper rental ) throws Exception {
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

			// String picture = rental.getPicture();
			// if (picture != null) {
			// 	currentRental.setPicture(picture);
			// }

			String description = rental.getDescription();
			if (description != null) {
				currentRental.setDescription(description);
			}

			// rentalService.saveRental(currentRental, null);

			return convertToDto(currentRental);
		} else {
			return null;
		}
	}

	@Operation(summary = "Delete a rental")
	@DeleteMapping("/api/rentals/{id}")
	public void deleteRental(@PathVariable("id") final Long id) {
		rentalService.deleteRental(id);
	}

	private RentalDTO convertToDto(Rental rental) {
		RentalDTO rentalDTO = modelMapper.map(rental, RentalDTO.class);
		return rentalDTO;
	}

	private String convertIterableToDto(Iterable<Rental> rentals) {
		ArrayList<RentalDTO> rentalsDTO = new ArrayList<RentalDTO>();

		for (Rental r : rentals) {
			RentalDTO rentalDTO = modelMapper.map(r, RentalDTO.class);
			rentalsDTO.add(rentalDTO);
		}

		return "{ \"rentals\": "+ new Gson().toJson(rentalsDTO) +" }";
	}
}
