package com.rental.api.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.rental.api.model.Rental;
import com.rental.api.model.RentalFormWrapper;
import com.rental.api.model.User;
import com.rental.api.repository.RentalRepository;

import lombok.Data;

@Data
@Service
public class RentalService {

    @Value("${spring.env-var.folder-images.rentals}")
    private String folderImagesBase;

    @Autowired
    private UserService userService;

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

    public Rental saveRental(RentalFormWrapper rentalWrapper) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByMail((auth.getName()));
        String folder = folderImagesBase;
        MultipartFile pictureToSave = rentalWrapper.getPicture();

        Rental rental = new Rental();
        rental.setId(rentalWrapper.getId());
        rental.setName(rentalWrapper.getName());
        rental.setSurface(rentalWrapper.getSurface());
        rental.setPrice(rentalWrapper.getPrice());
        rental.setDescription(rentalWrapper.getDescription());
     
        if (rental.getId() != null) { // Do not allow to change the owner of a rental
            Optional<Rental> r = getRental(rental.getId());
            
            if (r.isPresent()) {
                Rental rentalFound = r.get();
                
                if (!user.getId().equals(rentalFound.getOwner().getId())) {
                    throw new Exception("You can't change the owner of a rental");
                }
            }
        }

        if (pictureToSave != null) {
            rental.setPicture("");
        }
 
        rental.setOwner(user); // join the rental to the owner 
  
        Rental savedRental = rentalRepository.save(rental);
  
        // save the picture and its URL thanks to the ID generated
        if (pictureToSave != null) {
            String extensionFile = StringUtils.getFilenameExtension(pictureToSave.getOriginalFilename());
            String[] splitId = (rental.getId()+"").split("");

            // path for the image
            for (String s : splitId) {
                folder += "/"+s;
            }

            String imagePath = folder +"/"+ rental.getId() +"."+ extensionFile;

            rental.setPicture(imagePath);
            saveUploadedImage(pictureToSave, folder, imagePath);

            savedRental = rentalRepository.save(rental); // save the URL of the image
        }

        System.out.println("ID "+ rental.getId());
        return savedRental;
    }

   private void saveUploadedImage(MultipartFile file, String folder, String imagePath) throws IOException {
        File theDir = new File(folder); // create the folders just in case they don't
        if (!theDir.exists()) {
            theDir.mkdirs();
        }

        byte[] bytes = file.getBytes(); // create the image
        Path path = Paths.get(imagePath);
        Files.write(path, bytes);
    }
}
