package com.rental.api.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class ImagesController {
    @Value("${spring.env-var.folder-images.rentals}")
    private String folderImagesBase;

    @Operation(summary = "Get an image with its path in the URL")
    @GetMapping("api/images/rentals/**")
    public ResponseEntity<Resource> getImage(HttpServletRequest request) throws IOException {
       
        String uri = request.getRequestURI().toString();
        String imagePath = uri.replace("/api/images/rentals", "");

        Path path = Paths.get(folderImagesBase + imagePath); // get relative path of the image
      
        Resource resource = new UrlResource(path.toUri());
        
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }
}
