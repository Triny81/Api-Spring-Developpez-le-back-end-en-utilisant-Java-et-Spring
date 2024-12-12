package com.rental.api.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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

import com.rental.api.dto.RentalDTO;
import com.rental.api.dto.RentalDTOFormWrapper;
import com.rental.api.model.Rental;
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
	public String createRental(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The owner of the rental is automatically the user who is sending the post request.<br> The picture can be a PNG or a JPG.", required = true, content = @Content(mediaType = "multipart/form-data", schema = @Schema(implementation = RentalDTOFormWrapper.class)))
			@ModelAttribute RentalDTOFormWrapper rental
			) throws Exception {

			rentalService.saveRental(rental);
			return "{ \"message\": \"Rental created\" }";
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
	public Map<String, ArrayList<RentalDTO>> getRentals() {
		return convertIterableToDto(rentalService.getRentals());
	}

	@Operation(summary = "Update an existing rental")
	@PutMapping(path="/api/rentals/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public String updateRental(
		@PathVariable("id") final Long id, 
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Only the owner of the rental can update its rental.<br> Don't need to upload a picture, it won't be taken into consideration.", required = true, content = @Content(mediaType = "multipart/form-data", schema = @Schema(implementation = RentalDTOFormWrapper.class), examples = @ExampleObject(value = schemaExample)))
		@ModelAttribute RentalDTOFormWrapper rental) throws Exception {
		Optional<Rental> r = rentalService.getRental(id);
	
		if (r.isPresent()) {
			Rental currentRental = r.get();
			rental.setId(id);
	
			if (rental.getName() == null) {
				rental.setName(currentRental.getName());
			}

			if (rental.getSurface() == null) {
				rental.setSurface(currentRental.getSurface());
			}

			if (rental.getPrice() == null) {
				rental.setPrice(currentRental.getPrice());
			}

			if (rental.getDescription() == null) {
				rental.setDescription(currentRental.getDescription());
			}
	
			rentalService.saveRental(rental);
			return "{ \"message\": \"Rental updated\" }";
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

	private Map<String, ArrayList<RentalDTO>> convertIterableToDto(Iterable<Rental> rentals) {
		ArrayList<RentalDTO> rentalsDTO = new ArrayList<RentalDTO>();

		for (Rental r : rentals) {
			RentalDTO rentalDTO = modelMapper.map(r, RentalDTO.class);
			rentalsDTO.add(rentalDTO);
		}

		Map<String, ArrayList<RentalDTO>> map = new HashMap<>(); 
		map.put("rentals", rentalsDTO);

		return map;
	}
}
