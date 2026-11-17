package com.orderservice.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.orderservice.entity.Orders;
import com.orderservice.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService{
	
	private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderEventProducer orderEventProducer;

	public OrderServiceImpl(OrderRepository orderRepository, OrderEventProducer orderEventProducer) {
		this.orderRepository = orderRepository;
		this.orderEventProducer = orderEventProducer;
	}

	@Override
	@Cacheable(value = "orders")
	public List<Orders> getAllOrders() {
		log.info("Fetching all orders from database");
		return orderRepository.findAll();
	}

	@Override
	@CacheEvict(value = "orders", allEntries = true)
	public Orders createOrder(Orders neworder) {
		log.info("Saving new order to database for customer id: {}", neworder.getCustomerId());
		Orders savedOrder = orderRepository.save(neworder);

		try {
			double totalAmount = (savedOrder.getPrice() != null ? savedOrder.getPrice() : 0.0)
					* (savedOrder.getQuantity() != null ? savedOrder.getQuantity() : 1);

			com.orderservice.dto.OrderCreatedEvent event = new com.orderservice.dto.OrderCreatedEvent(
					savedOrder.getId(),
					savedOrder.getCustomerId(),
					savedOrder.getName(),
					savedOrder.getPrice(),
					savedOrder.getQuantity(),
					totalAmount,
					"CREATED",
					java.time.LocalDateTime.now()
			);
			orderEventProducer.publishOrderCreatedEvent(event);
		} catch (Exception e) {
			log.error("Exception occurred while dispatching OrderCreatedEvent for order id {}: {}",
					savedOrder.getId(), e.getMessage(), e);
		}

		return savedOrder;
	}

}