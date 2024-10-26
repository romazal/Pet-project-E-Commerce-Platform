package com.romazal.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ProductMovementsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductMovementsApplication.class, args);
	}

}
