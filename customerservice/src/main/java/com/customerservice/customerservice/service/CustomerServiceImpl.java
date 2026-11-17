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
		return customerRepository.findAll();
	}

	@Override
	@Cacheable(value = "customer", key = "#id")
	public Optional<Customer> getCustomerById(Long id) {
		log.info("Fetching customer by id {} from database", id);
		Optional<Customer> optCust= customerRepository.findById(id);
		if(optCust.isEmpty()) {
			log.error("Customer not found for id: {}", id);
			throw new CustomerNotFoundException(id);
		}
		return optCust;
	}

	@Override
	@CacheEvict(value = "customers", allEntries = true)
	public Customer createCustomer(Customer newCustomer) {
		log.info("Creating new customer with email {}", newCustomer.getEmail());
		Customer customer=  customerRepository.getCustomerByEmail(newCustomer.getEmail());
		if(customer!=null) {
			log.error("Customer already exists with email: {}", newCustomer.getEmail());
			throw new CustomerAlreadyExistsException(newCustomer.getEmail());
		}
		return customerRepository.save(newCustomer);
	}

	@Override
	@Caching(
		put = {@CachePut(value = "customer", key = "#id")},
		evict = {@CacheEvict(value = "customers", allEntries = true)}
	)
	public Customer updateCustomer(Customer newCustomer, Long id) {
		log.info("Updating customer with id {}", id);
		Optional<Customer> custOptional=customerRepository.findById(id);
		Customer custObj=null;
		if(custOptional.isPresent()) {
			custObj=custOptional.get();
			custObj.setName(newCustomer.getName());
			custObj.setEmail(newCustomer.getEmail());
			custObj.setLocation(newCustomer.getLocation());
			custObj.setPhonenumber(newCustomer.getPhonenumber());
			log.debug("Updated customer fields for id {}", id);
		} else {
			log.warn("Attempted to update non-existent customer id {}", id);
		}
		return customerRepository.save(custObj);
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
		Optional<Customer> custOptional=customerRepository.findById(id);
		Customer custObj=null;
		String deleteMsg=null;
		if(custOptional.isPresent()) {
			custObj=custOptional.get();
			customerRepository.delete(custObj);
			deleteMsg="Customer Successfully Deleted for id:"+id;
			log.debug("Successfully deleted customer id {}", id);
		} else {
			log.warn("Attempted to delete non-existent customer id {}", id);
		}
		return deleteMsg;
	}

	@Override
	public Customer getCustomerByEmail(String email) {
		log.info("Fetching customer by email: {}", email);
		return customerRepository.getCustomerByEmail(email);
	}

}