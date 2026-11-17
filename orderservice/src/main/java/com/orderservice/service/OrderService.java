package com.orderservice.service;

import java.util.List;

import com.orderservice.entity.Orders;

public interface OrderService {
  List<Orders> getAllOrders();
  Orders createOrder(Orders neworder);
}