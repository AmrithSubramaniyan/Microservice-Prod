package com.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.customerservice.customerservice.entity.Customer;

@FeignClient(name="customer-service")
public interface CustomerClient {
	@GetMapping("/customers/{id}")
	Customer getCustomerById(@PathVariable("id") Long id);
}
