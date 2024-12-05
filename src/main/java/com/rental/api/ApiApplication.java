package com.rental.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() { // allow CORS
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(@SuppressWarnings("null") CorsRegistry registry) {
				registry.addMapping("/**");
			}
		};
	}
}
