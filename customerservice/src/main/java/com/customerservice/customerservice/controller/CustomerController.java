package com.customerservice.customerservice.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.customerservice.customerservice.entity.Customer;
import com.customerservice.customerservice.service.CustomerService;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@RestController
@RateLimiter(name = "customerServiceRL")
public class CustomerController {
	
	private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/customers")
	public ResponseEntity<List<Customer>> fetchAllCustomers() {
		log.info("Fetching all customers");
		return  ResponseEntity.ok(customerService.fetchAllCustomers());
	}
	
	@PostMapping("/customers")
	public ResponseEntity<Customer> createCustomer(@RequestBody Customer newCustomer) {
		log.info("Creating new customer with email: {}", newCustomer.getEmail());
		return  ResponseEntity.ok(customerService.createCustomer(newCustomer));
	}
	
	@PutMapping("/customers/{id}")
	public ResponseEntity<Customer> updateCustomer(@RequestBody Customer updatedCust, @PathVariable("id") Long id) {
		log.info("Updating customer with id: {}", id);
		return  ResponseEntity.ok(customerService.updateCustomer(updatedCust,id));
	}
	
	@GetMapping("/customers/{id}")
	public ResponseEntity<Optional<Customer>> getCustomerById(@PathVariable("id") Long id) {
		log.info("Fetching customer by id: {}", id);
		return  ResponseEntity.ok(customerService.getCustomerById(id));
	}	
	
	@DeleteMapping("/customers/{id}")
	public ResponseEntity<String> deleteCustomer(@PathVariable("id") Long id) {
		log.info("Deleting customer with id: {}", id);
		return  ResponseEntity.ok(customerService.deleteCustomer(id));
	}
}