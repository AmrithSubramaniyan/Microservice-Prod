package com.customerservice.customerservice.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;

import com.customerservice.customerservice.entity.Customer;
import com.customerservice.customerservice.exception.CustomerAlreadyExistsException;
import com.customerservice.customerservice.exception.CustomerNotFoundException;
import com.customerservice.customerservice.repository.CustomerRepository;


@Service
public class CustomerServiceImpl implements CustomerService {

	private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);
	
	@Autowired
	private CustomerRepository customerRepository;
	
	public CustomerServiceImpl(CustomerRepository customerRepository) {
		this.customerRepository=customerRepository;
	}
	
	@Override
	@Cacheable(value = "customers")
	public List<Customer> fetchAllCustomers() {
		log.info("Fetching all customers from database");
		return customerRepository.findAll().stream()
				.filter(java.util.Objects::nonNull)
				.collect(java.util.stream.Collectors.toList());
	}

	@Override
	@Cacheable(value = "customer", key = "#id")
	public Optional<Customer> getCustomerById(Long id) {
		log.info("Fetching customer by id {} from database", id);
		return Optional.ofNullable(id)
				.flatMap(customerRepository::findById)
				.map(Optional::of)
				.orElseThrow(() -> {
					log.error("Customer not found for id: {}", id);
					return new CustomerNotFoundException(id);
				});
	}

	@Override
	@CacheEvict(value = "customers", allEntries = true)
	public Customer createCustomer(Customer newCustomer) {
		log.info("Creating new customer with email {}", newCustomer.getEmail());
		Optional.ofNullable(newCustomer.getEmail())
				.map(customerRepository::getCustomerByEmail)
				.ifPresent(existing -> {
					log.error("Customer already exists with email: {}", existing.getEmail());
					throw new CustomerAlreadyExistsException(existing.getEmail());
				});
		return customerRepository.save(newCustomer);
	}

	@Override
	@Caching(
		put = {@CachePut(value = "customer", key = "#id")},
		evict = {@CacheEvict(value = "customers", allEntries = true)}
	)
	public Customer updateCustomer(Customer newCustomer, Long id) {
		log.info("Updating customer with id {}", id);
		return customerRepository.findById(id)
				.map(custObj -> {
					custObj.setName(newCustomer.getName());
					custObj.setEmail(newCustomer.getEmail());
					custObj.setLocation(newCustomer.getLocation());
					custObj.setPhonenumber(newCustomer.getPhonenumber());
					log.debug("Updated customer fields for id {}", id);
					return customerRepository.save(custObj);
				})
				.orElseGet(() -> {
					log.warn("Attempted to update non-existent customer id {}", id);
					return customerRepository.save(null);
				});
	}

	@Override
	@Caching(
		evict = {
			@CacheEvict(value = "customer", key = "#id"),
			@CacheEvict(value = "customers", allEntries = true)
		}
	)
	public String deleteCustomer(Long id) {
		log.info("Deleting customer with id {}", id);
		return customerRepository.findById(id)
				.map(cust -> {
					customerRepository.delete(cust);
					log.debug("Successfully deleted customer id {}", id);
					return "Customer Successfully Deleted for id:" + id;
				})
				.orElseGet(() -> {
					log.warn("Attempted to delete non-existent customer id {}", id);
					return null;
				});
	}

	@Override
	public Customer getCustomerByEmail(String email) {
		log.info("Fetching customer by email: {}", email);
		return Optional.ofNullable(email)
				.map(customerRepository::getCustomerByEmail)
				.orElse(null);
	}

}