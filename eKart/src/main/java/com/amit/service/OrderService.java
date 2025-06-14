package com.amit.service;

import com.amit.dto.OrderDTO;
import com.amit.entity.Order;

public interface OrderService {
    Order createOrder(OrderDTO orderDTO);

    Order getOrderById(String orderId);

    Order saveOrder(Order order);

    Order getOrderByRazorpayOrderId(String razorpayOrderId);
}
