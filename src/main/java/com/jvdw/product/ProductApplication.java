package com.jvdw.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This Product REST API demonstrates best practices including
 * E2E Integration testing, Unit testing, automatic Swagger OAS 3 documentation generation,
 * interceptor logging, global exception handling, Lombok and more
 * Architecture styles and Design patterns used include Layered Architecture, REST, DTO pattern,
 * Repository pattern, and Interceptor pattern
 */
@SpringBootApplication
public class ProductApplication
{
	// run the application
	public static void main(String[] args) {
		SpringApplication.run(ProductApplication.class, args);
	}
}
