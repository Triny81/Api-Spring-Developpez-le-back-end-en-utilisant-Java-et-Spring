package com.rental.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rental.api.model.Rental;
import com.rental.api.repository.RentalRepository;

import lombok.Data;

@Data
@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    public Optional<Rental> getRental(final Long id) {
        return rentalRepository.findById(id);
    }

    public Iterable<Rental> getRentals() {
        return rentalRepository.findAll();
    }

    public void deleteRental(final Long id) {
        rentalRepository.deleteById(id);
    }

    public Rental saveRental(Rental rental) {

        if (rental.getPicture() != null) { 
            // TODO : télécharger image dans un dossier et sauvegarder en BD l'url de cette image
        }
        Rental savedRental = rentalRepository.save(rental);
        return savedRental;
    }
}
