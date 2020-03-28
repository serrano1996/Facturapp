package com.daw.facturapp.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class FacturappApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(FacturappApplication.class, args);
	}
	
	/**
	 * Necesario para generar el fichero war.
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder app) {
		return app.sources(FacturappApplication.class);
	}

}
