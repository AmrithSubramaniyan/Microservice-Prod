package com.orderservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.orderservice.client.CustomerClient;
import com.customerservice.customerservice.entity.Customer;
import com.customerservice.customerservice.exception.CustomerNotFoundException;
import com.orderservice.entity.CustomerResponse;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class CustomerService {
	
	@Autowired
	private CustomerClient customerClient;
	
	@CircuitBreaker(name="customerServiceCB", fallbackMethod="fallbackGetCustomer")
	public CustomerResponse getCustomer(Long id) {
		CustomerResponse customerResponse=new CustomerResponse();
		Customer customer=customerClient.getCustomerById(id);
		if(customer!=null) {
			customerResponse.setCustomer(customer);
			customerResponse.setIsError(false);
		}
		return  customerResponse;
	}
	
	public CustomerResponse fallbackGetCustomer(Long id, Throwable t) {
		CustomerResponse customerResponse=new CustomerResponse();
		if(t instanceof CustomerNotFoundException) {
			customerResponse.setIsError(true);
			customerResponse.setErrorMsg(t.getMessage());
		}else {
			customerResponse.setIsError(true);
			customerResponse.setErrorMsg("Customer Service Temporarily Unavailable. Please try later.");
		}
		
		return customerResponse;
	}

}