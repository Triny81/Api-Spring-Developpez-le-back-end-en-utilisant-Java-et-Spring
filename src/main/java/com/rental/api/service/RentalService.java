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

import com.rental.api.formWrapper.RentalFormWrapper;
import com.rental.api.model.Rental;
import com.rental.api.model.User;
import com.rental.api.repository.RentalRepository;

import lombok.Data;

@Data
@Service
public class RentalService {

    @Value("${spring.env-var.folder-images.rentals}")
    private String folderImagesBase;

    @Value("${spring.application.base-url}")
    private String baseUrl;
    
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
        Rental rental;
        Rental savedRental;

        if (rentalWrapper.getId() == null) { // create
            rental = new Rental(); 

            rental.setName(rentalWrapper.getName());
            rental.setSurface(rentalWrapper.getSurface());
            rental.setPrice(rentalWrapper.getPrice());
            rental.setDescription(rentalWrapper.getDescription());
            rental.setOwner(user); // join the rental to the owner
            rental.setPicture(""); // give a any value to the field picture first to avoid problems when saving      
      
            savedRental = rentalRepository.save(rental); // save the rental to get an ID which will be used for the URL of the photo
      
            if (pictureToSave != null) {
                String extensionFile = StringUtils.getFilenameExtension(pictureToSave.getOriginalFilename());
                String[] splitId = (rental.getId()+"").split("");
    
                // path for the image
                for (String s : splitId) {
                    folder += "/"+s;
                }
    
                String imagePathFolder = folder +"/"+ rental.getId() +"."+ extensionFile;
                String imagePathDB = imagePathFolder.replace("src/main/resources/static", baseUrl); // URL EN DUR ???? PAS UNE BONNE PRATIQUE
    
                rental.setPicture(imagePathDB); // save the URL of the image
                saveUploadedImage(pictureToSave, imagePathFolder);
    
                savedRental = rentalRepository.save(rental); 
            }

        } else { // update
            rental = getRental(rentalWrapper.getId()).get();

            Optional<Rental> r = getRental(rental.getId());
            
            if (r.isPresent()) {  
                Rental rentalFound = r.get();
                
                if (!user.getId().equals(rentalFound.getOwner().getId())) {
                    throw new Exception("You can't change the owner of a rental"); // Do not allow to change the owner of a rental
                }
            }
            
            rental.setName(rentalWrapper.getName());
            rental.setSurface(rentalWrapper.getSurface());
            rental.setPrice(rentalWrapper.getPrice());
            rental.setDescription(rentalWrapper.getDescription());
            // rental.setOwner(user); // join the rental to the owner
            // rental.setPicture(rental.getPicture()); // picture musn't be updated
         
            savedRental = rentalRepository.save(rental); 
        }

        return savedRental;
    }

   private void saveUploadedImage(MultipartFile file, String imagePath) throws IOException {
        String folderPath = imagePath.replace(StringUtils.getFilename(imagePath), ""); // remove to the path the image name

        File theDir = new File(folderPath); // create the folders if they don't exist
        if (!theDir.exists()) {
            theDir.mkdirs();
        }

        byte[] bytes = file.getBytes(); // create the image
        Path path = Paths.get(imagePath);
        Files.write(path, bytes);
    }
}
