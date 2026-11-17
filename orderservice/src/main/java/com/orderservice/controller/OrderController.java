package com.orderservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.orderservice.entity.CustomerResponse;
import com.orderservice.entity.Orders;
import com.orderservice.service.CustomerService;
import com.orderservice.service.OrderService;



import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@RestController
@RateLimiter(name = "orderServiceRL")
public class OrderController {
	
	private static final Logger log = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private OrderService orderService;
	
	@PostMapping("/orders/placeorder")
	public ResponseEntity<String> postMethodName(@RequestBody Orders newOrder) {
		log.info("Received request to place order for customer id: {}", newOrder.getCustomerId());
		//fetch customer from customer service for the given cust id
		CustomerResponse customerResponse=customerService.getCustomer(newOrder.getCustomerId());
		if((customerResponse.getErrorMsg()!=null) && customerResponse.getErrorMsg().contains("Customer Not found")) {
			log.warn("Customer not found for id: {}", newOrder.getCustomerId());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(customerResponse.getErrorMsg());
			
		}else if((customerResponse.getErrorMsg()!=null) && customerResponse.getErrorMsg().contains("Customer Service Temporarily Unavailable"))
		{
			log.error("Customer service is temporarily unavailable while placing order for customer id: {}", newOrder.getCustomerId());
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Customer Service Temporarily Unavailable. Please try later.");
		}
		
		log.info("Successfully placed order for customer id: {}", newOrder.getCustomerId());
		return ResponseEntity.ok("Order Placed for customer: "+orderService.createOrder(newOrder));
	}
	
	@GetMapping("/orders")
	public ResponseEntity<List<Orders>> getAllOrders() {
		log.info("Fetching all orders");
		return ResponseEntity.ok(orderService.getAllOrders());
	}
	
	

}